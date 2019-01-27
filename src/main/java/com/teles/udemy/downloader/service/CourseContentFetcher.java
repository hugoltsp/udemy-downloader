package com.teles.udemy.downloader.service;

import com.teles.udemy.downloader.domain.dto.Course;
import com.teles.udemy.downloader.domain.dto.Course.CourseContent;
import com.teles.udemy.downloader.domain.dto.CourseResponse.Lecture;
import com.teles.udemy.downloader.domain.dto.LectureAssetsResponse;
import com.teles.udemy.downloader.domain.dto.LectureAssetsResponse.Caption;
import com.teles.udemy.downloader.domain.dto.SubscribedCoursesResponse.SubscribedCourse;
import com.teles.udemy.downloader.domain.settings.ApplicationSettings;
import com.teles.udemy.downloader.resource.UdemyResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseContentFetcher {

    private final UdemyResource udemyResource;
    private final ApplicationSettings settings;

    public CourseContentFetcher(UdemyResource udemyResource, ApplicationSettings settings) {
        this.udemyResource = udemyResource;
        this.settings = settings;
    }

    public List<CourseContent> fetch() {

        List<Course> courses = new ArrayList<>();

        log.info("Getting content from subscribed courses...");

        List<SubscribedCourse> subscribedCoursesCourses = udemyResource.getSubscribedCourses().getCourses();

        log.info("A total of [{}] enrolled courses found. Titles are: {}", subscribedCoursesCourses.size(), subscribedCoursesCourses
                .stream()
                .map(SubscribedCourse::getTitle)
                .collect(Collectors.joining("\n")));

        if (settings.shouldFilterCourses()) {
            subscribedCoursesCourses = subscribedCoursesCourses
                    .stream()
                    .filter(subscribedCourse -> settings.getCourses().contains(subscribedCourse.getTitle()))
                    .collect(Collectors.toList());

            log.info("Will download lectures from the following courses: [{}]", subscribedCoursesCourses
                    .stream()
                    .map(SubscribedCourse::getTitle)
                    .collect(Collectors.joining("\n")));
        }

        subscribedCoursesCourses.stream().map(Course::build).peek(courses::add).forEach(course -> {

            log.info("Getting lectures from course: [{}]", course.getCourseName());

            List<Lecture> lectures = udemyResource.getCourse(course.getCourseId())
                    .getLectures()
                    .stream()
                    .filter(Lecture::hasAsset)
                    .collect(Collectors.toList());

            log.info("Found [{}] lectures.", lectures.size());

            lectures.parallelStream().forEach(lecture -> {

                LectureAssetsResponse assetsResponse = udemyResource.getAssets(lecture.getAsset().getId());

                if (assetsResponse.hasContent()) {

                    CourseContent courseContent = new CourseContent();
                    courseContent.setCourseName(course.getCourseName());
                    courseContent.setLectureName(lecture.getTitle());
                    courseContent.setFileName(udemyResource.getLectureDetails(lecture.getAsset().getId()).getTitle());
                    courseContent.setFileUrl(assetsResponse.getStreamContents()
                            .getVideos()
                            .stream()
                            .filter(v -> v.getResolution().equals(settings.getVideoResolution()))
                            .findFirst()
                            .get()
                            .getFile());

                    List<CourseContent> collect = udemyResource.getLectureCaptions(lecture.getAsset().getId())
                            .getCaptions()
                            .stream()
                            .filter(this::hasLocale)
                            .map(c -> {
                                CourseContent s = new CourseContent();
                                s.setFileName(c.getFileName());
                                s.setFileUrl(c.getUrl());
                                s.setCourseName(course.getCourseName());
                                s.setLectureName(lecture.getTitle());
                                return s;
                            }).collect(Collectors.toList());

                    course.getContents().add(courseContent);
                    course.getContents().addAll(collect);

                }

            });

        });

        return courses.stream().map(Course::getContents).flatMap(Collection::stream).collect(Collectors.toList());
    }

    private boolean hasLocale(Caption caption) {
        Map<String, String> locale = caption.getLocale();

        return locale.containsKey("locale") && (locale.get("locale").equals("en_US")
                || locale.get("locale").equals(settings.getSubtitleLanguage()));
    }
}

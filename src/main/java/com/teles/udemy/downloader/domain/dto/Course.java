package com.teles.udemy.downloader.domain.dto;

import com.teles.udemy.downloader.domain.dto.LectureAssetsResponse.Caption;
import com.teles.udemy.downloader.domain.dto.SubscribedCoursesResponse.SubscribedCourse;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Course {

    private Long courseId;

    private String courseName;

    private List<CourseContent> contents = new ArrayList<>();

    public static Course build(SubscribedCourse subscribedCourse) {
        Course course = new Course();
        course.setCourseId(subscribedCourse.getId());
        course.setCourseName(subscribedCourse.getTitle());
        return course;
    }

    @Data
    public static class CourseContent {

        private String courseName;

        private String lectureName;

        private String videoFileName;

        private String videoUrl;

        private List<Subtitle> subtitles = new ArrayList<>();

        @Data
        public static class Subtitle{

            private String subtitleFileName;

            private String subtitleUrl;

            public static Subtitle build(Caption caption){
                Subtitle subtitle = new Subtitle();
                subtitle.setSubtitleFileName(caption.getFileName());
                subtitle.setSubtitleUrl(caption.getUrl());
                return subtitle;
            }

        }

    }

}

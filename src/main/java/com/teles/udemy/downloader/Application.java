package com.teles.udemy.downloader;

import com.teles.udemy.downloader.domain.dto.CourseResponse;
import com.teles.udemy.downloader.domain.dto.SubscribedCoursesResponse;
import com.teles.udemy.downloader.resource.UdemyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private UdemyResource resource;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

//        SubscribedCoursesResponse subscribedCoursesResponse = resource.getSubscribedCourses().get();
//        CourseResponse courseResponse = resource.getCourse(496050L).get();
//
//        System.out.println(subscribedCoursesResponse);
//
//        System.out.println(courseResponse);
//
//        courseResponse.getLectures().stream().map(CourseResponse.Lecture::getAsset).forEach(System.out::println);

        System.out.println(resource.getLectureAssets(4962922L));
    }

}


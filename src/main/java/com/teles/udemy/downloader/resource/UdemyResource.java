package com.teles.udemy.downloader.resource;

import com.teles.udemy.downloader.domain.dto.CourseResponse;
import com.teles.udemy.downloader.domain.dto.LectureAssetsResponse;
import com.teles.udemy.downloader.domain.dto.SubscribedCoursesResponse;
import com.teles.udemy.downloader.domain.settings.ApplicationSettings;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.optionals.OptionalDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

public interface UdemyResource {

    @RequestLine("GET /users/me/subscribed-courses?fields")
    Optional<SubscribedCoursesResponse> getSubscribedCourses();

    @RequestLine("GET /courses/{courseId}/cached-subscriber-curriculum-items/?page_size=1400")
    Optional<CourseResponse> getCourse(@Param("courseId") Long courseId);

    @RequestLine("GET /assets/{assetId}?fields[asset]=stream_urls")
    Optional<LectureAssetsResponse> getLectureAssets(@Param("assetId") Long assetId);

    @Configuration
    class Config {

        private final ApplicationSettings settings;

        Config(ApplicationSettings settings) {
            this.settings = settings;
        }

        @Bean
        public UdemyResource resource() {

            return Feign.builder()
                    .client(new OkHttpClient())
                    .decoder(new OptionalDecoder(new JacksonDecoder()))
                    .encoder(new JacksonEncoder())
                    .requestInterceptor(template -> template
                            .header("Authorization", "Bearer " + settings.getHeaders().getAuthorization())
                            .header("X-Udemy-Authorization", "Bearer " + settings.getHeaders().getXUdemyAuthorization()))
                    .decode404()
                    .target(UdemyResource.class, settings.getApiUrl());
        }

    }

}
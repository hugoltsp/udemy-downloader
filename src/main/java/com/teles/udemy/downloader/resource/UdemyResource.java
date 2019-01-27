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

public interface UdemyResource {

    @RequestLine("GET /users/me/subscribed-courses?fields")
    SubscribedCoursesResponse getSubscribedCourses();

    @RequestLine("GET /courses/{courseId}/cached-subscriber-curriculum-items/?page_size=1400")
    CourseResponse getCourse(@Param("courseId") Long courseId);

    @RequestLine("GET /courses/{courseId}/cached-subscriber-curriculum-items/?page_size=1400&fields[lecture]=@min,object_index,asset,supplementary_assets,sort_order,is_published,is_free&fields[quiz]=@min,object_index,title,sort_order,is_published&fields[practice]=@min,object_index,title,sort_order,is_published&fields[chapter]=@min,description,object_index,title,sort_order,is_published&fields[asset]=@min,title,filename,asset_type,external_url,length,status")
    Object getCourse2(@Param("courseId") Long courseId);

    @RequestLine("GET /assets/{assetId}?fields[asset]=stream_urls")
    LectureAssetsResponse getAssets(@Param("assetId") Long assetId);

    @RequestLine("GET /assets/{assetId}")
    LectureAssetsResponse getLectureDetails(@Param("assetId") Long assetId);

    @RequestLine("GET /assets/{assetId}?fields[asset]=captions")
    LectureAssetsResponse getLectureCaptions(@Param("assetId") Long assetId);

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
                            .header("Authorization", getToken())
                            .header("X-Udemy-Authorization", getToken()))
                    .decode404()
                    .target(UdemyResource.class, settings.getApiUrl());
        }

        private String getToken() {
            return "Bearer " + settings.getAuthorizationToken();
        }

    }

}

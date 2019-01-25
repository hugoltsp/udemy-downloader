package com.teles.udemy.downloader.domain.settings;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Data
@Component
@ConfigurationProperties("app")
public class ApplicationSettings {

    private String apiUrl;

    private String authorizationToken;

    private String videoResolution;

    private int concurrentDownloads;

    private String output;

    private String courses;

    public boolean shouldFilterCourses() {
        return courses != null && !courses.trim().isEmpty();
    }

    @PostConstruct
    private void init() {
        log.info(toString());
    }

}

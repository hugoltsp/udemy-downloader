package com.teles.udemy.downloader.domain.settings;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotBlank;

@Slf4j
@Data
@Validated
@Component
@ConfigurationProperties("app")
public class ApplicationSettings {

    @NotBlank
    private String apiUrl;

    private Headers headers;

    @PostConstruct
    private void init(){
      log.info(toString());
    }

    @Data
    public static class Headers {

        @NotBlank
        private String authorization;

        @NotBlank
        private String xUdemyAuthorization;

    }

}

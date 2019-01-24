package com.teles.udemy.downloader.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SubscribedCoursesResponse {

    @JsonProperty("results")
    private List<SubscribedCourse> courses = new ArrayList<>();

    @Data
    public static class SubscribedCourse {

        private Long id;

        private String title;

        private String url;

    }
}

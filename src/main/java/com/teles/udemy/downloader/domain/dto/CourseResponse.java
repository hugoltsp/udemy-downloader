package com.teles.udemy.downloader.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CourseResponse {

    private int count;

    @JsonProperty("results")
    private List<Lecture> lectures = new ArrayList<>();

    @Data
    public static class Lecture {

        private Long id;

        private String title;

        private Asset asset;
    }

    @Data
    public static class Asset {

        private Long id;

        private String fileName;

    }

}

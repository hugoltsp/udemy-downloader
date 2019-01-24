package com.teles.udemy.downloader.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LectureAssetsResponse {

    private String title;

    @JsonProperty("stream_urls")
    private StreamContent streamContents;

    @Data
    public static class StreamContent {

        @JsonProperty("Video")
        private List<Video> videos = new ArrayList<>();

    }

    @Data
    public static class Video {

        private String label;

        private String file;
    }

}

package com.teles.udemy.downloader.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class LectureAssetsResponse {

    private Long id;

    private String title;

    @JsonProperty("stream_urls")
    private StreamContent streamContents;

    private List<Caption> captions = new ArrayList<>();

    public boolean hasContent() {
        return streamContents != null;
    }

    @Data
    public static class StreamContent {

        @JsonProperty("Video")
        private List<Video> videos = new ArrayList<>();

    }

    @Data
    public static class Caption {

        @JsonProperty("title")
        private String fileName;

        private String url;

        private Map<String, String> locale = new LinkedHashMap<>();
    }

    @Data
    public static class Video {

        @JsonProperty("label")
        private String resolution;

        private String file;
    }

}

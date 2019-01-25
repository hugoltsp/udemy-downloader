package com.teles.udemy.downloader;

import com.teles.udemy.downloader.service.CourseContentFetcher;
import com.teles.udemy.downloader.service.Downloader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final CourseContentFetcher fetcher;
    private final Downloader downloader;

    public static void main(String[] args) {
        //System.setProperty("app.authorizationToken","sadasda");
        SpringApplication.run(Application.class, args);
    }

    public Application(CourseContentFetcher fetcher, Downloader downloader) {
        this.fetcher = fetcher;
        this.downloader = downloader;
    }

    @Override
    public void run(String... args) throws Exception {
        downloader.downloadContents(fetcher.fetch());
    }

}


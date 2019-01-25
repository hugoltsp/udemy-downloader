package com.teles.udemy.downloader;

import com.teles.udemy.downloader.service.CourseContentFetcher;
import com.teles.udemy.downloader.service.Downloader;
import org.apache.commons.cli.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Options OPTIONS = buildOptions();

    private final CourseContentFetcher fetcher;
    private final Downloader downloader;

    public static void main(String[] args) throws ParseException {
        CommandLine commandLine = new DefaultParser().parse(OPTIONS, args);
        System.setProperty("app.authorizationToken", commandLine.getOptionValue("token"));

        if (commandLine.hasOption("courses")) {
            System.setProperty("app.courses", commandLine.getOptionValue("courses"));
        }

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

    private static Options buildOptions() {

        Options options = new Options();

        options.addOption(Option.builder().longOpt("token").hasArg().required().build());
        options.addOption(Option.builder().longOpt("courses").hasArg().build());

        return options;
    }

}


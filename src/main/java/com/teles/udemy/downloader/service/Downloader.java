package com.teles.udemy.downloader.service;

import com.teles.udemy.downloader.domain.dto.Course.CourseContent;
import com.teles.udemy.downloader.domain.settings.ApplicationSettings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class Downloader {

    private static final int EOF = -1;
    private static final int BUFFER_SIZE = 16384;
    private static final String SLASH = "/";

    private final ExecutorService executor;
    private final ApplicationSettings settings;

    public Downloader(ExecutorService executor, ApplicationSettings settings) {
        this.executor = executor;
        this.settings = settings;
    }

    public void downloadContents(List<CourseContent> contents) throws InterruptedException {

        contents.stream().map(this::download).forEach(executor::execute);

        executor.shutdown();

        while (!executor.isTerminated()) {
            Thread.sleep(1000);
        }

    }

    private Runnable download(CourseContent c) {
        return () -> {
            try {

                log.info("Downloading file: [{}] from course: [{}]", c.getFileName(), c.getCourseName());

                try (OutputStream outputStream = new BufferedOutputStream(createFile(c), BUFFER_SIZE);
                     InputStream inputStream = new URL(c.getFileUrl()).openStream()) {
                    write(outputStream, inputStream);
                }

            } catch (Exception e) {
                log.error("An error ocurred while trying to download this file [{}]", c, e);
            }
        };
    }

    private FileOutputStream createFile(CourseContent c) throws FileNotFoundException {
        File file = new File(new StringBuilder()
                .append(settings.getOutput())
                .append(SLASH)
                .append(buildFileName(c))
                .toString());
        file.getParentFile().mkdirs();
        return new FileOutputStream(file);
    }

    private void write(OutputStream outputStream, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int read;
        while (EOF != (read = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, read);
        }
    }

    private String buildFileName(CourseContent c) {
        return new StringBuilder()
                .append(c.getCourseName()).append(SLASH)
                .append(c.getLectureName()).append(SLASH)
                .append(c.getFileName()).toString();
    }

}

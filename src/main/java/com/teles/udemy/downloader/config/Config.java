package com.teles.udemy.downloader.config;

import com.teles.udemy.downloader.domain.settings.ApplicationSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class Config {

    @Bean
    public ExecutorService executor(ApplicationSettings settings){
        return Executors.newFixedThreadPool(settings.getConcurrentDownloads());
    }
}

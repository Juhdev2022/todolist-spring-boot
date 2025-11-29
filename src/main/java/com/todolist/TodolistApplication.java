package com.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class TodolistApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodolistApplication.class, args);
    }

    @Bean
    CommandLineRunner initDataDir() {
        return args -> {
            Path dataDir = Path.of("data");
            if (!Files.exists(dataDir)) {
                Files.createDirectories(dataDir);
            }
        };
    }

}

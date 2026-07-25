package com.bookcorner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookCornerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookCornerApplication.class, args);
    }
}

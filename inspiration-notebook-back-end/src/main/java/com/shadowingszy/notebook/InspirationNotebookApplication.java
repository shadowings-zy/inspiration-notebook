package com.shadowingszy.notebook;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.shadowingszy.notebook.mapper")
@SpringBootApplication
public class InspirationNotebookApplication {

    public static void main(String[] args) {
        SpringApplication.run(InspirationNotebookApplication.class, args);
    }

}

package com.project.sweprojectspring;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
public class SweProjectSpringApplication {

    public static void main(String[] args) {
        Application.launch(TestApplication.class,args);
    }

}

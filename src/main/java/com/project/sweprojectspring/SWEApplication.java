package com.project.sweprojectspring;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

import java.awt.*;

import javafx.scene.paint.Color;

@ImportResource("classpath:META-INF/persistence.xml")
@EnableAutoConfiguration
@ComponentScan
public class SWEApplication extends Application {
    private ConfigurableApplicationContext ApplicationContext;

    public static Color BackgroundColor;
    public static Color PrimaryColor;

    @Override
    public void init() throws Exception {
        BackgroundColor=Color.web("#FFA500");
        PrimaryColor= Color.web("#ffffff");


        ApplicationContext=new SpringApplicationBuilder(SweProjectSpringApplication.class).run();
    }
    @Override
    public void start(Stage stage) throws Exception {
        ApplicationContext.publishEvent(new StageReadyEvent(stage));

    }

    @Override
    public void stop() throws Exception {
        ApplicationContext.close();
        Platform.exit();
    }

    static class StageReadyEvent extends ApplicationEvent {
        public StageReadyEvent(Stage stage) {
            super(stage);
        }

        public Stage getStage() {
            return (Stage) getSource();
        }
    }
}

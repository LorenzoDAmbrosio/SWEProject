package com.project.sweprojectspring;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class TestApplication extends Application {
    private ConfigurableApplicationContext ApplicationContext;

    @Override
    public void init() throws Exception {
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

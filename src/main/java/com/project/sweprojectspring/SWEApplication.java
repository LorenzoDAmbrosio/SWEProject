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

@ImportResource("classpath:META-INF/persistence.xml")
@EnableAutoConfiguration
@ComponentScan
public class SWEApplication extends Application {
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

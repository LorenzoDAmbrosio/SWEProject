package com.project.sweprojectspring;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<TestApplication.StageReadyEvent> {
    private String applicationTitle;

    @Value("classpath:/main/main-menu.fxml")
    private Resource mainMenuResource;

    public StageInitializer(@Value("VelvetView") String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }


    @Override
    public void onApplicationEvent(TestApplication.StageReadyEvent event) {
        Stage stage=event.getStage();

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(DependencyInjectionConfig.class);
        context.refresh();
        try {
            FXMLLoader loader = new FXMLLoader(mainMenuResource.getURL());

            loader.setControllerFactory(context::getBean);
            Parent parent= loader.load();
            
            stage.setScene(new Scene(parent,800,600));
            stage.setTitle(applicationTitle);
            
            
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

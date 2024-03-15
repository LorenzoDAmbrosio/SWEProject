package com.project.sweprojectspring;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Configurable
public class StageHandler {


    @Value("classpath:/main/authentication/login-page.fxml")
    public Resource loginPageResource;

    @Value("classpath:/main/main-menu.fxml")
    public Resource mainMenuResource;

    public StageHandler(){
    }

    public void SwitchStage(Stage main,Resource resource){
        Parent parent=LoadResource(resource);
        main.getScene().setRoot(parent);
    }

    private Parent LoadResource(Resource resource){
        FXMLLoader loader= null;
        try {
            loader = new FXMLLoader(resource.getURL());
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
            context.register(DependencyInjectionConfig.class);
            context.refresh();
            loader.setControllerFactory(context::getBean);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

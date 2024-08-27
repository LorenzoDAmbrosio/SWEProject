package com.project.sweprojectspring.services;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
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

    @Value("classpath:/main/authentication/registration-page.fxml")
    public Resource registestrationPageResource;

    @Value("classpath:/main/profilo.fxml")
    public Resource profiloPageResource;

    @Value("classpath:/main/impostazioni.fxml")
    public Resource impostazioniPageResource;



    @Autowired
    private ApplicationContext context;

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
            loader.setControllerFactory(context::getBean);
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

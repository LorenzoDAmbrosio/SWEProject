package com.project.sweprojectspring;

import com.project.sweprojectspring.controllers.MainMenuController;
import com.project.sweprojectspring.controllers.authentication.LoginPageController;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class DependencyInjectionConfig {


    @Bean
    public StageHandler stageHandler() {
        return new StageHandler();
    }
    @Bean
    @Scope("prototype")
    public MainMenuController mainMenuController () {
        return new MainMenuController();
    }
    @Bean
    @Scope("prototype")
    public LoginPageController loginPageController () {
        return new LoginPageController();
    }
}

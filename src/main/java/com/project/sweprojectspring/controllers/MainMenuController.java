package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.StageHandler;
import com.project.sweprojectspring.StageInitializer;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

@Controller
public class MainMenuController {
    @Autowired
    private StageHandler stageHandler;

    @FXML
    private Button loginButton;
    @FXML
    private Label label;

    @FXML
    private void initialize() {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = ((Node)event.getSource()).getScene();
                Stage stage= (Stage) scene.getWindow();

                stageHandler.SwitchStage(stage,stageHandler.loginPageResource);
            }
        });
    }
}

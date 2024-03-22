package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
// permettono di interagire con le viste
@Controller
public class MainMenuController {
    @Autowired
    private StageHandler stageHandler;

    @FXML
    private Button logOutButton;
    @FXML
    private Label label;

    @FXML
    private void initialize() {
        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = ((Node)event.getSource()).getScene();
                Stage stage= (Stage) scene.getWindow();

                stageHandler.SwitchStage(stage,stageHandler.loginPageResource);
            }
        });
    }
}

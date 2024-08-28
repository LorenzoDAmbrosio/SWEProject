package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ProfiloController {
    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;


    @FXML
    private Button ImpostazioniButton;
    @FXML
    private Button RewButton;
    @FXML
    private Button MainButton;

    @FXML
    private void initialize() {
        ImpostazioniButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = ((Node)event.getSource()).getScene();
                Stage stage= (Stage) scene.getWindow();

                stageHandler.SwitchStage(stage,stageHandler.impostazioniPageResource);
            }
        });
        MainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Scene scene = ((Node)event.getSource()).getScene();
                Stage stage= (Stage) scene.getWindow();

                stageHandler.SwitchStage(stage,stageHandler.mainMenuResource);
            }
        });
    }
}
package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.components.container.FilmContainerComponent;
import com.project.sweprojectspring.daos.FilmDao;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

// permettono di interagire con le viste
@Controller
public class MainMenuController {
    @Autowired
    private AuthHandler authHandler;
    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private FilmDao filmDao;

    @FXML
    private Button logOutButton;
    @FXML
    private Label label;

    @FXML
    private FilmContainerComponent filmContainer;

    @FXML
    private void initialize() {
        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                boolean logoutResult=authHandler.Logout();

                if(logoutResult){
                    stageHandler.SwitchStageFromEvent(event,stageHandler.loginPageResource);
                }
            }
        });

        stageHandler.LoadComponent(stageHandler.filmContainerResource,filmContainer);
    }
}

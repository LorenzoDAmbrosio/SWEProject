package com.project.sweprojectspring.controllers.authentication;

import com.project.sweprojectspring.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

//il logout manda di nuovo alla pagina di login
@Controller
public class LogoutPageController {
    @Autowired
    private StageHandler stageHandler;

    @FXML
    private Button loginButton;

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

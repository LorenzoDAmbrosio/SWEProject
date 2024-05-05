package com.project.sweprojectspring.controllers.authentication;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.User;
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
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class LoginPageController {
    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;

    @FXML
    private Button mainMenuButton;
    @FXML
    private Button loginButton;
    @FXML
    private Label resultlabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    private void initialize() {
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = null;
                String password = null;

                username=usernameTextField.getText();
                password=passwordTextField.getText();

                Result<User> loginresult=authHandler.Login(username,password);

                if(loginresult.isFailed()){
                    resultlabel.setText(loginresult.ToError().getLocalizedMessage());
                    return;
                }
                resultlabel.setText("Hai effettuato l'accesso.");
            }
        });
    }
}
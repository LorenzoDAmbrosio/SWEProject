package com.project.sweprojectspring.controllers.authentication;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import com.project.sweprojectspring.models.billings.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class RegistrationPageController {
    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;


    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField repasswordTextField;
    @FXML
    private Button fattoButton;
    @FXML
    private Label registrationOutputLable;
    @FXML
    private RadioButton standardRadioButton;
    @FXML
    private RadioButton premiumRadioButton;


@FXML
    private void initialize() {
    ToggleGroup group = new ToggleGroup();
    standardRadioButton.setToggleGroup(group);
    standardRadioButton.setSelected(true);
    premiumRadioButton.setToggleGroup(group);

    fattoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = null;
                String password = null;
                String repassword =null;






                username=usernameTextField.getText();
                password=passwordTextField.getText();
                repassword=repasswordTextField.getText();

                Result<User> createresult=authHandler.Register(username, password, repassword);

                if(createresult.isFailed()){
                    registrationOutputLable.textFillProperty().setValue(new Color(1,0,0,1));
                    registrationOutputLable.setText(createresult.ToError().getLocalizedMessage());
                    return;
                }
                registrationOutputLable.textFillProperty().setValue(new Color(0,1,0,1));
                registrationOutputLable.setText("Ti sei registrato");

                if(standardRadioButton.isSelected()) {
                    authHandler.RegisterStandardSubscription();
                } else {
                    authHandler.RegisterPremiumSubscription();
                }
                authHandler.Login(username,password);


                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource );
            }
        });

    }

}

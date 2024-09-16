package com.project.sweprojectspring.controllers.settings;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CambioPasswordController {
    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;


    @FXML
    private Button CambiaButton;
    @FXML
    private Label CambiaPassLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private TextField newPasswordTextField;
    @FXML
    private Label changeOutputLabel;
    @FXML
    private Button MainButton;

    @FXML
    private void initialize() {
        //l'idea è di controllare se chi sta facendo l'accesso è il vero utente (controllo utente password)
        //se è lui vado a aggiornare il campo password
        CambiaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = null;
                String password = null;
                String newpassword = null;


                username=usernameTextField.getText();
                password=passwordTextField.getText();
                newpassword=newPasswordTextField.getText();


                //cambiare da login a controllo
                Result<User> loginresult=authHandler.Change(username,password,newpassword);

                if(loginresult.isFailed()){
                    changeOutputLabel.textFillProperty().setValue(new Color(1,0,0,1));
                    changeOutputLabel.setText(loginresult.ToError().getLocalizedMessage());
                    return;
                }

                changeOutputLabel.textFillProperty().setValue(new Color(0,1,0,1));
                changeOutputLabel.setText("Password cambiata.");


                stageHandler.SwitchStageFromEvent(event,stageHandler.profiloPageResource );
            }
        });
        MainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource);
            }
        });
    }




}

package com.project.sweprojectspring.controllers.settings;

import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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
    private Button BillingsButton;
    @FXML
    private AnchorPane adminStuff;
    @FXML
    private Button whishlistButton;
    @FXML
    private Button myreviewButton;


    @FXML
    private void visibilityRewButton(){
        if (authHandler.IsUserReviewer()){
            RewButton.setVisible(false);
            myreviewButton.setVisible(true);
       }else {
            RewButton.setVisible(true);
            myreviewButton.setVisible(false);
        }
    }

    @FXML
    private void visibilityAdminStuff(){
        if (authHandler.IsUserSubscribed() || authHandler.IsUserReviewer()){
            adminStuff.setVisible(false);
        }else adminStuff.setVisible(false);
    }

    @FXML
    private void initialize() {
        //top bar
        ImpostazioniButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageHandler.SwitchStageFromEvent(event,stageHandler.cambiopasswordPageResource);
            }
        });
        MainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource);
            }
        });



//----------------------------------------------------------------------------------------------------------------------
        //side bar
        BillingsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.billingsResource);
            }
        });

        whishlistButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.whishlistPageResource);
            }
        });
        myreviewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.myreviewPageResource);
            }
        });

//----------------------------------------------------------------------------------------------------------------------
        //central bar

        visibilityRewButton();
        RewButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                authHandler.DiventaRew();
            }
        });
        visibilityAdminStuff();
    }
}
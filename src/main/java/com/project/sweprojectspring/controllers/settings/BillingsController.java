package com.project.sweprojectspring.controllers.settings;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
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
public class BillingsController {
    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;



    @FXML
    private Button MainButton;
    @FXML
    private Button goreviewButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button RewButton;
    @FXML
    private Button cambiaButton;
    //<Button fx:id="cambiaButton" mnemonicParsing="false" text="Cambia abbonamento"  textFill="WHITE" style="-fx-background-color: #2A003C;"  />
    //non credo serva
    @FXML
    private Label Rewlable;
    @FXML
    private Label Rewalable;


    @FXML
    private void visibilityRew(){
        if (authHandler.IsUserReviewer()){
            RewButton.setVisible(false);
            Rewalable.setVisible(true);
            Rewlable.setVisible(false);
            goreviewButton.setVisible(true);
        }else {
            RewButton.setVisible(true);
            Rewalable.setVisible(false);
            Rewlable.setVisible(true);
            goreviewButton.setVisible(false);
        }
    }



    @FXML
    private void initialize() {

        MainButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource);
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.profiloPageResource);
            }
        });


        visibilityRew();
        RewButton.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                authHandler.DiventaRew();

                authHandler.getLoggedUser();
                stageHandler.SwitchStageFromEvent(event,stageHandler.profiloPageResource);
                visibilityRew();
            }
        });

//        goreviewButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                stageHandler.SwitchStageFromEvent(event,stageHandler.whishlistPageResource);
//            }
//        });







    }



}

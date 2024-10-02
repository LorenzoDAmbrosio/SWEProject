package com.project.sweprojectspring.controllers.settings;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.billings.StandardSub;
import com.project.sweprojectspring.models.billings.Subscription;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label premiumLabel;
    @FXML
    private Label standardLabel;


    @FXML
    private void visibilityRew(){
        if (authHandler.IsUserReviewer()){
            RewButton.setVisible(false);
            Rewlable.setVisible(false);
            Rewalable.setVisible(true);
            goreviewButton.setVisible(true);
        }else {
            RewButton.setVisible(true);
            Rewlable.setVisible(true);
            Rewalable.setVisible(false);
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

        Result<Subscription> sub=authHandler.getCurrentSubscription();
        if(sub.isSuccessful() && sub.toValue() instanceof StandardSub){
            standardLabel.setVisible(true);
            premiumLabel.setVisible(false);
        }else{
            standardLabel.setVisible(false);
            premiumLabel.setVisible(true);
        }

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

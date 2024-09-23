package com.project.sweprojectspring.controllers.settings;

import com.project.sweprojectspring.controllers.ReviewFormController;
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
    private Button qButton;

    @FXML
    private void visibilityRew(){
        if (authHandler.IsUserReviewer()){
            myreviewButton.setVisible(true);
       }else {
            myreviewButton.setVisible(false);
        }
    }

    @FXML
    private void visibilityAdminStuff(){
        if (authHandler.IsUserSubscribed()){
            adminStuff.setVisible(false);
        }else adminStuff.setVisible(true);
    }

    @FXML
    private void initialize() {
        visibilityRew();
        visibilityAdminStuff();
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
                stageHandler.SwitchStageFromEvent(event,stageHandler.wishlistFormResource);
            }
        });
        myreviewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.reviewFormResource,
                        (ReviewFormController c) ->{c.setSelectedFilm(null);return c;});}
        });

//        qButton.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//
//            }
//        });

//----------------------------------------------------------------------------------------------------------------------

    }
}
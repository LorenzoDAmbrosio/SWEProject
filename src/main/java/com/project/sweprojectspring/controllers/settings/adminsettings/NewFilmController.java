package com.project.sweprojectspring.controllers.settings.adminsettings;


import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;


@Controller
public class NewFilmController {

    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;

    @FXML
    private Button MainButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button fattoButton;
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField dateField;


    @FXML
    private void initialize(){

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

        fattoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String title = null;
                String author = null;
                String description =null;
                String input = null;

                input = dateField.getText();
                title = titleField.getText();
                author =authorField.getText();
                description=descriptionField.getText();
               int date = Integer.parseInt(input);

               authHandler.RegisterFilm(title,author,description,date);
               stageHandler.SwitchStageFromEvent(event,stageHandler.gestisciFilmResource);
            }
        });

    }
}

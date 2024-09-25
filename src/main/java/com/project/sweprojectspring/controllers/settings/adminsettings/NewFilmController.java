package com.project.sweprojectspring.controllers.settings.adminsettings;


import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Review;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.WeakEventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import com.project.sweprojectspring.daos.FilmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;


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

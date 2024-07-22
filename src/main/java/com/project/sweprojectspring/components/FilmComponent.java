package com.project.sweprojectspring.components;

import com.project.sweprojectspring.controllers.FilmDetailController;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.services.StageHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FilmComponent extends Pane {
    @FXML private Label filmTitle;
    @FXML private Label filmAuthor;
    @FXML private Label filmYear;
    @FXML private Label filmDescription;
    @FXML private Button detailButton;

    @Autowired
    private StageHandler stageHandler;


    @Setter
    private Film film;

    @FXML
    private void initialize() {
        if(film==null)
            return;

        filmTitle.setText(film.getTitle());
        filmAuthor.setText(film.getAuthor());
        filmYear.setText(String.valueOf(film.getReleaseDate()));
        filmDescription.setText(film.getDescription());
        detailButton.setText("Dettaglio");

        detailButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                stageHandler.SwitchStageFromEvent(
                event,
                stageHandler.filmDetailResource,
                (FilmDetailController controller)->{
                    controller.selectedFilm=film;
                    return controller;
                });
            }
        });
    }
}

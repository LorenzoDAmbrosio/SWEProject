package com.project.sweprojectspring.controllers.settings.adminsettings;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.project.sweprojectspring.daos.FilmDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GestisciFilmController {

    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private AuthHandler authHandler;
    @Autowired
    private FilmDao filmDao;


    @FXML private TableView<Film> filmTableView;

    @FXML
    private Button MainButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button newfilmButton;

    @FXML
    private void initialize(){
        createFilmTable();
        setupFilmTable();

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

        newfilmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.newFilmResource);
            }
        });

    }
    private void setupFilmTable() {
        filmTableView.setItems(FXCollections.observableArrayList());
        Result<List<Film>> filmResult= filmDao.retrieveAll();
        ObservableList<Film> filmRows = FXCollections.observableArrayList();
        List<Film> films=filmResult.toValue();
        films.removeIf(wishlist -> {
            if(!authHandler.IsUserLogged())
                return true;
            return false;
        });
        if(!films.isEmpty()) {
            filmRows.addAll(films);
        }
        filmTableView.setItems(filmRows);
    }

    private void createFilmTable() {
        filmTableView.getColumns().clear();

        // Create the "Nome" column
        TableColumn<Film, String> nameColumn = new TableColumn<>("Titolo");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameColumn.setPrefWidth(130);
        filmTableView.getColumns().add(nameColumn);

        // Create the "Descrizione" column
        TableColumn<Film, String> descriptionColumn = new TableColumn<>("Author");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        descriptionColumn.setPrefWidth(150);
        filmTableView.getColumns().add(descriptionColumn);

        // Create the "Descrizione" column
        TableColumn<Film, Integer> dateColumn= new TableColumn<>("Anno");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        dateColumn.setPrefWidth(110);
        filmTableView.getColumns().add(dateColumn);
    }


}

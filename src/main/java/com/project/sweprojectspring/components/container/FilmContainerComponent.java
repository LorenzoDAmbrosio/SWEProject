package com.project.sweprojectspring.components.container;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.components.FilmComponent;
import com.project.sweprojectspring.daos.FilmDao;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class FilmContainerComponent extends AnchorPane {

    @Autowired
    private AuthHandler authHandler;
    @Autowired
    private FilmDao filmDao;
    @Autowired
    private StageHandler stageHandler;

    @FXML
    private GridPane grid;

    @FXML
    private void initialize() {
        if (!authHandler.IsUserLogged()) {
            return;
        }

        Result<List<Film>> filmsResult = filmDao.retrieveAll();

        if (filmsResult.isFailed())
            return;

        grid.setHgap(10);
        grid.setVgap(10);

        int columns = grid.getColumnCount();
        int i = 0;
        for (Film film : filmsResult.ToValue()) {
            int row = i / columns;
            int col = i % columns;
            FilmComponent filmComponent=new FilmComponent();

            filmComponent=stageHandler.LoadComponent(stageHandler.filmResource,filmComponent,
            (_filmComponent)->{
                _filmComponent.setFilm(film);
                return _filmComponent;
            });

            grid.add(filmComponent, col, row);
            i++;
        }
    }

}

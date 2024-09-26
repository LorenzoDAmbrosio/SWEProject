package com.project.sweprojectspring.controllers.settings.adminsettings;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.daos.authentications.SubscribedUserDao;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.authentications.User;
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
public class GestisciUtentiController {
    @Autowired
    AuthHandler authHandler;
    @Autowired
    StageHandler stageHandler;
    @Autowired
    SubscribedUserDao subscribedUserDao;
    @Autowired
    UserDao userDao;

    @FXML
    private TableView<User> UserTableView;

    @FXML
    private Button MainButton;
    @FXML
    private Button exitButton;
    @FXML
    private Button newUserButton;

    @FXML
    private void initialize(){
        createUserTable();
        setupUserTable();


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

        newUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.newFilmResource);
            }
        });

    }
    private void setupUserTable() {
        UserTableView.setItems(FXCollections.observableArrayList());
        Result<List<User>> userResult= userDao.retrieveAll();
        ObservableList<User> usersRows = FXCollections.observableArrayList();
        List<User> users=userResult.toValue();
        if(!users.isEmpty()) {
            usersRows.addAll(users);
        }
//        UserTableView.setItems(usersRows);
        UserTableView.setItems(usersRows);
    }

    private void createUserTable() {
        UserTableView.getColumns().clear();

        // Create the "Id" column
        TableColumn<User, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        idColumn.setPrefWidth(50);
        UserTableView.getColumns().add(idColumn);
// Create the "Data" column
        TableColumn<User, String> usernameColumn= new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        usernameColumn.setPrefWidth(110);
        UserTableView.getColumns().add(usernameColumn);
//
    }


}

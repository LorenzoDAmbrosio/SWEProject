package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.resources.ReviewDao;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Review;
import com.project.sweprojectspring.models.resources.Wishlist;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import com.sun.javafx.menu.MenuItemBase;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WishlistFormController {

    @Setter
    @Getter
    public Wishlist selectedWishlist;

    @FXML
    private Label listNameLabel;
    @FXML private TextField listNameTextField;
    @FXML private TextArea listDescriptionTextField;
    @FXML private Button createEditButton;
    @FXML private Button editWishlistsButton;

    @FXML private Button exitButton;

    @FXML private TableView<Wishlist> wishlistTableView;

    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private WishlistDao wishlistDao;
    @Autowired
    private AuthHandler authHandler;

    private boolean IsWishlistSelected=false;

    @FXML
    public void initialize() {

        Result<List<Wishlist>> wishlistResult= wishlistDao.retrieveAll();
        ObservableList<Wishlist> wishlistRows = FXCollections.observableArrayList();
        List<Wishlist> wishlists=wishlistResult.ToValue();
        wishlists.removeIf(wishlist -> {
            if(!authHandler.IsUserLogged()) return true;
            return !wishlist.getSubscribedUser().equals(authHandler.getLoggedUser());
        });
        if(!wishlists.isEmpty()) {
            wishlistRows.addAll(wishlists);
        }
        createWishlistTable(wishlistRows);

        wishlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Add a listener to handle row selection
        wishlistTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedWishlist=newSelection;
            if(selectedWishlist == null){
                listNameLabel.setText("Nuova Wishlist");
            }
            else{
                listNameLabel.setText(selectedWishlist.getName());
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource);
            }
        });
        editWishlistsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedWishlist == null){
                    return;
                }
                if(IsWishlistSelected){
                    selectedWishlist=null;
                    IsWishlistSelected=false;
                    editWishlistsButton.setText("seleziona");
                    return;
                }
                listNameTextField.setText(selectedWishlist.getName());
                listDescriptionTextField.setText(selectedWishlist.getDescription());
                editWishlistsButton.setText("deseleziona");
                IsWishlistSelected=true;
            }
        });

    }


    private void createWishlistTable(ObservableList<Wishlist> wishlistRows) {

        // Create the "Nome" column
        TableColumn<Wishlist, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(100);
        wishlistTableView.getColumns().add(nameColumn);

        // Create the "Descrizione" column
        TableColumn<Wishlist, String> descriptionColumn = new TableColumn<>("Descrizione");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("descrizione"));
        descriptionColumn.setPrefWidth(100);
        wishlistTableView.getColumns().add(descriptionColumn);

        // Create the "N° film" column
        TableColumn<Wishlist, Integer> numFilmsColumn = new TableColumn<>("N° film");
        numFilmsColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getNumberOfFilms());
        });
        numFilmsColumn.setPrefWidth(72);
        wishlistTableView.getColumns().add(numFilmsColumn);
        wishlistTableView.setItems(wishlistRows);
    }
}

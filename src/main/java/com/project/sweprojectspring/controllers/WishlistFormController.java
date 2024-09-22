package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.FilmDao;
import com.project.sweprojectspring.daos.actions.AddToWishlistActionDao;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.models.actions.AddToWishlistAction;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Wishlist;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class WishlistFormController {

    @Setter
    @Getter
    public Wishlist selectedWishlist;

    @FXML private Label listNameLabel;
    @FXML private Label wishlistOutputLabel;
    @FXML private TextField listNameTextField;
    @FXML private TextArea listDescriptionTextField;

    @FXML private Button wishlistCreateButton;
    @FXML private Button wishlistEditButton;
    @FXML private Button wishlistDeleteButton;
    @FXML private Button wishlistSelectButton;

    @FXML private Button exitButton;

    @FXML private TableView<Wishlist> wishlistTableView;

    @FXML private TableView<Film> filmTableView;

    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private WishlistDao wishlistDao;
    @Autowired
    private AddToWishlistActionDao addToWishlistActionDao;
    @Autowired
    private FilmDao filmDao;
    @Autowired
    private AuthHandler authHandler;

    private boolean IsWishlistSelected=false;

    @FXML
    public void initialize() {

        createFilmTable();
        setupFilmTable();

        createWishlistTable();
        setupWishlistTable();

        wishlistTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Add a listener to handle row selection
        wishlistTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectedWishlist=newSelection;
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource);
            }
        });
        wishlistSelectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedWishlist == null)
                    return;
                if(IsWishlistSelected){
                    WishlistDeselection();
                    IsWishlistSelected=false;
                }else{
                    WishlistSelection();
                    IsWishlistSelected=true;
                }
                createFilmTable();
                setupFilmTable();
            }

        });
        wishlistCreateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedWishlist != null)
                    return;
                Wishlist newWishlist=new Wishlist();
                newWishlist.setName(listNameTextField.getText());
                newWishlist.setDescription(listDescriptionTextField.getText());
                newWishlist.setSubscribedUser((SubscribedUser) authHandler.getLoggedUser());
                Result<Wishlist> result= wishlistDao.create(newWishlist);

                wishlistOutputLabel.setText(result.toString());
                if(result.isFailed())
                    return;

                setupWishlistTable();
                wishlistTableView.getSelectionModel().select(result.toValue());
                WishlistSelection();
                createFilmTable();
                setupFilmTable();
                IsWishlistSelected=true;
            }
        });

        wishlistEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedWishlist == null)
                    return;
                Wishlist editWishlist=selectedWishlist;
                editWishlist.setName(listNameTextField.getText());
                editWishlist.setDescription(listDescriptionTextField.getText());

                Set<Film> films=selectedWishlist.getFilms();

                Result<Wishlist> result= wishlistDao.update(editWishlist);
                wishlistOutputLabel.setText(result.toString());
                if(result.isFailed()) {
                    return;
                }
                List<AddToWishlistAction> currentCallToActions= new ArrayList<>();
                for(Film film : films){
                    AddToWishlistAction callToAction=new AddToWishlistAction();
                    callToAction.setFilm(film);
                    callToAction.setSubscribedUser((SubscribedUser) authHandler.getLoggedUser());
                    currentCallToActions.add(callToAction);
                }
                addToWishlistActionDao.Sync(currentCallToActions,
                        old->{
                            if(!old.getSubscribedUser().equals(authHandler.getLoggedUser()))
                                return true;
                            return false;
                        });

                createWishlistTable();
                setupWishlistTable();
                wishlistTableView.getSelectionModel().select(result.toValue());
                WishlistSelection();
                createFilmTable();
                setupFilmTable();
            }
        });
        wishlistDeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedWishlist == null)
                    return;
                Wishlist editWishlist=selectedWishlist;

                Result<Wishlist> result= wishlistDao.delete(editWishlist);

                wishlistOutputLabel.setText(result.toString());

                WishlistDeselection();
                IsWishlistSelected=false;
                createFilmTable();
                setupFilmTable();
                setupWishlistTable();
            }
        });


    }

    private void WishlistSelection() {
        listNameLabel.setText("Modifica di "+selectedWishlist.getName());
        listNameTextField.setText(selectedWishlist.getName());
        listDescriptionTextField.setText(selectedWishlist.getDescription());
        wishlistSelectButton.setText("deseleziona");
        wishlistCreateButton.setDisable(true);
        wishlistDeleteButton.setDisable(false);
        wishlistEditButton.setDisable(false);
    }

    private void WishlistDeselection() {
        selectedWishlist=null;
        wishlistSelectButton.setText("seleziona");
        listNameTextField.setText("");
        listDescriptionTextField.setText("");
        wishlistCreateButton.setDisable(false);
        wishlistDeleteButton.setDisable(true);
        wishlistEditButton.setDisable(true);
        listNameLabel.setText("Nuova Wishlist");
        wishlistTableView.getSelectionModel().clearSelection();
    }

    private void setupWishlistTable() {
        wishlistTableView.setItems(FXCollections.observableArrayList());
        Result<List<Wishlist>> wishlistResult= wishlistDao.retrieveAll();
        ObservableList<Wishlist> wishlistRows = FXCollections.observableArrayList();
        List<Wishlist> wishlists=wishlistResult.toValue();
        wishlists.removeIf(wishlist -> {
            if(!authHandler.IsUserLogged()) return true;
            return !wishlist.getSubscribedUser().equals(authHandler.getLoggedUser());
        });
        if(!wishlists.isEmpty()) {
            wishlistRows.addAll(wishlists);
        }
        wishlistTableView.setItems(wishlistRows);
    }

    private void createWishlistTable() {

        wishlistTableView.getColumns().clear();
        // Create the "Nome" column
        TableColumn<Wishlist, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(100);
        wishlistTableView.getColumns().add(nameColumn);

        // Create the "Descrizione" column
        TableColumn<Wishlist, String> descriptionColumn = new TableColumn<>("Descrizione");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(100);
        wishlistTableView.getColumns().add(descriptionColumn);

        // Create the "N° film" column
        TableColumn<Wishlist, Integer> numFilmsColumn = new TableColumn<>("N° film");
        numFilmsColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getNumberOfFilms());
        });
        numFilmsColumn.setPrefWidth(72);
        wishlistTableView.getColumns().add(numFilmsColumn);
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
        TableColumn<Film, Boolean> actionColumn = new TableColumn<>("Assign");

        Callback<TableColumn<Film, Boolean>, TableCell<Film, Boolean>> cellFactory =
                new Callback<TableColumn<Film, Boolean>, TableCell<Film, Boolean>>() {
                    @Override
                    public TableCell call(final TableColumn<Film, Boolean> param) {
                        final TableCell<Film, Boolean> cell = new TableCell<Film, Boolean>() {

                            final Button btn = new Button("☐");
                            @Override
                            public void updateItem(Boolean item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    Film film = getTableView().getItems().get(getIndex());
                                    String btnText = film.isInWishlist(selectedWishlist) ? "☑":"☐";
                                    btn.setText(btnText);

                                    if(selectedWishlist==null || selectedWishlist.getFilms() == null){
                                        btn.setDisable(true);
                                    }
                                    btn.setOnAction(event -> {
                                        Boolean isFilmInWishlist=film.isInWishlist(selectedWishlist);
                                        if(isFilmInWishlist){
                                            selectedWishlist.getFilms().remove(film);
                                        }
                                        else{
                                            selectedWishlist.getFilms().add(film);
                                        }
                                        // ! perché è stato effettuato un toggle
                                        String updateText = !isFilmInWishlist ? "☑":"☐";
                                        btn.setText(updateText);
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionColumn.setStyle( "-fx-alignment: CENTER;");
        actionColumn.setCellFactory(cellFactory);
        actionColumn.setPrefWidth(50);
        filmTableView.getColumns().add(actionColumn);
        // Create the "Nome" column
        TableColumn<Film, String> nameColumn = new TableColumn<>("Titolo");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameColumn.setPrefWidth(130);
        filmTableView.getColumns().add(nameColumn);

        // Create the "Descrizione" column
        TableColumn<Film, String> descriptionColumn = new TableColumn<>("Author");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        descriptionColumn.setPrefWidth(100);
        filmTableView.getColumns().add(descriptionColumn);

        // Create the "Descrizione" column
        TableColumn<Film, Integer> dateColumn= new TableColumn<>("Anno");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        dateColumn.setPrefWidth(70);
        filmTableView.getColumns().add(dateColumn);

    }

}

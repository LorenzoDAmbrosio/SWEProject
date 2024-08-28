package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.daos.resources.ReviewDao;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Review;
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
import javafx.scene.text.Font;
import javafx.util.Callback;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;

@Controller
public class FilmDetailController {

    @Setter
    @Getter
    public Film selectedFilm;

    @FXML
    private Label filmTitle;
    @FXML private Label filmAuthor;
    @FXML private Label filmYear;
    @FXML private Label filmDescription;

    @FXML private Button exitButton;
    @FXML private Button likeButton;
    @FXML private Button editWishlistsButton;
    @FXML private Button addReviewButton;


    @FXML private TableView<Wishlist> wishlistTableView;
    @FXML private TableView<Review> filmReviewsTable;

    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private WishlistDao wishlistDao;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private AuthHandler authHandler;


    private boolean IsFilmLiked=false;

    @FXML
    public void initialize() {

        filmTitle.setText(selectedFilm.getTitle());
        filmAuthor.setText(selectedFilm.getAuthor());
        filmYear.setText(String.valueOf(selectedFilm.getReleaseDate()));
        filmDescription.setText(selectedFilm.getDescription());

        addReviewButton.setVisible(authHandler.IsUserReviewer());
        editWishlistsButton.setVisible(authHandler.IsUserSubscribed());

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

        Result<List<Review>> reviewsResult= reviewDao.retrieveAll();
        ObservableList<Review> reviewsRows = FXCollections.observableArrayList();
        List<Review> reviews=reviewsResult.ToValue();
        reviews.removeIf(review -> {
            return !review.getFilm().equals(selectedFilm);
        });
        if(!reviews.isEmpty()) {
            reviewsRows.addAll(reviews);
        }
        createReviewsTable(reviewsRows);

        likeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IsFilmLiked = !IsFilmLiked;
                String likeIcon= IsFilmLiked ? "♥" :"♡";
                int textSize= IsFilmLiked ? 30 : 24;
                Font likeButtonFont=new Font(textSize);
                likeButton.setFont(likeButtonFont);
                likeButton.setText(likeIcon);
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
                stageHandler.SwitchStageFromEvent(event,stageHandler.wishlistFormResource);
            }
        });
    }

    private void createReviewsTable(ObservableList<Review> reviews) {

        TableColumn<Review, String> nameColumn = new TableColumn<>("Revisore");
        nameColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getReviewer().getUsername());
        });
        nameColumn.setPrefWidth(115);
        filmReviewsTable.getColumns().add(nameColumn);

        TableColumn<Review, String> numFilmsColumn = new TableColumn<>("Recensione");
        numFilmsColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        numFilmsColumn.setPrefWidth(175);
        filmReviewsTable.getColumns().add(numFilmsColumn);

        TableColumn<Review, Date> publishDateColumn = new TableColumn<>("Data");
        publishDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishDate"));
        publishDateColumn.setPrefWidth(100);
        filmReviewsTable.getColumns().add(publishDateColumn);

        filmReviewsTable.setItems(reviews);
    }

    private void createWishlistTable(ObservableList<Wishlist> wishlistRows) {


        TableColumn<Wishlist, Boolean> actionColumn = new TableColumn<>("Aggiungi");

        Callback<TableColumn<Wishlist, Boolean>, TableCell<Wishlist, Boolean>> cellFactory =
                        new Callback<TableColumn<Wishlist, Boolean>, TableCell<Wishlist, Boolean>>() {
                            @Override
                            public TableCell call(final TableColumn<Wishlist, Boolean> param) {
                                final TableCell<Wishlist, Boolean> cell = new TableCell<Wishlist, Boolean>() {

                                    final Button btn = new Button("+");
                                    @Override
                                    public void updateItem(Boolean item, boolean empty) {
                                        super.updateItem(item, empty);

                                        if (empty) {
                                            setGraphic(null);
                                            setText(null);
                                        } else {
                                            Wishlist wishlist = getTableView().getItems().get(getIndex());
                                            String btnText = wishlist.containsFilm(selectedFilm) ? "-":"+";
                                            btn.setText(btnText);

                                            btn.setOnAction(event -> {
                                                Boolean isFilmInWishlist=wishlist.containsFilm(selectedFilm);
                                                if(isFilmInWishlist){
                                                    wishlist.getFilms().remove(selectedFilm);
                                                }
                                                else{
                                                    wishlist.getFilms().add(selectedFilm);
                                                }
                                                wishlistDao.update(wishlist);
                                                String updateText = isFilmInWishlist ? "-":"+";
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
        actionColumn.setCellFactory(cellFactory);
        actionColumn.setPrefWidth(70);
        wishlistTableView.getColumns().add(actionColumn);

        // Create the "Nome" column
        TableColumn<Wishlist, String> nameColumn = new TableColumn<>("Nome");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(225);
        wishlistTableView.getColumns().add(nameColumn);

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

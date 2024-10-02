package com.project.sweprojectspring.controllers;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.resources.FilmDao;
import com.project.sweprojectspring.daos.resources.ReviewDao;
import com.project.sweprojectspring.models.authentications.Reviewer;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Review;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReviewFormController {

    @Autowired
    private AuthHandler authHandler;
    @Getter
    @Setter
    private  Film SelectedFilm = null;
    @Setter
    @Getter
    public Review selectedReview;

    @FXML private Label filmTitle;
    @FXML private Label reviewNameLabel;
    @FXML private Label reviewOutputLabel;
    @FXML private TextArea reviewDescriptionTextField;

    @FXML private Button reviewCreateButton;
    @FXML private Button reviewEditButton;
    @FXML private Button reviewDeleteButton;
    @FXML private Button reviewSelectButton;

    @FXML private Button exitButton;

    @FXML private TableView<Review> reviewTableView;

    @FXML private TableView<Film> filmTableView;

    @Autowired
    private StageHandler stageHandler;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private FilmDao filmDao;

    private boolean IsReviewSelected=false;
    private boolean IsReviewInCreation=false;

    @FXML
    public void initialize() {

        createFilmTable();
        setupFilmTable();

        createReviewTable();
        setupReviewTable();

        if(SelectedFilm != null){
            filmTitle.setText("Recensioni di "+SelectedFilm.getTitle());
            filmTableView.setVisible(false);
        }

        if(selectedReview==null) {
            reviewDescriptionTextField.setDisable(true);
            reviewCreateButton.setText("Crea");
        }
        reviewTableView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        // Add a listener to handle row selection
        reviewTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if(selectedReview == null)
                selectedReview = newSelection;
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                stageHandler.SwitchStageFromEvent(event,stageHandler.mainMenuResource);
            }
        });
        reviewSelectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedReview == null)
                    return;
                if(IsReviewSelected){
                    ReviewDeselection();
                }else{
                    ReviewSelection();
                }
                createFilmTable();
                setupFilmTable();
            }

        });

        reviewCreateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!IsReviewInCreation && !IsReviewSelected){
                    selectedReview=new Review();
                    selectedReview.setFilm(SelectedFilm);

                    reviewCreateButton.setText("Salva");
                    reviewDescriptionTextField.setDisable(false);
                    reviewDescriptionTextField.setText(selectedReview.getDescription());
                    reviewSelectButton.setText("deseleziona");
                    createFilmTable();
                    setupFilmTable();
                    IsReviewInCreation=true;
                    return;
                }
                Review newReview=new Review();
                newReview.setDescription(reviewDescriptionTextField.getText());
                newReview.setReviewer((Reviewer) authHandler.getLoggedUser());
                newReview.setFilm(selectedReview.getFilm());
                Result<Review> result= reviewDao.create(newReview);

                reviewOutputLabel.setText(result.toString());

                if(result.isSuccessful()){
                    selectedReview=result.toValue();
                    setupReviewTable();
                    reviewTableView.getSelectionModel().select(selectedReview);

                    ReviewSelection();

                    IsReviewInCreation=false;
                    reviewCreateButton.setText("Crea");
                }
                createFilmTable();
                setupFilmTable();

            }
        });

        reviewEditButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedReview == null)
                    return;
                Review editReview= selectedReview;
                editReview.setDescription(reviewDescriptionTextField.getText());
                editReview.setFilm(selectedReview.getFilm());

                Result<Review> result= reviewDao.update(editReview);

                reviewOutputLabel.setText(result.toString());

                createReviewTable();
                setupReviewTable();
                reviewTableView.getSelectionModel().select(result.toValue());
                ReviewSelection();
                createFilmTable();
                setupFilmTable();
            }
        });
        reviewDeleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedReview == null)
                    return;
                Review editReview= new Review();
                editReview.setId(selectedReview.getId());
                Result<Review> result= reviewDao.delete(editReview);

                reviewOutputLabel.setText(result.toString());

                if(result.isSuccessful()){
                    reviewDescriptionTextField.setDisable(true);
                    reviewCreateButton.setText("Crea");
                    ReviewDeselection();
                }

                createFilmTable();
                setupFilmTable();
                setupReviewTable();
            }
        });


    }

    private void ReviewSelection() {
        String text="";
        if(selectedReview !=null && selectedReview.getPublishDate() != null) {
            Date publishDate = selectedReview.getPublishDate();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            text = formatter.format(publishDate);
        }

        reviewNameLabel.setText("Modifica di "+selectedReview.getFilm().getTitle()+" del "+text);
        reviewDescriptionTextField.setText(selectedReview.getDescription());
        reviewSelectButton.setText("deseleziona");
        reviewCreateButton.setDisable(true);
        reviewDeleteButton.setDisable(false);
        reviewEditButton.setDisable(false);
        reviewDescriptionTextField.setDisable(false);
        IsReviewSelected=true;
    }

    private void ReviewDeselection() {
        selectedReview =null;
        reviewSelectButton.setText("seleziona");
        reviewDescriptionTextField.setText("");
        reviewDescriptionTextField.setDisable(true);
        reviewCreateButton.setDisable(false);
        reviewDeleteButton.setDisable(true);
        reviewEditButton.setDisable(true);
        reviewNameLabel.setText("Nuova Review");
        reviewTableView.getSelectionModel().clearSelection();
        IsReviewSelected=false;
    }

    private void setupReviewTable() {
        reviewTableView.setItems(FXCollections.observableArrayList());
        Result<List<Review>> reviewResult= reviewDao.retrieveAll();
        ObservableList<Review> reviewRows = FXCollections.observableArrayList();
        List<Review> reviews=reviewResult.toValue();
        reviews.removeIf(review -> { //scorre sulla lista e le rimuove SE
            if(!authHandler.IsUserLogged()) return true;
            if(!review.getReviewer().equals((Reviewer) authHandler.getLoggedUser())){
               return true;
            }
            if(SelectedFilm != null && !review.getFilm().equals(SelectedFilm))
                return true;
            return false;
        });
        if(!reviews.isEmpty()) {
            reviewRows.addAll(reviews);
        }
        reviewTableView.setItems(reviewRows);
    }

    private void createReviewTable() {
        reviewTableView.getColumns().clear();

        TableColumn<Review, String> releaseDateColumn = new TableColumn<>("Data");
        releaseDateColumn.setCellValueFactory(cellData -> {
            Date publishDate = cellData.getValue().getPublishDate();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            String coltext="";
            if(publishDate != null)
                coltext=formatter.format(publishDate);

            return new ReadOnlyObjectWrapper<>(coltext);
        });
        releaseDateColumn.setPrefWidth(100);
        reviewTableView.getColumns().add(releaseDateColumn);

        TableColumn<Review, String> filmColumn = new TableColumn<>("Film");
        filmColumn.setCellValueFactory(cellData -> {
            return new ReadOnlyObjectWrapper<>(cellData.getValue().getFilm().getTitle());
        });
        filmColumn.setPrefWidth(100);
        reviewTableView.getColumns().add(filmColumn);

        // Create the "Descrizione" column
        TableColumn<Review, String> descriptionColumn = new TableColumn<>("Descrizione");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        descriptionColumn.setPrefWidth(100);
        reviewTableView.getColumns().add(descriptionColumn);
    }

    private void setupFilmTable() {
        if(SelectedFilm != null)
            return;

        filmTableView.setItems(FXCollections.observableArrayList());
        Result<List<Film>> filmResult= filmDao.retrieveAll();
        ObservableList<Film> filmRows = FXCollections.observableArrayList();
        List<Film> films=filmResult.toValue();
        films.removeIf(film -> {
            if(!authHandler.IsUserLogged())
                return true;
            if(SelectedFilm != null && !film.equals(SelectedFilm))
                return true;
            return false;
        });
        if(!films.isEmpty()) {
            filmRows.addAll(films);
        }
        filmTableView.setItems(filmRows);
    }

    private void createFilmTable() {
        if(SelectedFilm != null)
            return;
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
                                    String btnText ="☐";

                                    if(selectedReview != null && selectedReview.getFilm() != null){
                                        btnText = selectedReview.getFilm().equals(film) ? "☑":"☐";
                                    }else if(selectedReview == null){
                                        btn.setDisable(true);
                                    }
                                    btn.setText(btnText);
                                    btn.setOnAction(event -> {
                                        Boolean isFilmInReview=false;
                                        if(selectedReview != null){
                                            isFilmInReview= selectedReview.getFilm() != null
                                                && selectedReview.getFilm().equals(film);

                                            if(isFilmInReview){
                                                selectedReview.setFilm(null);
                                            }
                                            else{
                                                selectedReview.setFilm(film);
                                            }
                                        }

                                        // ! perché è stato effettuato un toggle
                                        String updateText = !isFilmInReview ? "☑":"☐";
                                        btn.setText(updateText);
                                        createFilmTable();
                                        setupFilmTable();
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

        TableColumn<Film, String> nameColumn = new TableColumn<>("Titolo");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        nameColumn.setPrefWidth(130);
        filmTableView.getColumns().add(nameColumn);

        TableColumn<Film, String> descriptionColumn = new TableColumn<>("Author");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        descriptionColumn.setPrefWidth(100);
        filmTableView.getColumns().add(descriptionColumn);

        TableColumn<Film, Integer> dateColumn= new TableColumn<>("Anno");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));
        dateColumn.setPrefWidth(70);
        filmTableView.getColumns().add(dateColumn);

    }

}

package com.project.sweprojectspring;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.authentications.SubscribedUserDao;
import com.project.sweprojectspring.daos.authentications.UserDao;
import com.project.sweprojectspring.daos.resources.FilmDao;
import com.project.sweprojectspring.daos.resources.ReviewDao;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Wishlist;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.models.resources.Review;
import com.project.sweprojectspring.models.authentications.Reviewer;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public class TemplateCasesTests {

    @Autowired
    private AuthHandler authHandler;

    @Autowired
    private WishlistDao wishlistDao;

    @Autowired
    private FilmDao filmDao;
    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private UserDao userDao;

    private User testUser;

    @BeforeEach
    public void setUp() {
        String username = "testuser";
        String password = "password";

        // Creazione di un utente di prova
        testUser = new Reviewer();
        testUser.setUsername(username);
        testUser.setPassword(password);

        userDao.create(testUser);

        Result<User> loginRes=authHandler.Login(testUser.getUsername(), testUser.getPassword());
        assertTrue(loginRes.isSuccessful());

    }

    // UC-1 Register Account
    @Test
    public void testUC01_RegisterAccount() {
        String username = "registeruser";
        String password = "password";
        String repassword = "password";

        Result<User> result = authHandler.Register(username, password, repassword);

        assertTrue(result.isSuccessful());
        assertNotNull(result.toValue());
    }

    @Test
    public void testUC01_RegisterAccount_FailUsernameExists() {
        String username = "testuser";
        String password = "password";
        String repassword = "password";

        Result<User> result = authHandler.Register(username, password, repassword);

        assertFalse(result.isSuccessful());
        assertEquals("esiste gia un utente con questo nome", result.ToError().getMessage());
    }

    @Test
    public void testUC01_RegisterAccount_FailPasswordMismatch() {
        String username = "newuser";
        String password = "password";
        String repassword = "wrongpassword";

        Result<User> result = authHandler.Register(username, password, repassword);

        assertFalse(result.isSuccessful());
        assertEquals("le passord devono combaciare", result.ToError().getMessage());
    }

    // UC-2 Add to Wishlist
    @Test
    public void testUC02_AddToWishlist() {
        Film film = new Film();
        film.setTitle("Film di Prova");

        Wishlist wishlist = new Wishlist();
        wishlist.setName("Lista dei Preferiti");
        wishlist.setSubscribedUser((SubscribedUser) testUser);

        Result<Wishlist> createResult=wishlistDao.create(wishlist);
        assertTrue(createResult.isSuccessful());

        wishlist.getFilms().add(film);
        Result<Wishlist> result=wishlistDao.update(wishlist);

        assertTrue(result.isSuccessful());
        Wishlist retWishlist=result.toValue();
        assertTrue(retWishlist.getFilms().contains(film));
    }

    @Test
    public void testUC02_AddToWishlist_AlreadyExists() {
        Film film = new Film();
        film.setTitle("Film già presente");

        Wishlist wishlist = new Wishlist();
        wishlist.setName("Lista Preferiti");
        wishlist.setSubscribedUser((SubscribedUser) testUser);

        Result<Wishlist> result=wishlistDao.create(wishlist);
        assertTrue(result.isSuccessful());

        wishlist.getFilms().add(film);

        result=wishlistDao.update(wishlist);
        assertTrue(result.isSuccessful());

    }

    // UC-3 Change Password
    @Test
    public void testUC03_ChangePassword_Success() {
        String username = "testuser";
        String oldPassword = "password";
        String newPassword = "newpassword";

        Result<User> result = authHandler.Change(username, oldPassword, newPassword);
        assertTrue(result.isSuccessful());
    }

    @Test
    public void testUC03_ChangePassword_Fail_WrongUsername() {

        String username = "wronguser";
        String oldPassword = "oldpassword";
        String newPassword = "newpassword";

        Result<User> result = authHandler.Change(username, oldPassword, newPassword);
        assertFalse(result.isSuccessful());
        assertEquals("username non coincide con quello loggato", result.ToError().getMessage());
    }

    // UC-4 Add Review
    @Test
    public void testUC04_AddReview_Success() {

        Film film = new Film();
        film.setTitle("Film da recensire");

        Review review=new Review("Recensione eccellente!");
        review.setFilm(film);
        review.setReviewer((Reviewer) testUser);

        Result<Review> result = reviewDao.create(review);

        assertTrue(result.isSuccessful());
    }

    // UC-5 View All Reviews
    @Test
    public void testUC05_ViewAllReviews() {

        Film film=new Film();
        film.setTitle("film");

        Review review1 = new Review();
        review1.setDescription("Recensione 1");
        review1.setReviewer((Reviewer) authHandler.getLoggedUser());
        review1.setFilm(film);

        Review review2 = new Review();
        review2.setDescription("Recensione 2");
        review2.setReviewer((Reviewer) authHandler.getLoggedUser());
        review2.setFilm(film);

        Result<Review> result = reviewDao.create(review1);
        assertTrue(result.isSuccessful());

        result = reviewDao.create(review2);
        assertTrue(result.isSuccessful());

        Result<List<Review>> resultlist = reviewDao.retrieveAll();

        List<Review> filtered=resultlist.toValue()
                .stream().filter(review -> {
                    return review.getReviewer().equals(authHandler.getLoggedUser());
                }).toList();

        assertTrue(resultlist.isSuccessful());
        assertEquals(2, filtered.size());
    }

    // UC-6 Add Film (Administrator)
    @Test
    public void testUC06_AddFilm_Success() {
        Film film = new Film();
        film.setTitle("Nuovo Film");
        film.setAuthor("Autore Test");
        film.setDescription("Descrizione del film");

        Result<Film> result = filmDao.create(film);

        assertTrue(result.isSuccessful());
        assertEquals("Nuovo Film", result.toValue().getTitle());
    }
}

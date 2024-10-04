package com.project.sweprojectspring;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.FilmDao;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.daos.billings.PremiumSubDao;
import com.project.sweprojectspring.daos.billings.StandardSubDao;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.services.AuthHandler;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthenticationTests {

    @Autowired
    private AuthHandler authHandler;

    @Autowired
    private UserDao userDao;

    @Autowired
    private FilmDao filmDao;

    @Autowired
    private StandardSubDao standardSubDao;

    @Autowired
    private PremiumSubDao premiumSubDao;


    // Test del metodo Login
    @Test
    public void testLoginSuccess() {
        // Creazione di un utente di prova
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userDao.create(user);

        // Verifica che il login abbia successo
        Result<User> result = authHandler.Login("testuser", "password");
        assertTrue(result.isSuccessful());
        assertNotNull(authHandler.getLoggedUser());
    }

    @Test
    public void testLoginFail_WrongPassword() {
        // Creazione di un utente di prova
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userDao.create(user);

        // Verifica che il login fallisca con password errata
        Result<User> result = authHandler.Login("testuser", "wrongpassword");
        assertFalse(result.isSuccessful());
    }

    // Test del metodo Register
    @Test
    public void testRegisterSuccess() {
        Result<User> result = authHandler.Register("newuser", "password", "password");
        assertTrue(result.isSuccessful());

        // Verifica che l'utente sia stato creato
        User user = userDao.retrieveOne(new User("newuser", "password")).toValue();
        assertNotNull(user);
    }

    @Test
    public void testRegisterFail_PasswordMismatch() {
        Result<User> result = authHandler.Register("newuser", "password", "differentpassword");
        assertFalse(result.isSuccessful());
    }

    // Test del metodo RegisterFilm
    @Test
    public void testRegisterFilmSuccess() {
        Film testFilm=new Film();
        testFilm.setTitle("FilmTitle");
        testFilm.setAuthor("FilmAuthor");
        testFilm.setDescription("FilmDescription");
        testFilm.setReleaseDate(2024);

        Result<Film> result = authHandler.RegisterFilm(
                testFilm.getTitle(), testFilm.getAuthor()
                , testFilm.getDescription(), testFilm.getReleaseDate());
        assertTrue(result.isSuccessful());

        // Verifica che il film sia stato creato
        Film film = filmDao.retrieveOne(testFilm).toValue();
        assertNotNull(film);
    }

    @Test
    public void testRegisterFilmFail_MissingTitle() {
        Result<Film> result = authHandler.RegisterFilm("", "FilmAuthor", "FilmDescription", 2024);
        assertFalse(result.isSuccessful());
    }

    // Test del metodo RegisterStandardSubscription
    @Test
    public void testRegisterStandardSubscription() {
        // Simula un login
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userDao.create(user);
        authHandler.Login("testuser", "password");

        // Verifica che la registrazione all'abbonamento standard abbia successo
        Result<Boolean> result = authHandler.RegisterStandardSubscription();
        assertTrue(result.isSuccessful());
    }

    // Test del metodo Logout
    @Test
    public void testLogout() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        userDao.create(user);
        authHandler.Login("testuser", "password");

        boolean logoutSuccess = authHandler.Logout();
        assertTrue(logoutSuccess);
        assertNull(authHandler.getLoggedUser());
    }
}


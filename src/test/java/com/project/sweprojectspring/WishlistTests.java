package com.project.sweprojectspring;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.authentications.UserDao;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Wishlist;
import com.project.sweprojectspring.controllers.WishlistFormController;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;

@SpringBootTest
@Transactional
public class WishlistTests {

    @Autowired
    private WishlistDao wishlistDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WishlistFormController wishlistFormController;

    private SubscribedUser testUser;

    @BeforeEach
    public void setUp() {
        // Creazione di un utente di prova
        testUser = new SubscribedUser();
        testUser.setUsername("testuser");
        testUser.setPassword("password");

        userDao.create(testUser);
    }

    // Test per creare una wishlist
    @Test
    public void testCreateWishlistSuccess() {
        Wishlist wishlist = new Wishlist();
        wishlist.setName("Film Wishlist");
        wishlist.setDescription("My favorite films");
        wishlist.setSubscribedUser(testUser);

        Result<Wishlist> result = wishlistDao.create(wishlist);
        assertTrue(result.isSuccessful());
        assertNotNull(result.toValue());
    }

    // Test per creare una wishlist con nome vuoto (fallimento)
    @Test
    public void testCreateWishlistFail_EmptyName() {
        Wishlist wishlist = new Wishlist();
        wishlist.setName("");  // Nome vuoto
        wishlist.setDescription("Wishlist senza nome");
        wishlist.setSubscribedUser(testUser);

        Result<Wishlist> result = wishlistDao.create(wishlist);
        assertFalse(result.isSuccessful());
        assertEquals("Assegnare un nome", result.toString());
    }

    // Test per il recupero di tutte le wishlist
    @Test
    public void testRetrieveAllWishlists() {
        // Creare alcune wishlist per il test
        Wishlist wishlist1 = new Wishlist("Wishlist1", "Descrizione 1", testUser);
        Wishlist wishlist2 = new Wishlist("Wishlist2", "Descrizione 2", testUser);

        wishlistDao.create(wishlist1);
        wishlistDao.create(wishlist2);

        Result<List<Wishlist>> result = wishlistDao.retrieveAll();

        List<Wishlist> userLists=result.toValue().stream().filter(wishlist -> {
          return  wishlist.getSubscribedUser().equals(testUser);
        }).toList();

        assertTrue(result.isSuccessful());
        assertEquals(2, userLists.size());
    }

    // Test per il recupero di una singola wishlist tramite filtro
    @Test
    public void testRetrieveOneWishlist() {
        Wishlist wishlist = new Wishlist("WishlistUnica", "Descrizione unica", testUser);
        wishlistDao.create(wishlist);

        Wishlist filter = new Wishlist();
        filter.setName("WishlistUnica");

        Result<Wishlist> result = wishlistDao.retrieveOne(filter);
        assertTrue(result.isSuccessful());
        assertNotNull(result.toValue());
    }

    // Test per l'aggiornamento di una wishlist
    @Test
    public void testUpdateWishlist() {
        Wishlist wishlist = new Wishlist("WishlistToUpdate", "Descrizione vecchia", testUser);
        wishlistDao.create(wishlist);

        wishlist.setDescription("Nuova descrizione aggiornata");
        Result<Wishlist> result = wishlistDao.update(wishlist);

        assertTrue(result.isSuccessful());
        assertEquals("Nuova descrizione aggiornata", result.toValue().getDescription());
    }

    // Test per la cancellazione di una wishlist
    @Test
    public void testDeleteWishlist() {
        Wishlist wishlist = new Wishlist("WishlistToDelete", "Descrizione da cancellare", testUser);
        wishlistDao.create(wishlist);

        Result<Wishlist> result = wishlistDao.delete(wishlist);
        assertTrue(result.isSuccessful());

        Result<Wishlist> check = wishlistDao.retrieveOne(wishlist);
        assertFalse(check.isSuccessful());
    }

    // Test del metodo filmExistsInUserWishlists
    @Test
    public void testFilmExistsInUserWishlist() {
        // Creazione di una wishlist con un film
        Film film = new Film();
        film.setTitle("Film Unico");
        Wishlist wishlist = new Wishlist("WishlistConFilm", "Descrizione", testUser);
        wishlist.setFilms(new HashSet<>());
        wishlist.getFilms().add(film);
        wishlistDao.create(wishlist);

        Result<Long> result = wishlistDao.filmExistsInUserWishlists(testUser, film);
        assertTrue(result.isSuccessful());
        assertEquals(1, result.toValue());
    }

}

package com.project.sweprojectspring.daos.resources;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Wishlist;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("WishlistDao")
public class WishlistDao extends DAO<Wishlist> {
    @Override
    public Result<Wishlist> create(Wishlist wishlist) {
        if(wishlist.getName().isEmpty())
            return  Result.fail("Assegnare un nome");
        try {
            entityManager.persist(wishlist);
            return Result.success(wishlist);
        } catch (DataIntegrityViolationException e) {
            return  Result.fail("whishlist già esistente.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<Wishlist>> retrieveAll() {
        try {
            List<Wishlist> result= entityManager.createQuery(
                            "SELECT w FROM Wishlist w WHERE true"
                            , Wishlist.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Wishlist> retrieveOne(Wishlist filter) {
        try {
            Wishlist result= entityManager.createQuery("SELECT w FROM Wishlist w WHERE " +
                            "(w.id = :id OR :id IS NULL) AND " +
                            "(w.name = :name OR :name IS NULL) AND " +
                            "(w.description = :description OR :description IS NULL)", Wishlist.class)
                    .setParameter("id", filter.getId())
                    .setParameter("name", filter.getName())
                    .setParameter("description", filter.getDescription())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Wishlist> update(Wishlist wishlist) {
        try {
            // Trova la wishlist esistente
            Wishlist filter=new Wishlist(wishlist.getId(), wishlist.getName());
            Result<Wishlist> existingWishlistResult = retrieveOne(filter);
            if (existingWishlistResult.isFailed()) {
                return Result.fail("Wishlist not found");
            }

            Wishlist existingWishlist = existingWishlistResult.toValue();

            // Aggiorna i campi della wishlist esistente con i valori della nuova wishlist
            existingWishlist.setName(wishlist.getName());
            existingWishlist.setDescription(wishlist.getDescription());
            existingWishlist.setFilms(wishlist.getFilms());

            // Esegui il merge delle modifiche
            entityManager.merge(existingWishlist);

            return Result.success(existingWishlist);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Data integrity violation.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Wishlist> delete(Wishlist wishlist) {
        try {
            Result<Wishlist> wishlistToDeleteResult = retrieveOne(wishlist);
            if (wishlistToDeleteResult.isFailed()) {
                return Result.fail("Film not found");
            }
            entityManager.remove(wishlistToDeleteResult.toValue());
            return Result.success(wishlistToDeleteResult.toValue());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public boolean any(Wishlist wishlist) {
        return count(wishlist) > 0;
    }
    @Override
    public Long count(Wishlist filter) {
        Long count = entityManager.createQuery("SELECT w FROM Wishlist w WHERE " +
                        "(w.id = :id OR :id IS NULL) AND " +
                        "(w.name = :name OR :name IS NULL) AND " +
                        "(w.description = :description OR :description IS NULL)", Long.class)
                .setParameter("id", filter.getId())
                .setParameter("name", filter.getName())
                .setParameter("description", filter.getDescription())
                .getSingleResult();
        return count;
    }

    public Result<Long> filmExistsInUserWishlists(SubscribedUser user, Film film) {
        try {
            // Query JPQL per verificare se il film esiste in una delle wishlist dell'utente
            Long count = entityManager.createQuery(
                            "SELECT COUNT(w) FROM Wishlist w JOIN w.films f WHERE w.subscribedUser.id = :userId AND f.id = :filmId", Long.class)
                    .setParameter("userId", user.getId())
                    .setParameter("filmId", film.getId())
                    .getSingleResult();

            // Se il conteggio è maggiore di 0, il film è presente in almeno una wishlist
            return Result.success(count);
        } catch (Exception e) {
            // Gestione di eventuali errori
            return Result.fail(e);
        }
    }



}

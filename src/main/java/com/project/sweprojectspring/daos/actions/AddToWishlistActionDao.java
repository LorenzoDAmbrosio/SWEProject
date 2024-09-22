package com.project.sweprojectspring.daos.actions;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.models.actions.AddToWishlistAction;
import com.project.sweprojectspring.models.resources.Film;
import com.project.sweprojectspring.models.resources.Wishlist;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Predicate;

@Repository("AddToWishlistActionDao")
public class AddToWishlistActionDao extends DAO<AddToWishlistAction> {

    @Autowired
    private WishlistDao wishlistDao;

    @Override
    public Result<AddToWishlistAction> create(AddToWishlistAction action) {
        try {
            entityManager.persist(action);
            return Result.success(action);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Action già esistente.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<AddToWishlistAction>> retrieveAll() {
        try {
            List<AddToWishlistAction> result = entityManager.createQuery(
                            "SELECT a FROM AddToWishlistAction a WHERE true", AddToWishlistAction.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<AddToWishlistAction> retrieveOne(AddToWishlistAction filter) {
        try {
            AddToWishlistAction result = entityManager.createQuery("SELECT a FROM AddToWishlistAction a WHERE " +
                            "(a.film = :film OR :film IS NULL) AND " +
                            "(a.subscribedUser = :subscribedUser OR :subscribedUser IS NULL)", AddToWishlistAction.class)
                    .setParameter("film", filter.getFilm())
                    .setParameter("subscribedUser", filter.getSubscribedUser())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<AddToWishlistAction> update(AddToWishlistAction action) {
        try {
            // Trova l'azione esistente
            Result<AddToWishlistAction> existingActionResult = retrieveOne(action);
            if (existingActionResult.isFailed()) {
                return Result.fail("Action not found");
            }

            AddToWishlistAction existingAction = existingActionResult.toValue();

            // Non ci sono campi specifici da aggiornare oltre a quelli ereditati, quindi esegui il merge
            entityManager.merge(existingAction);

            return Result.success(existingAction);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Data integrity violation.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<AddToWishlistAction> delete(AddToWishlistAction action) {
        try {
            Result<AddToWishlistAction> actionToDeleteResult = retrieveOne(action);
            if (actionToDeleteResult.isFailed()) {
                return Result.fail("Action not found");
            }
            entityManager.remove(actionToDeleteResult.toValue());
            return Result.success(actionToDeleteResult.toValue());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public boolean any(AddToWishlistAction action) {
        return count(action) > 0;
    }

    @Override
    public Long count(AddToWishlistAction filter) {
        Long count = entityManager.createQuery("SELECT COUNT(a) FROM AddToWishlistAction a WHERE " +
                        "(a.id = :id OR :id IS NULL) AND " +
                        "(a.film = :film OR :film IS NULL) AND " +
                        "(a.subscribedUser = :subscribedUser OR :subscribedUser IS NULL)", Long.class)
                .setParameter("id", filter.getId())
                .setParameter("film", filter.getFilm())
                .setParameter("subscribedUser", filter.getSubscribedUser())
                .getSingleResult();

        return count;
    }

    public  Result<Boolean> Sync(List<AddToWishlistAction> actions
        , Predicate<? super AddToWishlistAction> oldFilter)
    {
        List<AddToWishlistAction> oldActions= retrieveAll()
                .toValue();
        oldActions.removeIf(oldFilter);
        Result<Long> res;
        for(AddToWishlistAction oldAction : oldActions){
            if(actions.contains(oldAction))
                continue;
             res= wishlistDao
                .filmExistsInUserWishlists(oldAction.getSubscribedUser(),oldAction.getFilm());
            if(res.isFailed() || res.toValue() > 0)
                continue;

            delete(oldAction);
        }
        for(AddToWishlistAction newAction : actions){
            if(oldActions.contains(newAction))
                continue;

            res= wishlistDao
                    .filmExistsInUserWishlists(newAction.getSubscribedUser(),newAction.getFilm());
            if(res.isFailed() || res.toValue() == 0)
                continue;

            create(newAction);
        }
        return Result.success(true);
    }

    public void SyncFilmInWishlist(Wishlist wishlist, Film film) {
        Result<Long> res= wishlistDao
                .filmExistsInUserWishlists(wishlist.getSubscribedUser(),film);

        if(res.isFailed()) return;

        AddToWishlistAction action=new AddToWishlistAction();
        action.setSubscribedUser(wishlist.getSubscribedUser());
        action.setFilm(film);


        if(!wishlist.containsFilm(film)) {
            if(res.toValue() > 0)
                return;
            delete(action);
        }else{
            if(res.toValue() > 1)
                return;
            create(action);
        }
    }
}

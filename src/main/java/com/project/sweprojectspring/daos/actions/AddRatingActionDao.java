package com.project.sweprojectspring.daos.actions;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.actions.AddRatingAction;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("AddRatingActionDao")
public class AddRatingActionDao extends DAO<AddRatingAction> {

    @Override
    public Result<AddRatingAction> create(AddRatingAction action) {
        try {
            entityManager.persist(action);
            return Result.success(action);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Action already exists.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<AddRatingAction>> retrieveAll() {
        try {
            List<AddRatingAction> result = entityManager.createQuery(
                            "SELECT a FROM AddRatingAction a WHERE true", AddRatingAction.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<AddRatingAction> retrieveOne(AddRatingAction filter) {
        try {
            AddRatingAction result = entityManager.createQuery("SELECT a FROM AddRatingAction a WHERE " +
                            "(a.film = :film OR :film IS NULL) AND " +
                            "(a.subscribedUser = :subscribedUser OR :subscribedUser IS NULL)", AddRatingAction.class)
                    .setParameter("film", filter.getFilm())
                    .setParameter("subscribedUser", filter.getSubscribedUser())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<AddRatingAction> update(AddRatingAction action) {
        try {
            Result<AddRatingAction> existingActionResult = retrieveOne(action);
            if (existingActionResult.isFailed()) {
                return Result.fail("Action not found");
            }

            AddRatingAction existingAction = existingActionResult.toValue();
            entityManager.merge(existingAction);

            return Result.success(existingAction);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Data integrity violation.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<AddRatingAction> delete(AddRatingAction action) {
        try {
            Result<AddRatingAction> actionToDeleteResult = retrieveOne(action);
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
    public boolean any(AddRatingAction action) {
        return count(action) > 0;
    }

    @Override
    public Long count(AddRatingAction filter) {
        Long count = entityManager.createQuery("SELECT COUNT(a) FROM AddRatingAction a WHERE " +
                        "(a.id = :id OR :id IS NULL) AND " +
                        "(a.film = :film OR :film IS NULL) AND " +
                        "(a.subscribedUser = :subscribedUser OR :subscribedUser IS NULL)", Long.class)
                .setParameter("id", filter.getId())
                .setParameter("film", filter.getFilm())
                .setParameter("subscribedUser", filter.getSubscribedUser())
                .getSingleResult();

        return count;
    }
}

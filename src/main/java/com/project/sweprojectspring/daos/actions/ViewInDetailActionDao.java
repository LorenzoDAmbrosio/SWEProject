package com.project.sweprojectspring.daos.actions;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.actions.ViewInDetailAction;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ViewInDetailActionDao")
public class ViewInDetailActionDao extends DAO<ViewInDetailAction> {

    @Override
    public Result<ViewInDetailAction> create(ViewInDetailAction action) {
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
    public Result<List<ViewInDetailAction>> retrieveAll() {
        try {
            List<ViewInDetailAction> result = entityManager.createQuery(
                            "SELECT a FROM ViewInDetailAction a WHERE true", ViewInDetailAction.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<ViewInDetailAction> retrieveOne(ViewInDetailAction filter) {
        try {
            ViewInDetailAction result = entityManager.createQuery("SELECT a FROM ViewInDetailAction a WHERE " +
                            "(a.film = :film OR :film IS NULL) AND " +
                            "(a.subscribedUser = :subscribedUser OR :subscribedUser IS NULL)", ViewInDetailAction.class)
                    .setParameter("film", filter.getFilm())
                    .setParameter("subscribedUser", filter.getSubscribedUser())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<ViewInDetailAction> update(ViewInDetailAction action) {
        try {
            Result<ViewInDetailAction> existingActionResult = retrieveOne(action);
            if (existingActionResult.isFailed()) {
                return Result.fail("Action not found");
            }

            ViewInDetailAction existingAction = existingActionResult.toValue();
            entityManager.merge(existingAction);

            return Result.success(existingAction);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Data integrity violation.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<ViewInDetailAction> delete(ViewInDetailAction action) {
        try {
            Result<ViewInDetailAction> actionToDeleteResult = retrieveOne(action);
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
    public boolean any(ViewInDetailAction action) {
        return count(action) > 0;
    }

    @Override
    public Long count(ViewInDetailAction filter) {
        Long count = entityManager.createQuery("SELECT COUNT(a) FROM ViewInDetailAction a WHERE " +
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

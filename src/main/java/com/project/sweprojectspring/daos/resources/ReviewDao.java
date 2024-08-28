package com.project.sweprojectspring.daos.resources;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.resources.Review;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("ReviewDao")
public class ReviewDao extends DAO<Review> {
    @Override
    public Result<Review> create(Review review) {
        try {
            entityManager.persist(review);
            return Result.success(review);
        } catch (DataIntegrityViolationException e) {
            return  Result.fail("whishlist già esistente.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<Review>> retrieveAll() {
        try {
            List<Review> result= entityManager.createQuery(
                            "SELECT r FROM Review r WHERE true"
                            , Review.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Review> retrieveOne(Review filter) {
        try {
            Review result= entityManager.createQuery("SELECT w FROM Review w WHERE " +
                            "(w.id = :id OR :id IS NULL) AND " +
                            "(w.description = :description OR :description IS NULL)", Review.class)
                    .setParameter("id", filter.getId())
                    .setParameter("description", filter.getDescription())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Review> update(Review review) {
        return Result.fail("Non implementato");
    }

    @Override
    public Result<Review> delete(Review review) {
        try {
            Result<Review> wishlistToDeleteResult = retrieveOne(review);
            if (wishlistToDeleteResult.isFailed()) {
                return Result.fail("Film not found");
            }
            entityManager.remove(wishlistToDeleteResult.ToValue());
            return Result.success(wishlistToDeleteResult.ToValue());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public boolean any(Review wishlist) {
        return count(wishlist) > 0;
    }
    @Override
    public Long count(Review filter) {
        Long count = entityManager.createQuery("SELECT w FROM Review w WHERE " +
                        "(w.id = :id OR :id IS NULL) AND " +
                        "(w.description = :description OR :description IS NULL)", Long.class)
                .setParameter("id", filter.getId())
                .setParameter("description", filter.getDescription())
                .getSingleResult();
        return count;
    }

}

package com.project.sweprojectspring.daos.resources;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.resources.Review;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Repository("ReviewDao")
public class ReviewDao extends DAO<Review> {
    @Override
    public Result<Review> create(Review review) {
        try {
            if(review.getFilm() == null)
                return  Result.fail("Assegnare un film");

            review.setPublishDate(Date.from(ZonedDateTime.now().toInstant()));

            entityManager.persist(review);
            return Result.success(review);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Review già esistente.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<Review>> retrieveAll() {
        try {
            List<Review> result = entityManager.createQuery(
                            "SELECT r FROM Review r WHERE true", Review.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Review> retrieveOne(Review filter) {
        try {
            Review result = entityManager.createQuery("SELECT r FROM Review r WHERE " +
                            "(r.id = :id OR :id IS NULL) AND " +
                            "(r.description = :description OR :description IS NULL)", Review.class)
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
        try {
            if(review.getFilm() == null)
                return  Result.fail("Assegnare un film");

            Result<Review> existingReviewResult = retrieveOne(review);
            if (existingReviewResult.isFailed()) {
                return Result.fail("Review not found");
            }

            Review existingReview = existingReviewResult.toValue();

            // Aggiorna i campi della review esistente con i valori della nuova review
            existingReview.setDescription(review.getDescription());

            // Esegui il merge delle modifiche
            entityManager.merge(existingReview);

            return Result.success(existingReview);
        } catch (DataIntegrityViolationException e) {
            return Result.fail("Data integrity violation.");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<Review> delete(Review review) {
        try {
            Result<Review> reviewToDeleteResult = retrieveOne(review);
            if (reviewToDeleteResult.isFailed()) {
                return Result.fail("Review not found");
            }
            entityManager.remove(reviewToDeleteResult.toValue());
            return Result.success(reviewToDeleteResult.toValue());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public boolean any(Review review) {
        return count(review) > 0;
    }

    @Override
    public Long count(Review filter) {
        Long count = entityManager.createQuery("SELECT COUNT(r) FROM Review r WHERE " +
                        "(r.id = :id OR :id IS NULL) AND " +
                        "(r.description = :description OR :description IS NULL)", Long.class)
                .setParameter("id", filter.getId())
                .setParameter("description", filter.getDescription())
                .getSingleResult();
        return count;
    }
}

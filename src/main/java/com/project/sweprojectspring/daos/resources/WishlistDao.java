package com.project.sweprojectspring.daos.resources;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
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
        return Result.fail("Non implementato");
    }

    @Override
    public Result<Wishlist> delete(Wishlist wishlist) {
        try {
            Result<Wishlist> wishlistToDeleteResult = retrieveOne(wishlist);
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

}

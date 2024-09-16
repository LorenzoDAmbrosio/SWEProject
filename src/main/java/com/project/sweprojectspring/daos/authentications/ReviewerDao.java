package com.project.sweprojectspring.daos.authentications;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.Reviewer;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class ReviewerDao extends DAO<Reviewer> {
    @Override
    public Result<Reviewer> create(Reviewer reviewer) {
        try {
            entityManager.persist(reviewer);
            return Result.success(reviewer);
        } catch (DataIntegrityViolationException e) {
            //return Result.fail("User with this Username: "+user.getUsername()+" already exists");
            return  Result.fail("a");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<Reviewer>> retrieveAll() {
        return null;
    }

    @Override
    public Result<Reviewer> retrieveOne(Reviewer reviewer) {
        return null;
    }

    @Override
    public Result<Reviewer> update(Reviewer reviewer) {
        return null;
    }

    @Override
    public Result<Reviewer> delete(Reviewer reviewer) {
        return null;
    }

    @Override
    public boolean any(Reviewer reviewer) {
        return false;
    }

    @Override
    public Long count(Reviewer reviewer) {
        return null;
    }
}

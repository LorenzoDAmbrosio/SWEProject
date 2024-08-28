package com.project.sweprojectspring.daos.authentications;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.Reviewer;

import java.util.List;

public class ReviewerDao extends DAO<Reviewer> {
    @Override
    public Result<Reviewer> create(Reviewer reviewer) {

        return Result.fail("puppa");
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

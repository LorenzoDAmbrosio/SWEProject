package com.project.sweprojectspring.daos.authentications;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class SubscribedUserDao extends DAO<SubscribedUser>{
    @Override
    public Result<SubscribedUser> create(SubscribedUser subscribedUser) {
        return null;
    }

    @Override
    public Result<List<SubscribedUser>> retrieveAll() {
        return null;
    }

    @Override
    public Result<SubscribedUser> retrieveOne(SubscribedUser subscribedUser) {
        return null;
    }

    @Override
    public Result<SubscribedUser> update(SubscribedUser subscribedUser) {
        return null;
    }

    @Override
    public Result<SubscribedUser> delete(SubscribedUser subscribedUser) {
        return null;
    }

    @Override
    public boolean any(SubscribedUser subscribedUser) {
        return false;
    }

    @Override
    public Long count(SubscribedUser subscribedUser) {
        return null;
    }
}

package com.project.sweprojectspring.daos;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.User;

import java.util.List;

public class UserDao implements DAO<User> {
    @Override
    public void create(User user) {

    }

    @Override
    public List<Result<User>> retrieveAll() {

        return null;
    }

    @Override
    public Result<User> retrieveOne() {
        return null;
    }

    @Override
    public void update(User user, String[] params) {

    }

    @Override
    public void delete(User user) {

    }
}

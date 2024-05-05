package com.project.sweprojectspring.base;

import com.project.sweprojectspring.models.User;
import jakarta.transaction.Transactional;

import java.util.List;

/*
* Implements crud functionalities
* */
public interface DAOInterface<T> {

    @Transactional
    Result<T> create(T t);
    Result<List<T>> retrieveAll();
    Result<T> retrieveOne(T t);

    @Transactional
    Result<T> update(T t);

    @Transactional
    Result<T> delete(T t);

    boolean any(T t);

    Long count(T t);
}

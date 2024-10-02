package com.project.sweprojectspring.base;

import jakarta.transaction.Transactional;

import java.util.List;

/*
* Implements crud functionalities
* */
public interface DAOInterface<T> {

    @Transactional(Transactional.TxType.REQUIRED)
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

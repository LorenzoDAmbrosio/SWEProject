package com.project.sweprojectspring.base;

import java.util.List;

/*
* Implements crud functionalities
* */
public interface DAO<T> {

    void create(T t);
    List<Result<T>> retrieveAll();
    Result<T> retrieveOne();
    void update(T t, String[] params);

    void delete(T t);
}

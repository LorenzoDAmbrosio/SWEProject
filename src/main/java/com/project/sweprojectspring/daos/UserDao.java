package com.project.sweprojectspring.daos;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.authentications.User;
import jakarta.persistence.NoResultException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository("UserDao")
public class
UserDao extends DAO<User>{
    @Override
    public Result<User> create(User user) {
        try {
            entityManager.persist(user);
            return Result.success(user);
        } catch (DataIntegrityViolationException e) {
            //return Result.fail("User with this Username: "+user.getUsername()+" already exists");
            return  Result.fail("a");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<User>> retrieveAll() {
        try {
            List<User> result= entityManager.createQuery(
                            "SELECT u FROM User u WHERE true"
                            , User.class)
                    .getResultList();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<User> retrieveOne(User filter) {
        try {
            User result= entityManager.createQuery("SELECT u FROM User u WHERE " +
                            "(u.id = :id OR :id IS NULL) AND " +
                            "(u.username = :username OR :username IS NULL) AND " +
                            "(u.password = :password OR :password IS NULL)", User.class)
                    .setParameter("id", filter.getId())
                    .setParameter("username", filter.getUsername())
                    .setParameter("password", filter.getPassword())
                    .getSingleResult();
            return Result.success(result);
        } catch (NoResultException e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<User> update(User user) {
        try {
//            Result<User> userToUpdateResult = retrieveOne(user);
//            if (userToUpdateResult.isFailed()) {
//                return Result.fail("User not found");
//            }
            entityManager.merge(user);
            return Result.success(user);

        }catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<User> delete(User user) {
        try {
            Result<User> userToDeleteResult = retrieveOne(user);
            if (userToDeleteResult.isFailed()) {
                return Result.fail("User not found");
            }
            entityManager.remove(userToDeleteResult.toValue());
            return Result.success(userToDeleteResult.toValue());
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public boolean any(User user) {
        return count(user) > 0;
    }
    @Override
    public Long count(User partialUser) {
        Long count = entityManager.createQuery("SELECT COUNT(u) FROM User u WHERE " +
                        "(u.id = :id OR :id IS NULL) AND " +
                        "(u.username = :username OR :username IS NULL) AND " +
                        "(u.password = :password OR :password IS NULL)", Long.class)
                .setParameter("id", partialUser.getId())
                .setParameter("username", partialUser.getUsername())
                .setParameter("password", partialUser.getPassword())
                .getSingleResult();
        return count;
    }

    public void createUserTable() {
        try {
            entityManager.createNativeQuery("CREATE TABLE user (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(255) UNIQUE, password VARCHAR(255))").executeUpdate();
        } catch (Exception e) {
            // Handle exception if necessary
        }
    }
    public void dropUserTable() {
        try {
            entityManager.createNativeQuery("DROP TABLE user").executeUpdate();
        } catch (Exception e) {
            // Handle exception if necessary
        }
    }
}

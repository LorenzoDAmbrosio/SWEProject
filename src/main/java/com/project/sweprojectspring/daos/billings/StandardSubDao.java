package com.project.sweprojectspring.daos.billings;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.billings.StandardSub;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class StandardSubDao extends DAO<StandardSub> {
    @Override
    public Result<StandardSub> create(StandardSub StandardSub) {
        try {
            entityManager.persist(StandardSub);
            return Result.success(StandardSub);
        } catch (DataIntegrityViolationException e) {
            //return Result.fail("User with this Username: "+user.getUsername()+" already exists");
            return  Result.fail("a");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }


    @Override
    public Result<List<StandardSub>> retrieveAll() {
        return null;
    }


    @Override
    public Result<StandardSub> retrieveOne(StandardSub subscription) {
        return null;
    }

    @Override
    public Result<StandardSub> update(StandardSub subscription) {
        return null;
    }

    @Override
    public Result<StandardSub> delete(StandardSub subscription) {
        return null;
    }

    @Override
    public boolean any(StandardSub subscription) {
        return false;
    }

    @Override
    public Long count(StandardSub subscription) {
        return null;
    }

    public void createUserTable() {
        try {
            entityManager.createNativeQuery("CREATE TABLE STANDARD_SUBSCRIPTION (id INT AUTO_INCREMENT PRIMARY KEY, subscriptionStart Date , subscriptionDuration time)")
                    .executeUpdate();
        } catch (Exception e) {
            // Handle exception if necessary
        }
    }

    public void dropUserTable() {
        try {
            entityManager.createNativeQuery("DROP TABLE STANDARD_SUBSCRIPTION").executeUpdate();
        } catch (Exception e) {
            // Handle exception if necessary
        }
    }
}

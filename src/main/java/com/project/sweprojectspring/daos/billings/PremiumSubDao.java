package com.project.sweprojectspring.daos.billings;

import com.project.sweprojectspring.base.DAO;
import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.models.billings.PremiumSub;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

public class PremiumSubDao extends DAO<PremiumSub> {

    @Override
    public Result<PremiumSub> create(PremiumSub premiumSub) {
        try {
            entityManager.persist(premiumSub);
            return Result.success(premiumSub);
        } catch (DataIntegrityViolationException e) {
            //return Result.fail("User with this Username: "+user.getUsername()+" already exists");
            return  Result.fail("a");
        } catch (Exception e) {
            return Result.fail(e);
        }
    }

    @Override
    public Result<List<PremiumSub>> retrieveAll() {
        return null;
    }

    @Override
    public Result<PremiumSub> retrieveOne(PremiumSub premiumSub) {
        return null;
    }

    @Override
    public Result<PremiumSub> update(PremiumSub premiumSub) {
        return null;
    }

    @Override
    public Result<PremiumSub> delete(PremiumSub premiumSub) {
        return null;
    }

    @Override
    public boolean any(PremiumSub premiumSub) {
        return false;
    }

    @Override
    public Long count(PremiumSub premiumSub) {
        return null;
    }


    public void createUserTable() {
        try {
            entityManager.createNativeQuery("CREATE TABLE PREMIUM_SUB (id INT AUTO_INCREMENT PRIMARY KEY, subscriptionStart Date , subscriptionDuration time)")
                    .executeUpdate();
        } catch (Exception e) {
            // Handle exception if necessary
        }
    }

    public void dropUserTable() {
        try {
            entityManager.createNativeQuery("DROP TABLE PREMIUM_SUB").executeUpdate();
        } catch (Exception e) {
            // Handle exception if necessary
        }
    }
}


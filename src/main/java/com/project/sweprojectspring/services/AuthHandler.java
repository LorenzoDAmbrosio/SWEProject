package com.project.sweprojectspring.services;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.models.authentications.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthHandler {
    private User loggedUser = null;
    @Autowired
    private UserDao userDao;

    public  AuthHandler(UserDao userDao){
        this.userDao=userDao;
    }

    public boolean IsUserLogged(){
        return loggedUser==null;
    }

    public Result<User> Login(String username,String password){
        User filter=new User();

        if(username == null || username.trim().isEmpty()){
            return Result.fail("è richiesto un username");
        }
        if(password == null || password.trim().isEmpty()){
            return Result.fail("è richiesta una password");
        }

        filter.setUsername(username);
        if(!userDao.any(filter)){
            return Result.fail("Utente non trovato");
        }
        filter.setPassword(password);
        if(!userDao.any(filter)){
            return Result.fail("La password non coincide");
        }
        Result<User> foundUserResult=userDao.retrieveOne(new User(username,password));

        if(foundUserResult.isSuccessful()){
            loggedUser=foundUserResult.ToValue();
        }

        return foundUserResult;
    }
    public Result<User> Logout(){
        loggedUser=null;
        return Result.fail("Not implemented");
    }

}

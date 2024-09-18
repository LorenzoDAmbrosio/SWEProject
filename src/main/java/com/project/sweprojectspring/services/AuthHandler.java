package com.project.sweprojectspring.services;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.daos.authentications.ReviewerDao;
import com.project.sweprojectspring.models.authentications.Customer;
import com.project.sweprojectspring.models.authentications.Reviewer;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.authentications.User;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthHandler {
    @Getter
    private User loggedUser = null;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ReviewerDao reviewerDao;

    public  AuthHandler(UserDao userDao, ReviewerDao reviewerDao){
        this.userDao=userDao;
        this.reviewerDao=reviewerDao;
    }




    public boolean IsUserLogged(){
        return loggedUser!=null;
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
    public Result<User> Register(String username, String password, String repassword){
        User newUser= new User();

        if (username == null || username.trim().isEmpty()){
            return Result.fail("è richiesto un username");
        }
        if(password == null || password.trim().isEmpty()){
            return Result.fail("è richiesta una password");
        }
        if(!password.equals(repassword)){
            return Result.fail("le passord devono combaciare");
        }

        newUser.setUsername(username);
        if (userDao.any(newUser)){
            return Result.fail("esiste gia un utente con questo nome");
        }
        newUser.setPassword(password);
        return userDao.create(newUser);
    }

    @Transactional
    public boolean DiventaRew(){
        User subUser = getLoggedUser();
        Reviewer newReviewer = new Reviewer();
        long rewid = subUser.getId();

        reviewerDao.upgradeUser(rewid);
        User filter = new User(subUser.getId());
        Result<User> foundUserResult=userDao.retrieveOne(filter);

        if(foundUserResult.isSuccessful()){
            loggedUser=foundUserResult.ToValue();
        }
        return true;
    }

    public Result<User> Change(String username, String password, String newpassword){
        User user = getLoggedUser();

        if(username == null || username.trim().isEmpty()){
            return Result.fail("è richiesto un username");
        }
        if ( !(user.getUsername().equals(username) ) ){
            return Result.fail("username non coincide con quello loggato");
        }
        if(password == null || password.trim().isEmpty()){
            return Result.fail("è richiesta una password");
        }
        if (!( user.getPassword().equals(password))){
            return Result.fail("pass non coincide");
        }
        if(newpassword == null || newpassword.trim().isEmpty()){
            return Result.fail("è richiesta una password");
        }

        if(password.equals(newpassword)){
            return Result.fail("la nuova password è uguale a quella vecchia");
        }

        user.setPassword(newpassword);
        return userDao.update(user);


    }


    public boolean IsUserSubscribed(){
        if(!IsUserLogged()) return false;
        return loggedUser instanceof SubscribedUser;
    }
    public boolean IsUserCustomer(){
        if(!IsUserLogged()) return false;
        return loggedUser instanceof Customer;
    }
    public boolean IsUserReviewer(){
        if(!IsUserLogged()) return false;
        return loggedUser instanceof Reviewer;
    }


    public boolean Logout(){
        loggedUser=null;
        return true;
    }

}

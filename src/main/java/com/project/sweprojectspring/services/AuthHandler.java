package com.project.sweprojectspring.services;

import com.project.sweprojectspring.base.Result;
import com.project.sweprojectspring.daos.FilmDao;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.daos.authentications.ReviewerDao;
import com.project.sweprojectspring.daos.authentications.SubscribedUserDao;
import com.project.sweprojectspring.daos.billings.PremiumSubDao;
import com.project.sweprojectspring.daos.billings.StandardSubDao;
import com.project.sweprojectspring.models.authentications.Customer;
import com.project.sweprojectspring.models.authentications.Reviewer;
import com.project.sweprojectspring.models.authentications.SubscribedUser;
import com.project.sweprojectspring.models.authentications.User;
import com.project.sweprojectspring.models.billings.PremiumSub;
import com.project.sweprojectspring.models.billings.StandardSub;
import com.project.sweprojectspring.models.billings.Subscription;
import com.project.sweprojectspring.models.resources.Film;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.Date;

@Service
public class AuthHandler {
    @Getter
    private User loggedUser = null;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ReviewerDao reviewerDao;
    @Autowired
    private SubscribedUserDao subscribedUserDao;
    @Autowired
    private StandardSubDao standardSubDao;
    @Autowired
    private PremiumSubDao premiumSubDao;
    @Autowired
    private FilmDao filmDao;


    public  AuthHandler(UserDao userDao, ReviewerDao reviewerDao, SubscribedUserDao subscribedUserDao){
        this.userDao=userDao;
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
            loggedUser=foundUserResult.toValue();
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
    public Result<Film> RegisterFilm(String title, String au, String description, int date){
        Film fl = new Film();
        if (title == null || title.trim().isEmpty()){
            return Result.fail("è richiesto un titolo");
        }
        if(au == null || au.trim().isEmpty()){
            return Result.fail("è richiesto un autore");
        }
        fl.setAuthor(au);
        fl.setDescription(description);
        fl.setTitle(title);
        fl.setReleaseDate(date);

       return filmDao.create(fl);
    }

    public Result<Boolean> RegisterStandardSubscription(){
        StandardSub sb = new StandardSub();
        Date subscriptionStart= new Date();

        Time subscriptionDuration = new Time(200000*1000);

        sb.setSubscriptionDuration(subscriptionDuration);
        sb.setSubscriptionStart(subscriptionStart);

        standardSubDao.create(sb);
        AssociaUserSubscription(sb.getId());

        return Result.success(true);
    }

    public Result<Boolean> RegisterPremiumSubscription() {
        PremiumSub pb = new PremiumSub();
        Date subscriptionStart = new Date();

        Time subscriptionDuration = new Time(200000 * 1000);

        pb.setSubscriptionDuration(subscriptionDuration);
        pb.setSubscriptionStart(subscriptionStart);

        premiumSubDao.create(pb);
        AssociaUserSubscription(pb.getId());

        return Result.success(true);
    }



    @Transactional
    public void DiventaRew(){
        User subUser = getLoggedUser();
        Reviewer newReviewer = new Reviewer();
        long rewid = subUser.getId();

        reviewerDao.upgradeUser(rewid);
        User filter = new User(subUser.getId());
        Result<User> foundUserResult=userDao.retrieveOne(filter);

        if(foundUserResult.isSuccessful()){
            loggedUser=foundUserResult.toValue();
        }
    }
    @Transactional
    public Result<User> AssociaUserSubscription(long subid){
        User subUser = getLoggedUser();
        long usid = subUser.getId();


        subscribedUserDao.compilaSubscribed(usid,subid);
        User filter = new User(subUser.getId());

        Result<User> foundUserResult=userDao.retrieveOne(filter);
        if(foundUserResult.isSuccessful()){
            loggedUser=foundUserResult.toValue();
        }
        return foundUserResult;
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

    public Result<Subscription> getCurrentSubscription(){
        if(!IsUserSubscribed())
            return Result.fail("L'Utente loggato non è autorizzato");
        SubscribedUser user= (SubscribedUser) getLoggedUser();
        Subscription subscription=user.getSubscription();
        if(subscription == null)
            return  Result.fail("L'Utente loggato è privo di iscrizione");
        return Result.success(subscription);
    }

    public boolean Logout(){
        loggedUser=null;
        return true;
    }

}

package com.project.sweprojectspring;

import com.project.sweprojectspring.components.FilmComponent;
import com.project.sweprojectspring.components.container.FilmContainerComponent;
import com.project.sweprojectspring.controllers.FilmDetailController;
import com.project.sweprojectspring.controllers.MainMenuController;
import com.project.sweprojectspring.controllers.WishlistFormController;
import com.project.sweprojectspring.controllers.authentication.LoginPageController;
import com.project.sweprojectspring.controllers.authentication.LogoutPageController;
import com.project.sweprojectspring.daos.FilmDao;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.daos.actions.AddToWishlistActionDao;
import com.project.sweprojectspring.daos.resources.ReviewDao;
import com.project.sweprojectspring.daos.resources.WishlistDao;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan("com.project.sweprojectspring.models")
public class SweProjectSpringApplication {
    public static void main(String[] args) {
        Application.launch(SWEApplication.class,args);
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    //endregion
    //region Daos
    @Bean
    public UserDao userDao() {
        return new UserDao();
    }
    @Bean
    public FilmDao filmDao() {
        return new FilmDao();
    }
    @Bean
    public WishlistDao wishlistDao() {
        return new WishlistDao();
    }
    @Bean
    public ReviewDao reviewDao() {
        return new ReviewDao();
    }
    @Bean
    public AddToWishlistActionDao addToWishlistActionDao() {
        return new AddToWishlistActionDao();
    }


    //endregion

    @Bean
    public AuthHandler authHandler() {
        return new AuthHandler(userDao());
    }

    @Bean
    public StageHandler stageHandler() {
        return new StageHandler();
    }

    //region Controllers Dependency Injection

    @Bean
    @Scope("prototype")
    //abilita la dependency injection sui controller javafx
    public MainMenuController mainMenuController () {
        return new MainMenuController();
    }
    @Bean
    @Scope("prototype")
    public LoginPageController loginPageController () {
        return new LoginPageController();
    }

    @Bean
    @Scope("prototype")
    public LogoutPageController logoutPageController () { return new LogoutPageController();}
    @Bean
    @Scope("prototype")
    public FilmContainerComponent filmContainerComponent () {
        return new FilmContainerComponent();
    }
    @Bean
    @Scope("prototype")
    public FilmComponent filmComponent () {
        return new FilmComponent();
    }
    @Bean
    @Scope("prototype")
    public FilmDetailController filmDetailController () {
        return new FilmDetailController();
    }
    @Bean
    @Scope("prototype")
    public WishlistFormController wishlistFormController () {
        return new WishlistFormController();
    }
    //endregion

}


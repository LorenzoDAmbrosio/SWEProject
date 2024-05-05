package com.project.sweprojectspring;

import com.project.sweprojectspring.controllers.MainMenuController;
import com.project.sweprojectspring.controllers.authentication.LoginPageController;
import com.project.sweprojectspring.controllers.authentication.LogoutPageController;
import com.project.sweprojectspring.daos.UserDao;
import com.project.sweprojectspring.services.AuthHandler;
import com.project.sweprojectspring.services.StageHandler;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@SpringBootApplication
@EnableTransactionManagement
@EntityScan("com.project.sweprojectspring.models")
public class SweProjectSpringApplication {
    public static void main(String[] args) {
        Application.launch(SWEApplication.class,args);
    }

    //region EntityManager Initialization
//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
////        LocalContainerEntityManagerFactoryBean em
////                = new LocalContainerEntityManagerFactoryBean();
////        em.setDataSource(dataSource);
////        em.setPackagesToScan("com.project.sweprojectspring.models");
//
////        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
////        em.setJpaVendorAdapter(vendorAdapter);
////        em.setJpaProperties(additionalProperties());
//
//        return em;
//    }

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
    //endregion

}


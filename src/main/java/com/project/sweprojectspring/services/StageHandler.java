package com.project.sweprojectspring.services;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import javax.security.auth.callback.Callback;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.function.Function;

@Service
@Configurable
public class StageHandler {


    @Value("classpath:/main/authentication/login-page.fxml")
    public Resource loginPageResource;
    @Value("classpath:/main/main-menu.fxml")
    public Resource mainMenuResource;
    @Value("classpath:/main/components/containers/FilmContainerComponent.fxml")
    public Resource filmContainerResource;
    @Value("classpath:/main/film/film-detail.fxml")
    public Resource filmDetailResource;
    @Value("classpath:/main/components/FilmComponent.fxml")
    public Resource filmResource;

    @Value("classpath:/main/authentication/registration-page.fxml")
    public Resource registestrationPageResource;

    @Autowired
    private ApplicationContext context;

    public StageHandler(){
    }

    public void SwitchStageFromEvent(Event event, Resource resource){
        Scene scene = ((Node)event.getSource()).getScene();
        Stage stage= (Stage) scene.getWindow();
        Parent parent=LoadResource(resource);
        stage.getScene().setRoot(parent);
    }
    public <T> void SwitchStageFromEvent(Event event, Resource resource,Function<T,T> func)
    {
        Scene scene = ((Node)event.getSource()).getScene();
        Stage stage= (Stage) scene.getWindow();
        Parent parent=LoadResource(resource,func);
        stage.getScene().setRoot(parent);
    }

    private Parent LoadResource(Resource resource){
        FXMLLoader loader= null;
        try {
            loader = new FXMLLoader(resource.getURL());
            loader.setControllerFactory(context::getBean);

            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private <T> Parent LoadResource(Resource resource,Function<T,T> func){
        FXMLLoader loader= null;
        try {
            loader = new FXMLLoader(resource.getURL());

            loader.setControllerFactory(c ->{
                T bean= (T) context.getBean(c);
                bean=func.apply(bean);
                return bean;
            });
//            T bean= (T) context.getBean(classType);
//            loader.setRoot(bean);

            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T LoadComponent(Resource resource, T instance,Function<T,T> parameterFunc) {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(resource.getURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loader.setControllerFactory(c ->{
            T bean= (T) context.getBean(c);
            bean=parameterFunc.apply(bean);
            return bean;
        });
        T bean=parameterFunc.apply(instance);
        loader.setRoot(bean);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }
    public <T> void LoadComponent(Resource resource, T instance) {
        FXMLLoader loader = null;
        try {
            loader = new FXMLLoader(resource.getURL());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        loader.setControllerFactory(context::getBean);
        loader.setRoot(instance);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

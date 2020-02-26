package com.ambroziepaval.stockui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ChartInitializer implements ApplicationListener<ChartApplication.StageReadyEvent> {

    @Value("classpath:chart.fxml")
    private Resource chartResource;

    private ApplicationContext applicationContext;

    public ChartInitializer(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ChartApplication.StageReadyEvent event) {
        Stage stage = event.getStage();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(chartResource.getURL());
            fxmlLoader.setControllerFactory(aClass -> applicationContext.getBean(aClass));

            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent, 800, 600));
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

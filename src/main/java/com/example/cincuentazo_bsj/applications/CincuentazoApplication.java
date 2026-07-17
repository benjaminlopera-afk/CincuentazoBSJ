package com.example.cincuentazo_bsj.applications;

import com.example.cincuentazo_bsj.utils.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CincuentazoApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CincuentazoApplication.class.getResource(Paths.START_VIEW));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Cincuentazo");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
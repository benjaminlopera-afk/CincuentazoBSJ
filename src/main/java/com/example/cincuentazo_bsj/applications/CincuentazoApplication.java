package com.example.cincuentazo_bsj.applications;

import com.example.cincuentazo_bsj.utils.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicación JavaFX. Carga la vista de inicio
 * y arranca la ventana principal.
 */
public class CincuentazoApplication extends Application {

    /**
     * Carga {@code StartView.fxml} y muestra la ventana principal del juego.
     *
     * @param stage ventana principal proporcionada por JavaFX.
     * @throws IOException si no se puede cargar el archivo FXML de inicio.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CincuentazoApplication.class.getResource(Paths.START_VIEW));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Cincuentazo");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Método principal de arranque de JavaFX.
     *
     * @param args argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
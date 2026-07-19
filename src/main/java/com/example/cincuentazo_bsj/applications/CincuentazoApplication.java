package com.example.cincuentazo_bsj.applications;

import com.example.cincuentazo_bsj.utils.Paths;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal de la aplicación Cincuentazo.
 * Gestiona la ventana principal y centraliza el cambio de escenas para
 * que los controladores no tengan que manejar {@code FXMLLoader}, {@code Scene}
 * ni {@code Stage} directamente.
 */
public class CincuentazoApplication extends Application {

    /** Ventana principal de la aplicación. */
    private static Stage stageWindow;

    /**
     * Inicializa y muestra la ventana principal con la pantalla de inicio.
     *
     * @param stage escenario principal proporcionado por JavaFX.
     */
    @Override
    public void start(Stage stage) {
        stageWindow = stage;
        setScene(Paths.START_VIEW, "Cincuentazo");
    }

    /**
     * Cambia la escena actual de la ventana principal y devuelve el
     * controlador asociado a la vista cargada, para que quien la invoque
     * pueda configurarlo (por ejemplo, pasarle el {@code Game} en curso).
     *
     * @param path  ruta del archivo FXML a cargar.
     * @param title título que tomará la ventana con la nueva vista.
     * @param <T>   tipo del controlador declarado en el FXML.
     * @return el controlador de la vista recién cargada.
     * @throws RuntimeException si no se puede cargar la vista indicada.
     */
    public static <T> T setScene(String path, String title) {
        FXMLLoader loader = new FXMLLoader(CincuentazoApplication.class.getResource(path));
        try {
            Parent root = loader.load();
            stageWindow.setScene(new Scene(root));
            stageWindow.setTitle(title);
            stageWindow.show();
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la vista: " + path, e);
        }
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
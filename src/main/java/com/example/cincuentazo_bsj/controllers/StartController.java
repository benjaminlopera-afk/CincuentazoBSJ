package com.example.cincuentazo_bsj.controllers;

import com.example.cincuentazo_bsj.model.Game;
import com.example.cincuentazo_bsj.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controlador de la pantalla de inicio (HU-1). Permite al jugador humano
 * seleccionar con cuántas máquinas jugar y arranca la partida.
 */
public class StartController {

    @FXML
    private ToggleGroup machineCountGroup;

    @FXML
    private RadioButton oneMachineOption;

    @FXML
    private RadioButton twoMachineOption;

    @FXML
    private RadioButton threeMachineOption;

    @FXML
    private Button startButton;

    @FXML
    private Button rulesButton;

    /**
     * Habilita el botón de inicio únicamente cuando el jugador ya
     * seleccionó una cantidad de máquinas.
     */
    @FXML
    public void initialize() {
        machineCountGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) ->
                startButton.setDisable(newToggle == null)
        );
    }

    /**
     * Crea la partida con la cantidad de máquinas seleccionada y cambia
     * la escena actual hacia la vista de juego.
     *
     * @param event evento de clic sobre el botón de inicio.
     * @throws IOException si no se puede cargar el archivo FXML de la vista de juego.
     */
    @FXML
    private void handleStartGame(ActionEvent event) throws IOException {
        int machineCount = getSelectedMachineCount();

        Game game = new Game();
        game.initializePlayers(machineCount);

        FXMLLoader loader = new FXMLLoader(getClass().getResource(Paths.GAME_VIEW));
        Parent root = loader.load();

        GameController gameController = loader.getController();
        gameController.setGame(game);

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Cincuentazo - Juego");
    }

    /**
     * @return la cantidad de máquinas seleccionada por el jugador (1, 2 o 3).
     */
    private int getSelectedMachineCount() {
        if (oneMachineOption.isSelected()) return 1;
        if (twoMachineOption.isSelected()) return 2;
        return 3;
    }

    /**
     * Muestra un resumen simplificado de las reglas del juego en una alerta,
     * para que el jugador pueda consultarlas antes de iniciar la partida.
     *
     * @param event evento de clic sobre el botón de reglas.
     */
    @FXML
    private void handleShowRules(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reglas de Cincuentazo");
        alert.setHeaderText("Objetivo: ser el último jugador en quedar en juego");
        alert.setContentText("""
                • La suma de la mesa nunca puede exceder 50.

                • Valores de las cartas:
                   - 2 al 8 y 10: suman su número.
                   - 9: no suma ni resta.
                   - J, Q, K: restan 10.
                   - A: suma 1 o 10, tú eliges.

                • En tu turno: juega una carta sin pasarte de 50,
                  luego toma una carta del mazo.

                • Si no tienes ninguna carta jugable, quedas eliminado.
                """);
        alert.showAndWait();
    }
}
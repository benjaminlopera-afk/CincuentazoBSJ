package com.example.cincuentazo_bsj.controllers;

import com.example.cincuentazo_bsj.model.Game;
import com.example.cincuentazo_bsj.utils.Paths;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.io.IOException;

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
    public void initialize() {
        machineCountGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) ->
                startButton.setDisable(newToggle == null)
        );
    }

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

    private int getSelectedMachineCount() {
        if (oneMachineOption.isSelected()) return 1;
        if (twoMachineOption.isSelected()) return 2;
        return 3;
    }
}
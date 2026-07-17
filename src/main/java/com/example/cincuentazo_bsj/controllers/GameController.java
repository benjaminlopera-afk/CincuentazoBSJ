package com.example.cincuentazo_bsj.controllers;

import com.example.cincuentazo_bsj.model.Card;
import com.example.cincuentazo_bsj.model.Game;
import com.example.cincuentazo_bsj.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GameController {

    @FXML private VBox machinesBox;
    @FXML private HBox humanHandBox;
    @FXML private Label tableCardLabel;
    @FXML private Label deckCountLabel;
    @FXML private Label tableSumLabel;

    private Game game;

    public void setGame(Game game) {
        this.game = game;
        game.startGame();
        renderMachineHands();
        renderHumanHand();
        renderTable();
        renderDeckCount();
    }

    private void renderMachineHands() {
        machinesBox.getChildren().clear();
        for (Player player : game.getPlayers()) {
            if (player.getClass().getSimpleName().equals("HumanPlayer")) continue;

            Label nameLabel = new Label(player.getName());
            nameLabel.getStyleClass().add("player-name-label");

            HBox handBox = new HBox(8);
            handBox.getStyleClass().add("hand-row");
            for (Card ignored : player.getHand()) {
                Label back = new Label();
                back.getStyleClass().add("card-back");
                handBox.getChildren().add(back);
            }

            VBox machineColumn = new VBox(6, nameLabel, handBox);
            machineColumn.getStyleClass().add("machine-column");
            machinesBox.getChildren().add(machineColumn);
        }
    }

    private void renderHumanHand() {
        humanHandBox.getChildren().clear();
        Player human = game.getPlayers().get(0);
        for (Card card : human.getHand()) {
            card.setFaceUp(true);
            Label cardLabel = new Label(card.getDisplayText());
            cardLabel.getStyleClass().add(card.isRedSuit() ? "card-face-red" : "card-face-black");
            humanHandBox.getChildren().add(cardLabel);
        }
    }

    private void renderTable() {
        Card topCard = game.getTable().getPlayedCards()
                .get(game.getTable().getPlayedCards().size() - 1);
        tableCardLabel.setText(topCard.getDisplayText());
        tableCardLabel.getStyleClass().add(topCard.isRedSuit() ? "card-face-red" : "card-face-black");
        tableSumLabel.setText("Suma: " + game.getTable().getSum());
    }

    private void renderDeckCount() {
        deckCountLabel.setText("Mazo: " + game.getDeck().size());
    }
}
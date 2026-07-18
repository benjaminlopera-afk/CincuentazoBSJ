package com.example.cincuentazo_bsj.controllers;

import com.example.cincuentazo_bsj.model.Card;
import com.example.cincuentazo_bsj.model.Game;
import com.example.cincuentazo_bsj.model.HumanPlayer;
import com.example.cincuentazo_bsj.model.Player;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Random;

/**
 * Controlador de la vista de juego. Refleja en pantalla el estado del
 * modelo {@link Game} y maneja la interacción del jugador humano
 * (jugar carta -> tomar carta) y el turno automático de las máquinas.
 */
public class GameController {

    @FXML private VBox machinesBox;
    @FXML private HBox humanHandBox;
    @FXML private Label tableCardLabel;
    @FXML private Label deckCountLabel;
    @FXML private Label tableSumLabel;
    @FXML private Button drawButton;
    @FXML private Label messageLabel;

    private Game game;
    private boolean awaitingHumanDraw = false;

    public void setGame(Game game) {
        this.game = game;
        game.startGame();
        renderMachineHands();
        renderHumanHand();
        renderTable();
        renderDeckCount();
        messageLabel.setText("Selecciona una carta para empezar a jugar.");
        triggerMachineTurnIfNeeded();
    }

    private void renderMachineHands() {
        machinesBox.getChildren().clear();
        for (Player player : game.getPlayers()) {
            if (player instanceof HumanPlayer) continue;

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
        boolean isHumanTurn = game.getCurrentPlayer() == human;

        for (Card card : human.getHand()) {
            card.setFaceUp(true);
            Label cardLabel = new Label(card.getDisplayText());
            cardLabel.getStyleClass().add(card.isRedSuit() ? "card-face-red" : "card-face-black");

            boolean playable = isHumanTurn && !awaitingHumanDraw
                    && card.getBestValueFor(game.getTable().getSum()) != Integer.MIN_VALUE;

            if (playable) {
                cardLabel.getStyleClass().add("card-playable");
                cardLabel.setOnMouseClicked(e -> onHumanCardSelected(card));
            } else {
                cardLabel.getStyleClass().add("card-disabled");
            }
            humanHandBox.getChildren().add(cardLabel);
        }
    }

    private void renderTable() {
        Card topCard = game.getTable().getPlayedCards()
                .get(game.getTable().getPlayedCards().size() - 1);
        tableCardLabel.setText(topCard.getDisplayText());
        tableCardLabel.getStyleClass().removeAll("card-face-red", "card-face-black");
        tableCardLabel.getStyleClass().add(topCard.isRedSuit() ? "card-face-red" : "card-face-black");
        tableSumLabel.setText("Suma: " + game.getTable().getSum());
    }

    private void renderDeckCount() {
        deckCountLabel.setText("Mazo: " + game.getDeck().size());
    }

    private void onHumanCardSelected(Card card) {
        game.playCard(game.getPlayers().get(0), card);
        awaitingHumanDraw = true;

        messageLabel.setText("Jugaste " + card.getDisplayText() + ". Ahora toma una carta del mazo.");
        drawButton.setDisable(false);

        renderHumanHand();
        renderTable();
    }

    @FXML
    private void handleDrawCard() {
        Player human = game.getPlayers().get(0);
        Card drawnCard = game.drawCardForPlayer(human);

        messageLabel.setText("Tomaste " + drawnCard.getDisplayText() + ".");
        drawButton.setDisable(true);
        awaitingHumanDraw = false;

        game.advanceTurn();
        renderHumanHand();
        renderDeckCount();
        triggerMachineTurnIfNeeded();
    }

    private void triggerMachineTurnIfNeeded() {
        Player current = game.getCurrentPlayer();
        if (current instanceof HumanPlayer) return;

        Thread machineTurnThread = new Thread(() -> {
            try {
                int delayMillis = 2000 + new Random().nextInt(2001); // 2 a 4 s
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Card chosen = current.selectCard(game.getTable());
            Platform.runLater(() -> {
                if (chosen != null) {
                    game.playCard(current, chosen);
                    Card drawnCard = game.drawCardForPlayer(current);
                    messageLabel.setText(current.getName() + " jugó " + chosen.getDisplayText()
                            + " y tomó una carta del mazo.");
                }
                game.advanceTurn();
                renderMachineHands();
                renderTable();
                renderDeckCount();
                triggerMachineTurnIfNeeded();
            });
        });
        machineTurnThread.setDaemon(true);
        machineTurnThread.start();
    }
}
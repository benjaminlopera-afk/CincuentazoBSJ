package com.example.cincuentazo_bsj.controllers;

import com.example.cincuentazo_bsj.exceptions.NoPlayableCardException;
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

    @FXML
    private VBox machinesBox;
    @FXML
    private HBox humanHandBox;
    @FXML
    private Label tableCardLabel;
    @FXML
    private Label deckCountLabel;
    @FXML
    private Label tableSumLabel;
    @FXML
    private Button drawButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Label timeLabel;

    private Game game;
    private boolean awaitingHumanDraw = false;

    private volatile boolean gameRunning = true;
    private final long gameStartMillis = System.currentTimeMillis();

    public void setGame(Game game) {
        this.game = game;
        game.startGame();
        renderMachineHands();
        renderHumanHand();
        renderTable();
        renderDeckCount();
        messageLabel.setText("Selecciona una carta para empezar a jugar.");
        startClockThread();
        beginTurn();
    }

    private void beginTurn() {
        if (game.isGameOver()) {
            endGame();
            return;
        }

        Player current = game.getCurrentPlayer();
        try {
            game.validateHasPlayableCard(current);
        } catch (NoPlayableCardException ex) {
            handleElimination(current, ex);
            return;
        }

        renderHumanHand();
        if (current instanceof HumanPlayer) {
            messageLabel.setText("Es tu turno. Selecciona una carta.");
        } else {
            triggerMachineTurn(current);
        }
    }

    private void handleElimination(Player player, NoPlayableCardException ex) {
        game.eliminatePlayer(player);
        messageLabel.setText(ex.getMessage());
        renderMachineHands();
        renderHumanHand();
        renderDeckCount();

        if (game.isGameOver()) {
            endGame();
            return;
        }
        game.advanceTurn();
        beginTurn();
    }

    private void endGame() {
        gameRunning = false;
        Player winner = game.getWinner();
        String text = winner != null
                ? "¡" + winner.getName() + " ha ganado la partida!"
                : "La partida ha finalizado.";
        messageLabel.setText(text);
        drawButton.setDisable(true);
        humanHandBox.getChildren().forEach(node -> node.setOnMouseClicked(null));
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

        renderHumanHand();
        renderDeckCount();

        game.advanceTurn();
        beginTurn();
    }

    private void triggerMachineTurn(Player current) {
        Thread machineTurnThread = new Thread(() -> {
            try {
                int delayMillis = 2000 + new Random().nextInt(2001);
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Card chosen = current.selectCard(game.getTable());
            Platform.runLater(() -> {
                if (chosen != null) {
                    game.playCard(current, chosen);
                    Card drawnCard = game.drawCardForPlayer(current);
                    messageLabel.setText(current.getName() + " jugó " + chosen.getDisplayText()
                            + " y tomó una carta del mazo.");
                }
                renderMachineHands();
                renderTable();
                renderDeckCount();
                game.advanceTurn();
                beginTurn();
            });
        });
        machineTurnThread.setDaemon(true);
        machineTurnThread.start();
    }

    private void startClockThread() {
        Thread clockThread = new Thread(() -> {
            while (gameRunning) {
                long elapsedSeconds = (System.currentTimeMillis() - gameStartMillis) / 1000;
                Platform.runLater(() -> timeLabel.setText(formatElapsedTime(elapsedSeconds)));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        clockThread.setDaemon(true);
        clockThread.start();
    }

    private String formatElapsedTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
package com.example.cincuentazo_bsj.controllers;

import com.example.cincuentazo_bsj.applications.CincuentazoApplication;
import com.example.cincuentazo_bsj.exceptions.NoPlayableCardException;
import com.example.cincuentazo_bsj.model.Card;
import com.example.cincuentazo_bsj.model.Game;
import com.example.cincuentazo_bsj.model.HumanPlayer;
import com.example.cincuentazo_bsj.model.Player;
import com.example.cincuentazo_bsj.utils.Paths;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.Random;

/**
 * Controlador de la vista de juego. Refleja en pantalla el estado del
 * modelo {@link Game}: manos de los jugadores, carta y suma de la mesa,
 * mazo restante y cronómetro. Maneja la interacción del jugador humano
 * (jugar carta, elegir valor del As, tomar carta del mazo), el turno
 * automático de las máquinas en un hilo separado, y los anuncios de
 * eliminación y fin de partida.
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
    private Label eventLabel;
    @FXML
    private Label timeLabel;

    @FXML
    private Button restartButton;

    private Game game;

    /** true cuando el humano ya jugó su carta y le falta tomar del mazo para cerrar el turno. */
    private boolean awaitingHumanDraw = false;

    /** Controla si el hilo del cronómetro sigue corriendo. */
    private volatile boolean gameRunning = true;

    private final long gameStartMillis = System.currentTimeMillis();

    /** Pausa entre eliminaciones o cambios de turno, para que cada mensaje se alcance a leer. */
    private static final Duration ELIMINATION_PAUSE = Duration.seconds(1.8);
    private static final Duration TURN_MESSAGE_PAUSE = Duration.seconds(1.5);

    /**
     * Recibe la partida ya creada desde {@link StartController}, la inicializa
     * (reparto de cartas, HU-2) y arranca el ciclo de turnos y el cronómetro.
     *
     * @param game partida a jugar.
     */
    public void setGame(Game game) {
        this.game = game;
        game.startGame();
        renderMachineHands();
        renderHumanHand();
        renderTable();
        renderDeckCount();
        messageLabel.setText("Selecciona una carta para empezar a jugar.");
        eventLabel.setText("");
        startClockThread();
        beginTurn();
    }

    /**
     * Evalúa el turno del jugador actual: si la partida ya terminó, la
     * finaliza; si el jugador no tiene carta jugable, dispara su eliminación
     * (HU-5); si es un humano, espera su interacción; si es máquina, dispara
     * su jugada automática en un hilo separado.
     */
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

    /**
     * Elimina al jugador sin carta jugable (HU-5), muestra el motivo en
     * pantalla y espera una pausa antes de continuar, para que la
     * eliminación sea visible aunque varios jugadores caigan en cadena
     * en el mismo turno.
     *
     * @param player jugador a eliminar.
     * @param ex     excepción con el mensaje descriptivo de la eliminación.
     */
    private void handleElimination(Player player, NoPlayableCardException ex) {
        game.eliminatePlayer(player);
        eventLabel.setText("❌ " + ex.getMessage());
        renderMachineHands();
        renderHumanHand();
        renderDeckCount();

        if (game.isGameOver()) {
            PauseTransition pause = new PauseTransition(ELIMINATION_PAUSE);
            pause.setOnFinished(e -> endGame());
            pause.play();
            return;
        }

        game.advanceTurn();
        PauseTransition pause = new PauseTransition(ELIMINATION_PAUSE);
        pause.setOnFinished(e -> beginTurn());
        pause.play();
    }

    /**
     * Finaliza la partida (HU-6): detiene el cronómetro, anuncia al ganador,
     * bloquea la interacción con la mano del jugador humano y habilita el
     * botón para volver a la pantalla de inicio.
     */
    private void endGame() {
        gameRunning = false;
        Player winner = game.getWinner();
        String text = winner != null
                ? "🏆 " + winner.getName() + " ganó la partida: fue el último jugador en quedar en juego."
                : "La partida ha finalizado.";
        eventLabel.setText(text);
        messageLabel.setText("");
        drawButton.setDisable(true);
        humanHandBox.getChildren().forEach(node -> node.setOnMouseClicked(null));

        restartButton.setVisible(true);
        restartButton.setManaged(true);
    }

    /**
     * Dibuja las manos de los jugadores máquina boca abajo, marcando con
     * una etiqueta a los que ya fueron eliminados.
     */
    private void renderMachineHands() {
        machinesBox.getChildren().clear();
        for (Player player : game.getPlayers()) {
            if (player instanceof HumanPlayer) continue;

            Label nameLabel = new Label(player.getName() + (player.isEliminated() ? " (eliminado)" : ""));
            nameLabel.getStyleClass().add("player-name-label");
            if (player.isEliminated()) {
                nameLabel.getStyleClass().add("player-eliminated-label");
            }

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

    /**
     * Dibuja la mano del jugador humano boca arriba, habilitando el clic
     * únicamente sobre las cartas jugables cuando es su turno.
     */
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

    /**
     * Actualiza la carta visible en la mesa y la suma actual.
     */
    private void renderTable() {
        Card topCard = game.getTable().getPlayedCards()
                .get(game.getTable().getPlayedCards().size() - 1);
        tableCardLabel.setText(topCard.getDisplayText());
        tableCardLabel.getStyleClass().removeAll("card-face-red", "card-face-black");
        tableCardLabel.getStyleClass().add(topCard.isRedSuit() ? "card-face-red" : "card-face-black");
        tableSumLabel.setText("Suma: " + game.getTable().getSum());
    }

    /**
     * Actualiza el contador de cartas restantes en el mazo.
     */
    private void renderDeckCount() {
        deckCountLabel.setText("Mazo: " + game.getDeck().size());
    }

    /**
     * Se dispara cuando el jugador humano hace clic en una carta jugable
     * de su mano. Si es un As con ambas opciones válidas, primero pregunta
     * qué valor usar; en cualquier otro caso, juega con el valor automático.
     *
     * @param card carta seleccionada.
     */
    private void onHumanCardSelected(Card card) {
        if (card.requiresAceChoice(game.getTable().getSum())) {
            showAceChoiceDialog(card);
        } else {
            playHumanCard(card, card.getBestValueFor(game.getTable().getSum()));
        }
    }

    /**
     * Muestra una alerta para que el jugador humano elija si el As jugado
     * suma 1 o 10, tal como indica el enunciado ("según convenga").
     *
     * @param card carta de As jugada.
     */
    private void showAceChoiceDialog(Card card) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Elige el valor del As");
        alert.setHeaderText(null);
        alert.setContentText("Jugaste " + card.getDisplayText()
                + ". Suma actual: " + game.getTable().getSum() + ". ¿Con qué valor quieres jugarlo?");

        ButtonType optionTen = new ButtonType("Sumar 10");
        ButtonType optionOne = new ButtonType("Sumar 1");
        alert.getButtonTypes().setAll(optionTen, optionOne);

        alert.showAndWait().ifPresent(choice -> {
            int chosenValue = (choice == optionTen) ? 10 : 1;
            playHumanCard(card, chosenValue);
        });
    }

    /**
     * Aplica la jugada del humano con el valor ya determinado (automático,
     * o elegido manualmente en el caso del As), deja el turno abierto a la
     * espera de que tome una carta del mazo, y refresca la vista.
     *
     * @param card  carta jugada.
     * @param value valor aplicado a la suma de la mesa.
     */
    private void playHumanCard(Card card, int value) {
        game.playCard(game.getPlayers().get(0), card, value);
        awaitingHumanDraw = true;

        messageLabel.setText("Jugaste " + card.getDisplayText()
                + " (" + (value > 0 ? "+" : "") + value + "). Ahora toma una carta del mazo.");
        drawButton.setDisable(false);

        renderHumanHand();
        renderTable();
    }

    /**
     * Se dispara cuando el jugador humano hace clic en "Tomar carta del
     * mazo" después de haber jugado (HU-4). Cierra el turno y avanza al
     * siguiente jugador.
     */
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

    /**
     * Vuelve a la pantalla de inicio para permitir iniciar una nueva partida,
     * usando {@link CincuentazoApplication#setScene} en vez de manejar el
     * cambio de escena manualmente.
     */
    @FXML
    private void handleRestart() {
        CincuentazoApplication.setScene(Paths.START_VIEW, "Cincuentazo");
    }


    /**
     * Ejecuta el turno de un jugador máquina en un hilo separado del hilo
     * de JavaFX: espera entre 2 y 4 segundos (simulando que "piensa"),
     * selecciona y juega una carta, toma una del mazo, y vuelve a
     * sincronizar la actualización de la interfaz con {@link Platform#runLater}.
     *
     * @param current jugador máquina al que le corresponde el turno.
     */
    private void triggerMachineTurn(Player current) {
        Thread machineTurnThread = new Thread(() -> {
            try {
                int delayMillis = 2000 + new Random().nextInt(2001); // 2 a 4 s
                Thread.sleep(delayMillis);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            Card chosen = current.selectCard(game.getTable());
            Platform.runLater(() -> {
                if (chosen != null) {
                    int value = chosen.getBestValueFor(game.getTable().getSum());
                    game.playCard(current, chosen, value);
                    game.drawCardForPlayer(current);
                    messageLabel.setText(current.getName() + " jugó " + chosen.getDisplayText()
                            + " (" + (value > 0 ? "+" : "") + value + ") y tomó una carta del mazo.");
                }
                renderMachineHands();
                renderTable();
                renderDeckCount();
                game.advanceTurn();

                PauseTransition pause = new PauseTransition(TURN_MESSAGE_PAUSE);
                pause.setOnFinished(e -> beginTurn());
                pause.play();
            });
        });
        machineTurnThread.setDaemon(true);
        machineTurnThread.start();
    }

    /**
     * Arranca un segundo hilo, independiente del turno de las máquinas,
     * que actualiza el cronómetro de la partida cada segundo mientras
     * {@link #gameRunning} sea true.
     */
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

    /**
     * Formatea segundos transcurridos como MM:SS.
     *
     * @param totalSeconds segundos totales transcurridos.
     * @return texto formateado, ej. "02:35".
     */
    private String formatElapsedTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
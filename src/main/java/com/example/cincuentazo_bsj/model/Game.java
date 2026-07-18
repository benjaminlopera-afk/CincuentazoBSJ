package com.example.cincuentazo_bsj.model;

import com.example.cincuentazo_bsj.exceptions.CincuentazoException;
import com.example.cincuentazo_bsj.exceptions.NoPlayableCardException;

import java.util.ArrayList;
import java.util.List;

/**
 * Controla el estado y las reglas generales de una partida de Cincuentazo:
 * inicialización de jugadores, reparto de cartas, validación y ejecución
 * de jugadas, manejo del mazo, eliminación de jugadores y condición de victoria.
 */
public class Game {
    private static final int INITIAL_HAND_SIZE = 4;

    private final Deck deck;
    private final Table table;
    private final List<Player> players;
    private int currentPlayerIndex;

    /**
     * Crea una partida nueva con mazo y mesa vacíos, sin jugadores todavía.
     */
    public Game() {
        this.deck = new Deck();
        this.table = new Table();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    /**
     * Crea la lista de jugadores de la partida (HU-1): siempre un jugador
     * humano en la posición 0, seguido de las máquinas indicadas.
     *
     * @param machineCount cantidad de jugadores máquina (1, 2 o 3).
     */
    public void initializePlayers(int machineCount) {
        players.clear();
        players.add(new HumanPlayer("Jugador"));
        for (int i = 1; i <= machineCount; i++) {
            players.add(new MachinePlayer("Máquina " + i));
        }
        currentPlayerIndex = 0;
    }

    /**
     * Prepara la partida (HU-2): arma y baraja el mazo, reparte 4 cartas
     * a cada jugador y coloca la carta inicial boca arriba en la mesa,
     * estableciendo la suma inicial según el rango de esa carta.
     */
    public void startGame() {
        deck.buildFullDeck();
        deck.shuffle();

        for (Player player : players) {
            for (int i = 0; i < INITIAL_HAND_SIZE; i++) {
                player.getHand().add(deck.drawCard());
            }
        }

        Card initialCard = deck.drawCard();
        initialCard.setFaceUp(true);
        table.getPlayedCards().add(initialCard);
        table.setSum(initialCard.getInitialTableValue());
    }

    /**
     * @return el jugador al que le corresponde jugar en este momento.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Indica si el jugador tiene al menos una carta jugable en su mano,
     * según la suma actual de la mesa.
     *
     * @param player jugador a evaluar.
     * @return true si existe alguna carta jugable.
     */
    public boolean hasPlayableCard(Player player) {
        return player.getHand().stream()
                .anyMatch(card -> card.getBestValueFor(table.getSum()) != Integer.MIN_VALUE);
    }

    /**
     * Valida que el jugador tenga una carta jugable; si no la tiene, lanza
     * una excepción marcada que el controlador debe manejar explícitamente
     * (punto donde se dispara la eliminación, HU-5).
     *
     * @param player jugador a validar.
     * @throws NoPlayableCardException si ninguna carta de su mano es jugable.
     */
    public void validateHasPlayableCard(Player player) throws NoPlayableCardException {
        if (!hasPlayableCard(player)) {
            throw new NoPlayableCardException(
                    player.getName() + " no tiene cartas jugables y será eliminado. ");
        }
    }

    /**
     * Ejecuta la jugada de una carta (HU-3), con el valor explícito elegido
     * para esa jugada. Para cartas con un único valor posible, ese valor
     * debe coincidir con el calculado por {@link Card#getBestValueFor(int)};
     * para el As, permite indicar 1 o 10 según lo que el jugador elija.
     *
     * @param player      jugador que realiza la jugada.
     * @param card        carta seleccionada de la mano del jugador.
     * @param chosenValue valor a aplicar a la suma de la mesa.
     * @throws CincuentazoException si el valor no es una opción válida para
     *                               la carta, o si excede la suma de 50 en la mesa.
     */
    public void playCard(Player player, Card card, int chosenValue) {
        boolean isValidOption = false;
        for (int possible : card.getPossibleValues()) {
            if (possible == chosenValue) {
                isValidOption = true;
                break;
            }
        }
        if (!isValidOption || table.getSum() + chosenValue > 50) {
            throw new CincuentazoException(
                    "El valor " + chosenValue + " no es válido para la carta " + card.getDisplayText() + ".");
        }
        player.getHand().remove(card);
        card.setFaceUp(true);
        table.getPlayedCards().add(card);
        table.setSum(table.getSum() + chosenValue);
    }

    /**
     * Toma una carta del mazo y la agrega a la mano del jugador (HU-4).
     * Si el mazo está vacío, primero lo rearma reciclando las cartas de
     * la mesa (excepto la última jugada).
     *
     * @param player jugador que toma la carta.
     * @return la carta que fue tomada del mazo.
     */
    public Card drawCardForPlayer(Player player) {
        if (deck.isEmpty()) {
            reshuffleDeckFromTable();
        }
        Card drawnCard = deck.drawCard();
        player.getHand().add(drawnCard);
        return drawnCard;
    }

    /**
     * Recicla las cartas jugadas en la mesa (todas menos la última) de
     * vuelta al mazo cuando este se queda sin cartas. La suma de la mesa
     * no se modifica, tal como indica el enunciado.
     *
     * @throws CincuentazoException si no hay suficientes cartas en la mesa para reciclar.
     */
    private void reshuffleDeckFromTable() {
        if (table.getPlayedCards().size() <= 1) {
            throw new CincuentazoException("No hay suficientes cartas en la mesa para rearmar el mazo.");
        }
        Card lastPlayed = table.getPlayedCards().remove(table.getPlayedCards().size() - 1);
        deck.getCards().addAll(table.getPlayedCards());
        table.getPlayedCards().clear();
        table.getPlayedCards().add(lastPlayed);
        deck.shuffle();
    }

    /**
     * Elimina a un jugador de la partida (HU-5): lo marca como eliminado
     * y envía las cartas de su mano al final del mazo, quedando disponibles
     * para ser tomadas por otro jugador.
     *
     * @param player jugador a eliminar.
     */
    public void eliminatePlayer(Player player) {
        player.setEliminated(true);
        deck.addCardsToBottom(player.getHand());
        player.getHand().clear();
    }

    /**
     * @return la lista de jugadores que aún no han sido eliminados.
     */
    public List<Player> getActivePlayers() {
        return players.stream().filter(p -> !p.isEliminated()).toList();
    }

    /**
     * @return true si solo queda un jugador activo o menos (HU-6, fin del juego).
     */
    public boolean isGameOver() {
        return getActivePlayers().size() <= 1;
    }

    /**
     * @return el único jugador activo si la partida terminó, o {@code null}
     *         si todavía hay más de uno en juego.
     */
    public Player getWinner() {
        List<Player> active = getActivePlayers();
        return active.size() == 1 ? active.get(0) : null;
    }

    /**
     * Avanza el turno al siguiente jugador activo de la lista, de forma
     * circular, saltando a los jugadores ya eliminados.
     */
    public void advanceTurn() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (players.get(currentPlayerIndex).isEliminated());
    }

    public Deck getDeck() { return deck; }
    public Table getTable() { return table; }
    public List<Player> getPlayers() { return players; }
}
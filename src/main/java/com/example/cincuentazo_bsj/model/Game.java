package com.example.cincuentazo_bsj.model;

import com.example.cincuentazo_bsj.exceptions.CincuentazoException;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int INITIAL_HAND_SIZE = 4;

    private final Deck deck;
    private final Table table;
    private final List<Player> players;
    private int currentPlayerIndex;

    public Game() {
        this.deck = new Deck();
        this.table = new Table();
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public void initializePlayers(int machineCount) {
        players.clear();
        players.add(new HumanPlayer("Jugador"));
        for (int i = 1; i <= machineCount; i++) {
            players.add(new MachinePlayer("Máquina " + i));
        }
        currentPlayerIndex = 0;
    }

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

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean hasPlayableCard(Player player) {
        return player.getHand().stream()
                .anyMatch(card -> card.getBestValueFor(table.getSum()) != Integer.MIN_VALUE);
    }

    public void playCard(Player player, Card card) {
        int value = card.getBestValueFor(table.getSum());
        if (value == Integer.MIN_VALUE) {
            throw new CincuentazoException(
                    "La carta " + card.getDisplayText() + " excede la suma de 50 en la mesa.");
        }
        player.getHand().remove(card);
        card.setFaceUp(true);
        table.getPlayedCards().add(card);
        table.setSum(table.getSum() + value);
    }

    public void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public Deck getDeck() { return deck; }
    public Table getTable() { return table; }
    public List<Player> getPlayers() { return players; }
}
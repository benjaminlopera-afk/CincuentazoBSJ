package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private static final int INITIAL_HAND_SIZE = 4;

    private final Deck deck;
    private final Table table;
    private final List<Player> players;

    public Game() {
        this.deck = new Deck();
        this.table = new Table();
        this.players = new ArrayList<>();
    }

    public void initializePlayers(int machineCount) {
        players.clear();
        players.add(new HumanPlayer("Jugador"));
        for (int i = 1; i <= machineCount; i++) {
            players.add(new MachinePlayer("Máquina " + i));
        }
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

    public Deck getDeck() { return deck; }
    public Table getTable() { return table; }
    public List<Player> getPlayers() { return players; }
}
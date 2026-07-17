package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

public class Game {
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

    public Deck getDeck() { return deck; }
    public Table getTable() { return table; }
    public List<Player> getPlayers() { return players; }
}
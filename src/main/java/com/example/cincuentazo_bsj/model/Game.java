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

    public Deck getDeck() { return deck; }
    public Table getTable() { return table; }
    public List<Player> getPlayers() { return players; }
}
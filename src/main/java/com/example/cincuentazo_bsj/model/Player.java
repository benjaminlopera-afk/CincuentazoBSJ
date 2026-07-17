package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    protected final String name;
    protected final List<Card> hand;
    protected boolean eliminated;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    public String getName() { return name; }
    public List<Card> getHand() { return hand; }
    public boolean isEliminated() { return eliminated; }
    public void setEliminated(boolean eliminated) { this.eliminated = eliminated; }

    public abstract Card selectCard(Table table);
}
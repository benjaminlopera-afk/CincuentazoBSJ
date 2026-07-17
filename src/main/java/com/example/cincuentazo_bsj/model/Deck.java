package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() { return cards; }
}
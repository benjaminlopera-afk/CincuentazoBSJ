package com.example.cincuentazo_bsj.model;

import com.example.cincuentazo_bsj.exceptions.CincuentazoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private static final String[] SUITS = {"Corazones", "Diamantes", "Treboles", "Picas"};
    private static final String[] RANKS = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};

    private final List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() { return cards; }

    public void buildFullDeck() {
        cards.clear();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new CincuentazoException("No hay cartas disponibles en el mazo.");
        }
        return cards.remove(cards.size() - 1);
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
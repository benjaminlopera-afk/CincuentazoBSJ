package com.example.cincuentazo_bsj.model;

import com.example.cincuentazo_bsj.exceptions.CincuentazoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa el mazo de cartas boca abajo, del cual los jugadores toman cartas.
 * Se encarga de construir la baraja completa, barajarla, repartir cartas
 * y recibir de vuelta las cartas de jugadores eliminados.
 */
public class Deck {
    private static final String[] SUITS = {"Corazones", "Diamantes", "Treboles", "Picas"};
    private static final String[] RANKS = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};

    private final List<Card> cards;

    /**
     * Crea un mazo vacío. Debe llamarse a {@link #buildFullDeck()} antes de usarlo.
     */
    public Deck() {
        this.cards = new ArrayList<>();
    }

    public List<Card> getCards() { return cards; }

    /**
     * Construye las 52 cartas de la baraja estándar (4 palos x 13 rangos),
     * reemplazando cualquier contenido previo del mazo.
     */
    public void buildFullDeck() {
        cards.clear();
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Baraja aleatoriamente las cartas actuales del mazo.
     */
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Toma la carta superior del mazo.
     *
     * @return la carta tomada.
     * @throws CincuentazoException si el mazo está vacío.
     */
    public Card drawCard() {
        if (cards.isEmpty()) {
            throw new CincuentazoException("No hay cartas disponibles en el mazo.");
        }
        return cards.remove(cards.size() - 1);
    }

    /**
     * Agrega cartas al fondo del mazo (usado cuando un jugador es eliminado,
     * según el enunciado: "las cartas del jugador eliminado deben enviarse
     * al final del mazo").
     *
     * @param cardsToAdd cartas a devolver al mazo.
     */
    public void addCardsToBottom(List<Card> cardsToAdd) {
        cards.addAll(0, cardsToAdd);
    }

    /**
     * @return la cantidad de cartas restantes en el mazo.
     */
    public int size() {
        return cards.size();
    }

    /**
     * @return true si el mazo no tiene cartas disponibles.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
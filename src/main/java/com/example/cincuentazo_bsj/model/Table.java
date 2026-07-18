package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa la mesa de juego: la suma acumulada y la pila de cartas
 * jugadas boca arriba, una encima de otra.
 */
public class Table {
    private int sum;
    private final List<Card> playedCards;

    /**
     * Crea una mesa vacía con suma inicial en 0.
     */
    public Table() {
        this.sum = 0;
        this.playedCards = new ArrayList<>();
    }

    public int getSum() { return sum; }
    public void setSum(int sum) { this.sum = sum; }

    /**
     * @return la lista de cartas jugadas en la mesa, en orden. La última
     *         posición es siempre la carta visible actualmente.
     */
    public List<Card> getPlayedCards() { return playedCards; }
}
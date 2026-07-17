package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private int sum;
    private final List<Card> playedCards;

    public Table() {
        this.sum = 0;
        this.playedCards = new ArrayList<>();
    }

    public int getSum() { return sum; }
    public void setSum(int sum) { this.sum = sum; }
    public List<Card> getPlayedCards() { return playedCards; }
}
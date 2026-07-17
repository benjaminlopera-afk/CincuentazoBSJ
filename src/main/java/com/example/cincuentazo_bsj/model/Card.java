package com.example.cincuentazo_bsj.model;

public class Card {
    private String suit;
    private String rank;
    private boolean faceUp;

    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
    }

    public String getSuit() { return suit; }
    public String getRank() { return rank; }
    public boolean isFaceUp() { return faceUp; }
    public void setFaceUp(boolean faceUp) { this.faceUp = faceUp; }
}
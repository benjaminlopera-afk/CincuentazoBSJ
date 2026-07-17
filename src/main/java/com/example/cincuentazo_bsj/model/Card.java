package com.example.cincuentazo_bsj.model;

public class Card {
    private final String suit;
    private final String rank;
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

    public int getInitialTableValue() {
        return switch (rank) {
            case "9" -> 0;
            case "A" -> 1;
            case "J", "Q", "K" -> -10;
            default -> Integer.parseInt(rank);
        };
    }

    public String getDisplayText() {
        String symbol = switch (suit) {
            case "Corazones" -> "♥";
            case "Diamantes" -> "♦";
            case "Treboles" -> "♣";
            case "Picas" -> "♠";
            default -> "";
        };
        return rank + symbol;
    }

    public boolean isRedSuit() {
        return suit.equals("Corazones") || suit.equals("Diamantes");
    }
}
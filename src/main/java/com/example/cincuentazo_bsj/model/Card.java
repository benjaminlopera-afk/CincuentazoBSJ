package com.example.cincuentazo_bsj.model;

/**
 * Representa una carta de la baraja utilizada en Cincuentazo.
 * Cada carta pertenece a un palo y un rango, y sabe calcular su propio
 * aporte a la suma de la mesa según las reglas del juego.
 */
public class Card {
    private final String suit;
    private final String rank;
    private boolean faceUp;

    /**
     * Crea una carta con el palo y rango indicados. Se crea boca abajo por defecto.
     *
     * @param suit palo de la carta (Corazones, Diamantes, Treboles, Picas).
     * @param rank rango de la carta (A, 2-10, J, Q, K).
     */
    public Card(String suit, String rank) {
        this.suit = suit;
        this.rank = rank;
        this.faceUp = false;
    }

    public String getSuit() { return suit; }
    public String getRank() { return rank; }
    public boolean isFaceUp() { return faceUp; }
    public void setFaceUp(boolean faceUp) { this.faceUp = faceUp; }

    /**
     * Calcula el valor con el que esta carta inicia la suma de la mesa,
     * según la sección "Otras consideraciones" del enunciado:
     * 9 no suma ni resta (0), A suma 1, J/Q/K restan 10, el resto suma su número.
     *
     * @return el valor inicial que aporta la carta a la suma de la mesa.
     */
    public int getInitialTableValue() {
        return switch (rank) {
            case "9" -> 0;
            case "A" -> 1;
            case "J", "Q", "K" -> -10;
            default -> Integer.parseInt(rank);
        };
    }

    /**
     * Valores posibles que puede aportar esta carta a la suma de la mesa
     * al jugarla. Para la mayoría es un único valor; el As tiene dos
     * opciones (1 o 10) porque el enunciado indica "según convenga".
     *
     * @return arreglo con los valores posibles de esta carta.
     */
    public int[] getPossibleValues() {
        return switch (rank) {
            case "9" -> new int[]{0};
            case "A" -> new int[]{10, 1};
            case "J", "Q", "K" -> new int[]{-10};
            default -> new int[]{Integer.parseInt(rank)};
        };
    }

    /**
     * Devuelve el mejor valor con el que esta carta puede jugarse sin que
     * la suma de la mesa exceda 50. Para el As, prioriza 10 sobre 1.
     *
     * @param currentSum suma actual de la mesa.
     * @return el valor a aplicar, o {@code Integer.MIN_VALUE} si ninguno de
     *         sus valores posibles es válido (carta no jugable en este momento).
     */
    public int getBestValueFor(int currentSum) {
        for (int value : getPossibleValues()) {
            if (currentSum + value <= 50) {
                return value;
            }
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Indica si esta carta es un As con ambas opciones de valor (1 o 10)
     * disponibles sin exceder 50, caso en el que el jugador debe elegir
     * "según convenga", tal como indica el enunciado.
     *
     * @param currentSum suma actual de la mesa.
     * @return true si el jugador debe elegir entre 1 y 10 para esta jugada.
     */
    public boolean requiresAceChoice(int currentSum) {
        if (!rank.equals("A")) return false;
        return (currentSum + 10 <= 50) && (currentSum + 1 <= 50);
    }

    /**
     * Texto corto para mostrar en la interfaz (ej. "A♥", "10♠").
     *
     * @return representación visual de la carta.
     */
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

    /**
     * Indica si la carta es de palo rojo (Corazones/Diamantes), usado
     * para determinar el color con el que se pinta en la interfaz.
     *
     * @return true si el palo es rojo.
     */
    public boolean isRedSuit() {
        return suit.equals("Corazones") || suit.equals("Diamantes");
    }
}
package com.example.cincuentazo_bsj.model;

public class MachinePlayer extends Player {

    public MachinePlayer(String name) {
        super(name);
    }

    @Override
    public Card selectCard(Table table) {
        for (Card card : hand) {
            if (card.getBestValueFor(table.getSum()) != Integer.MIN_VALUE) {
                return card;
            }
        }
        return null; // Ninguna carta jugable -> se resuelve en HU-5 (eliminación)
    }
}
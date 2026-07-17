package com.example.cincuentazo_bsj.model;

public class HumanPlayer extends Player {

    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public Card selectCard(Table table) {
        return null; // Se implementa en HU-3
    }
}
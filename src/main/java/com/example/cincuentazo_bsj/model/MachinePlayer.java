package com.example.cincuentazo_bsj.model;

public class MachinePlayer extends Player {

    public MachinePlayer(String name) {
        super(name);
    }

    @Override
    public Card selectCard(Table table) {
        return null; // Se implementa en HU-3
    }
}
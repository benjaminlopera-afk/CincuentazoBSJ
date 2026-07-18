package com.example.cincuentazo_bsj.model;

/**
 * Jugador controlado por la máquina. Selecciona automáticamente la primera
 * carta de su mano que sea jugable sin exceder la suma de 50 en la mesa.
 */
public class MachinePlayer extends Player {

    /**
     * Crea un jugador máquina con el nombre indicado.
     *
     * @param name nombre visible del jugador.
     */
    public MachinePlayer(String name) {
        super(name);
    }

    /**
     * Recorre la mano en orden y devuelve la primera carta cuyo valor
     * (calculado con {@link Card#getBestValueFor(int)}) mantenga la suma
     * de la mesa dentro del límite de 50.
     *
     * @param table estado actual de la mesa.
     * @return la primera carta jugable encontrada, o {@code null} si ninguna
     *         carta de la mano puede jugarse (el jugador quedará eliminado en HU-5).
     */
    @Override
    public Card selectCard(Table table) {
        for (Card card : hand) {
            if (card.getBestValueFor(table.getSum()) != Integer.MIN_VALUE) {
                return card;
            }
        }
        return null;
    }
}
package com.example.cincuentazo_bsj.model;

/**
 * Jugador controlado por una persona. La selección de carta no se resuelve
 * en este método: la dispara directamente la interacción del usuario en
 * {@code GameController} (clic sobre una carta de su mano).
 */
public class HumanPlayer extends Player {

    /**
     * Crea un jugador humano con el nombre indicado.
     *
     * @param name nombre visible del jugador.
     */
    public HumanPlayer(String name) {
        super(name);
    }

    @Override
    public Card selectCard(Table table) {
        return null; // La selección del humano la dispara la interfaz, no este método.
    }
}
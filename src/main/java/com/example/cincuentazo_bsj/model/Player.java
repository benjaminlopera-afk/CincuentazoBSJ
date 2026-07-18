package com.example.cincuentazo_bsj.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase base abstracta para cualquier jugador de la partida (humano o máquina).
 * Mantiene el nombre, la mano de cartas y el estado de eliminación, y define
 * el contrato para seleccionar una carta durante el turno.
 */
public abstract class Player {
    protected final String name;
    protected final List<Card> hand;
    protected boolean eliminated;

    /**
     * Crea un jugador con el nombre indicado, mano vacía y sin eliminar.
     *
     * @param name nombre visible del jugador.
     */
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.eliminated = false;
    }

    public String getName() { return name; }
    public List<Card> getHand() { return hand; }
    public boolean isEliminated() { return eliminated; }
    public void setEliminated(boolean eliminated) { this.eliminated = eliminated; }

    /**
     * Selecciona la carta que este jugador jugará en su turno actual.
     * En {@link HumanPlayer} no se usa (la selección la dispara la interfaz);
     * en {@link MachinePlayer} implementa la decisión automática.
     *
     * @param table estado actual de la mesa, usado para evaluar qué cartas son jugables.
     * @return la carta elegida, o {@code null} si ninguna es jugable.
     */
    public abstract Card selectCard(Table table);
}
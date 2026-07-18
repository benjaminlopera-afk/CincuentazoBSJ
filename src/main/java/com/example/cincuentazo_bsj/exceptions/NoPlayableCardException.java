package com.example.cincuentazo_bsj.exceptions;

/**
 * Excepción marcada (checked) que se lanza cuando un jugador no tiene
 * ninguna carta jugable según la suma actual de la mesa.
 * Al ser checked, obliga a manejarla explícitamente en el controlador,
 * que es justo el punto donde se dispara la eliminación del jugador (HU-5).
 */
public class NoPlayableCardException extends Exception {

    /**
     * Crea la excepción con un mensaje descriptivo del motivo de eliminación.
     *
     * @param message detalle del error.
     */
    public NoPlayableCardException(String message) {
        super(message);
    }
}
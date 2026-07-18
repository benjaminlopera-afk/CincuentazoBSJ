package com.example.cincuentazo_bsj.exceptions;

/**
 * Excepción no marcada (unchecked) que representa una violación de las
 * reglas del juego, como intentar jugar una carta que excede la suma de 50
 * o tomar una carta cuando no hay ninguna disponible.
 */
public class CincuentazoException extends RuntimeException {

    /**
     * Crea la excepción con un mensaje descriptivo del error de regla.
     *
     * @param message detalle del error.
     */
    public CincuentazoException(String message) {
        super(message);
    }
}
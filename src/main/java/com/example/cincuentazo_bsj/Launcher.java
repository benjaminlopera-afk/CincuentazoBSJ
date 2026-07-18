package com.example.cincuentazo_bsj;

import com.example.cincuentazo_bsj.applications.CincuentazoApplication;

/**
 * Clase lanzadora separada de {@code CincuentazoApplication}, usada para
 * evitar problemas de classpath al ejecutar una aplicación JavaFX
 * empaquetada como un .jar sin el módulo de JavaFX explícito.
 */
public class Launcher {

    /**
     * Delega el arranque de la aplicación a {@link CincuentazoApplication#main(String[])}.
     *
     * @param args argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        CincuentazoApplication.main(args);
    }
}
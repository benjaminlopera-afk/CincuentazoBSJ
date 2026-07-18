# Cincuentazo

Juego de cartas **Cincuentazo** desarrollado en Java con JavaFX como proyecto universitario para el curso de **Fundamentos de Programación Orientada a Eventos - Universidad del Valle 2026-1**.

---

## Descripción

El objetivo del juego es ser el último jugador en permanecer en la partida. Cada jugador dispone de una mano de cuatro cartas y, en su turno, debe jugar una carta procurando que la suma acumulada de la mesa no supere el valor de 50. Durante la partida, los jugadores toman nuevas cartas del mazo, se eliminan cuando no pueden realizar una jugada válida y el juego finaliza cuando solo queda un jugador en competencia.

---

## Tecnologías

| Herramienta   | Versión |
| ------------- | ------- |
| Java          | 21      |
| JavaFX        | 21      |
| Scene Builder | -       |
| IntelliJ IDEA | -       |
| Git / GitHub  | -       |

---

## Funcionalidades

* Selección de 1, 2 o 3 jugadores máquina.
* Generación y barajado automático del mazo.
* Reparto automático de cuatro cartas a cada jugador.
* Inicialización de la carta y la suma de la mesa.
* Validación de jugadas para evitar superar el límite de 50.
* Actualización de la suma de la mesa después de cada jugada.
* Robo de cartas desde el mazo al finalizar cada turno.
* Cambio automático de turno entre los jugadores.
* Eliminación de jugadores sin movimientos válidos.
* Detección automática del jugador ganador.

---

## Estructura del proyecto

```
src/main/java/com/example/cincuentazo/
├── app/
│   └── Main.java
├── controller/
│   ├── MainController.java
│   └── GameController.java
├── model/
│   ├── Card.java
│   ├── Deck.java
│   ├── Game.java
│   ├── HumanPlayer.java
│   ├── MachinePlayer.java
│   ├── Player.java
│   └── Table.java
├── exception/
│   ├── EmptyDeckException.java
│   ├── GameException.java
│   └── InvalidMoveException.java
└── util/
    ├── CardFactory.java
    └── GameConstants.java
```

---

## Cómo ejecutar

1. Clona el repositorio.
2. Abre el proyecto en IntelliJ IDEA.
3. Espera a que Maven descargue las dependencias del proyecto.
4. Ejecuta la clase `Main.java`.

---

## Autores

* **Benjamín Lopera** - 2515144
* **Sebastián Martínez** - 2519817
* **Jhony Alexander Moreno Gómez** - 202525112

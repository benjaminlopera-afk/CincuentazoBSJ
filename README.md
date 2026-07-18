# Cincuentazo

Juego de cartas **Cincuentazo** desarrollado en **Java** con **JavaFX** como proyecto universitario para el curso de **Fundamentos de Programación Orientada a Eventos - Universidad del Valle 2026-1**.

---

## Descripción

El objetivo del juego es ser el último jugador en permanecer en la partida. Cada jugador inicia con cuatro cartas y, en su turno, debe jugar una carta procurando que la suma acumulada de la mesa no supere el valor de **50**. Al finalizar cada turno, el jugador toma una nueva carta del mazo. Cuando un jugador no posee ninguna carta válida para jugar, es eliminado. La partida termina cuando solo queda un jugador en competencia.

---

## Tecnologías

| Herramienta   | Versión |
| ------------- | ------- |
| Java          | 21      |
| JavaFX        | 21      |
| Maven         | -       |
| Scene Builder | -       |
| IntelliJ IDEA | -       |
| Git / GitHub  | -       |
| JUnit 5       | -       |

---

## Funcionalidades

* Selección de 1, 2 o 3 jugadores máquina.
* Generación y barajado automático del mazo.
* Reparto automático de cuatro cartas a cada jugador.
* Inicialización de la carta y suma inicial de la mesa.
* Validación de jugadas para evitar superar el valor de 50.
* Selección manual del valor del As (1 o 10) para el jugador humano.
* Turnos automáticos para los jugadores máquina.
* Robo de cartas desde el mazo después de cada jugada.
* Reciclaje de cartas de la mesa cuando el mazo se agota.
* Eliminación automática de jugadores sin cartas jugables.
* Detección automática del ganador.
* Cronómetro de duración de la partida.
* Consulta de las reglas del juego desde la pantalla inicial.
* Interfaz gráfica desarrollada con JavaFX y CSS.
* Pruebas unitarias para las clases principales del modelo.

---

## Estructura del proyecto

```
src
├── main
│   ├── java
│   │   └── com/example/cincuentazo_bsj
│   │       ├── applications
│   │       │   └── CincuentazoApplication.java
│   │       ├── controllers
│   │       │   ├── GameController.java
│   │       │   └── StartController.java
│   │       ├── exceptions
│   │       │   ├── CincuentazoException.java
│   │       │   └── NoPlayableCardException.java
│   │       ├── model
│   │       │   ├── Card.java
│   │       │   ├── Deck.java
│   │       │   ├── Game.java
│   │       │   ├── HumanPlayer.java
│   │       │   ├── MachinePlayer.java
│   │       │   ├── Player.java
│   │       │   └── Table.java
│   │       ├── utils
│   │       │   └── Paths.java
│   │       ├── Launcher.java
│   │       └── module-info.java
│   └── resources
│       ├── GameView.fxml
│       ├── StartView.fxml
│       └── Styles.css
│
└── test
    └── java
        └── com/example/cincuentazo_bsj/model
            ├── CardTest.java
            ├── DeckTest.java
            └── GameTest.java
```

---

## Cómo ejecutar

1. Clona el repositorio.
2. Abre el proyecto en IntelliJ IDEA.
3. Espera a que Maven descargue las dependencias.
4. Ejecuta la clase `Launcher.java`.

---

## Características implementadas

* ✔ Historia de Usuario 1: Inicio del juego.
* ✔ Historia de Usuario 2: Preparación de la partida.
* ✔ Historia de Usuario 3: Jugar una carta.
* ✔ Historia de Usuario 4: Tomar una carta del mazo.
* ✔ Historia de Usuario 5: Eliminación de jugadores.
* ✔ Historia de Usuario 6: Fin del juego.
* ✔ Hilo independiente para el cronómetro.
* ✔ Hilo independiente para los turnos automáticos de las máquinas.
* ✔ Pruebas unitarias con JUnit.

---

## Autores

* **Benjamín Lopera** - 2515144
* **Sebastián Martínez** - 2519817
* **Jhony Alexander Moreno Gómez** - 2525112

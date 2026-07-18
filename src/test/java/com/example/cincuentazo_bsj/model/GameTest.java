package com.example.cincuentazo_bsj.model;

import com.example.cincuentazo_bsj.exceptions.NoPlayableCardException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.initializePlayers(1); // 1 humano + 1 máquina
        game.startGame();
    }

    @Test
    void startGameDealsFourCardsToEachPlayer() {
        for (Player player : game.getPlayers()) {
            assertEquals(4, player.getHand().size());
        }
    }

    @Test
    void advanceTurnSkipsEliminatedPlayers() {
        Player machine = game.getPlayers().get(1);
        game.eliminatePlayer(machine);
        game.advanceTurn();
        assertEquals(game.getPlayers().get(0), game.getCurrentPlayer());
    }

    @Test
    void eliminatingSecondToLastPlayerEndsGame() {
        Player machine = game.getPlayers().get(1);
        assertFalse(game.isGameOver());
        game.eliminatePlayer(machine);
        assertTrue(game.isGameOver());
        assertEquals(game.getPlayers().get(0), game.getWinner());
    }

    @Test
    void validateHasPlayableCardThrowsWhenHandCannotBePlayed() {
        Player human = game.getPlayers().get(0);
        human.getHand().clear();
        human.getHand().add(new Card("Picas", "8"));
        game.getTable().setSum(45); // 45+8=53 > 50 -> no jugable
        assertThrows(NoPlayableCardException.class, () -> game.validateHasPlayableCard(human));
    }
}
package com.example.cincuentazo_bsj.model;

import com.example.cincuentazo_bsj.exceptions.CincuentazoException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void buildFullDeckHas52Cards() {
        Deck deck = new Deck();
        deck.buildFullDeck();
        assertEquals(52, deck.size());
    }

    @Test
    void drawCardReducesDeckSize() {
        Deck deck = new Deck();
        deck.buildFullDeck();
        deck.drawCard();
        assertEquals(51, deck.size());
    }

    @Test
    void drawCardOnEmptyDeckThrowsException() {
        Deck deck = new Deck();
        assertTrue(deck.isEmpty());
        assertThrows(CincuentazoException.class, deck::drawCard);
    }

    @Test
    void addCardsToBottomAreDrawnLast() {
        Deck deck = new Deck();
        deck.buildFullDeck();
        Card bottomCard = new Card("Picas", "5");
        deck.addCardsToBottom(java.util.List.of(bottomCard));

        Card last = null;
        for (int i = 0; i < 53; i++) {
            last = deck.drawCard();
        }
        assertEquals(bottomCard, last);
    }
}
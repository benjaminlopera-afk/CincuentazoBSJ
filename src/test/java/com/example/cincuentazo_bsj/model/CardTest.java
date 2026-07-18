package com.example.cincuentazo_bsj.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    @Test
    void numberCardReturnsOwnValue() {
        Card card = new Card("Corazones", "7");
        assertEquals(7, card.getBestValueFor(10));
    }

    @Test
    void nineCardHasZeroValue() {
        Card card = new Card("Picas", "9");
        assertEquals(0, card.getInitialTableValue());
        assertEquals(0, card.getBestValueFor(30));
    }

    @Test
    void faceCardsSubtractTen() {
        Card king = new Card("Treboles", "K");
        assertEquals(-10, king.getBestValueFor(20));
    }

    @Test
    void aceChoosesTenWhenPossibleOtherwiseOne() {
        Card ace = new Card("Diamantes", "A");
        assertEquals(10, ace.getBestValueFor(30)); // 30+10=40 <=50
        assertEquals(1, ace.getBestValueFor(45)); //45+10=55>50 -> usa 1
    }

    @Test
    void cardReturnsMinValueWhenNoOptionFits() {
        Card ten = new Card("Corazones", "10");
        assertEquals(Integer.MIN_VALUE, ten.getBestValueFor(45));
    }
}
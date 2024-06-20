package org.example.eiscuno.model.deck;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.unoenum.EISCUnoEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest extends ApplicationTest {

    private Deck deck;

    @BeforeEach
    public void setUp() {
        deck = new Deck();
    }

    @Test
    public void testDeckInitialization() {
        assertFalse(deck.isEmpty());
    }

    @Test
    public void testTakeCard() {
        // Take several cards and verify they match the expected values
        Card card1 = deck.takeCard();
        Card card2 = deck.takeCard();
        Card card3 = deck.takeCard();

        assertNotNull(card1);
        assertNotNull(card2);
        assertNotNull(card3);

        // Assuming specific values based on your card initialization logic
        assertEquals("RED", card1.getColor());
        assertEquals("5", card1.getValue());

        assertEquals("BLUE", card2.getColor());
        assertEquals("SKIP", card2.getValue());

        assertEquals("YELLOW", card3.getColor());
        assertEquals("+2", card3.getValue());

        // Check if the deck is empty after taking three cards
        assertFalse(deck.isEmpty());
    }

    @Test
    public void testEmptyDeck() {
        // Take all cards from the deck
        while (!deck.isEmpty()) {
            deck.takeCard();
        }

        // Try to take one more card, should throw IllegalStateException
        assertThrows(IllegalStateException.class, () -> {
            deck.takeCard();
        });
    }
}

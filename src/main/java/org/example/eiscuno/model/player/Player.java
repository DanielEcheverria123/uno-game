package org.example.eiscuno.model.player;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Uno game.
 */
public class Player implements IPlayer {
    private ArrayList<Card> cardsPlayer;
    private String typePlayer;
    private volatile boolean isProtected;
    private String drew;

    /**
     * Constructs a new Player object with an empty hand of cards.
     */
    public Player(String typePlayer) {
        this.cardsPlayer = new ArrayList<Card>();
        this.typePlayer = typePlayer;
        this.isProtected = false;
        this.drew = "";
    };

    /**
     * Adds a card to the player's hand.
     *
     * @param card The card to be added to the player's hand.
     */
    @Override
    public void addCard(Card card) {
        if (isProtected == false) {
            cardsPlayer.add(card);
        }
    }

    /**
     * Retrieves all cards currently held by the player.
     *
     * @return An ArrayList containing all cards in the player's hand.
     */
    @Override
    public ArrayList<Card> getCardsPlayer() {
        return cardsPlayer;
    }

    /**
     * Removes a card from the player's hand based on its index.
     *
     * @param index The index of the card to remove.
     */
    @Override
    public void removeCard(int index) {
        cardsPlayer.remove(index);
    }

    /**
     * Retrieves a card from the player's hand based on its index.
     *
     * @param index The index of the card to retrieve.
     * @return The card at the specified index in the player's hand.
     */
    @Override
    public Card getCard(int index) {
        return cardsPlayer.get(index);
    }

    public String getTypePlayer() {
        return typePlayer;
    }

    public synchronized List<Card> getCards() {
        return new ArrayList<>(cardsPlayer); // Return a copy to avoid concurrent modification issues
    }

    public synchronized void setProtected(boolean isProtected) {
        this.isProtected = isProtected;
    }

    public synchronized boolean isProtected() {
        return isProtected;
    }

    public synchronized void setDrew(String drew) {
        this.drew = drew;
    }

    public synchronized String getDrew() {
        return drew;
    }
}
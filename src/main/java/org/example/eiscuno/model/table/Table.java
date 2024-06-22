package org.example.eiscuno.model.table;

import org.example.eiscuno.model.card.Card;

import java.util.ArrayList;

/**
 * Represents the table in the Uno game where cards are played.
 */
public class Table {
    private ArrayList<Card> cardsTable;

    /**
     * Constructs a new Table object with no cards on it.
     */
    public Table() {
        this.cardsTable = new ArrayList<Card>();
    }

    /**
     * Adds a card to the table.
     *
     * @param card The card to be added to the table.
     */
    public Boolean addCardOnTheTable(Card card) {
        Card currentCardOnTheTable = this.cardsTable.get(this.cardsTable.size() - 1);
        System.out.print("Current card on the table: ");
        System.out.println(currentCardOnTheTable.getColor() + " " + currentCardOnTheTable.getValue());
        System.out.println(card.getColor() + " " + card.getValue());
        if (card.getColor().equals(currentCardOnTheTable.getColor()) ||
                card.getValue().equals(currentCardOnTheTable.getValue())) {
            System.out.println("Coincide con la carta de la mesa");
            this.cardsTable.add(card);
            return true;
        } else {
            System.out.println("No coincide con la carta de la mesa");
            System.out.println("Carta en la mesa: " + currentCardOnTheTable.getColor() + " y " + card.getColor());
            System.out.println("Carta en la mesa: " + currentCardOnTheTable.getValue() + " y " + card.getValue());
            System.out.println(card.getColor() + " " + card.getValue());

        }
        return false;
    }

    public ArrayList<Card> getCardsTable() {
        return cardsTable;
    }

    public void setStartCard(Card card) {
        this.cardsTable.add(card);
    }
}

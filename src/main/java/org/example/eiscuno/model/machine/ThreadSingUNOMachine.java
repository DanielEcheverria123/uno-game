package org.example.eiscuno.model.machine;

import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private ArrayList<Card> cardsMachine;
    private Deck deck;
    private Player humanPlayer;
    private Player machinePlayer;
    private Table table;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, ArrayList<Card> cardsMachine, Deck deck,
            Player humanPlayer, Player machinePlayer, Table table) {
        this.cardsPlayer = cardsPlayer;
        this.cardsMachine = cardsMachine;
        this.deck = deck;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.table = table;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hasOneCardTheHumanPlayer();
            // hasOneCardMachinePlayer();
        }
    }

    private void hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1) {
            System.out.println("UNO para el jugador");
            // Card cardWhenUnoSung = deck.takeCard();
            // humanPlayer.addCard(cardWhenUnoSung);
        }
    }

    // private void hasOneCardMachinePlayer() {
    // if (cardsMachine.size() == 1) {
    // System.out.println("UNO para la Máquina");
    // Card cardWhenUnoSung = deck.takeCard();
    // machinePlayer.addCard(cardWhenUnoSung);
    // }
    // }
}

package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private Deck deck;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck deck) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = deck;
    }

    public void run() {
        while (true) {
            if (hasPlayerPlayed) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Aqui iria la logica de colocar la carta
                putCardOnTheTable();
                hasPlayerPlayed = false;
            }
        }
    }

    private void putCardOnTheTable() {
        // int index = (int) (Math.random() * machinePlayer.getCardsPlayer().size());
        // System.out.println(machinePlayer.getCardsPlayer().get(index));
        // System.out.println("Cartas entes del Draw o put:");
        // for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
        // System.out.printf(machinePlayer.getCardsPlayer().get(i).getColor() + " "
        // + machinePlayer.getCardsPlayer().get(i).getValue() + " ");
        // }
        // System.out.println("\nEs el turno de la máquina, y juega la carta:");
        // System.out.println(card.getColor() + " " + card.getValue());
        // // table.addCardOnTheTable(card);
        boolean lastCardMachineNotMatch = false;
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            Card card = machinePlayer.getCard(i);
            if (table.addCardOnTheTable(card) == true) {
                tableImageView.setImage(card.getImage());
                machinePlayer.getCardsPlayer().remove(i);
                i = machinePlayer.getCardsPlayer().size() - 1;
                return;
            }
            if (i == machinePlayer.getCardsPlayer().size() - 1) {
                lastCardMachineNotMatch = true;
            }
        }
        if (lastCardMachineNotMatch) {
            // Eat card
            System.out.println("La máquina no tiene carta para jugar, come carta");
            Card cardToEat = this.deck.takeCard();
            System.out.println("Carta a comer: " + cardToEat.getColor() + " " + cardToEat.getValue());
            machinePlayer.addCard(cardToEat);
        }

        System.out.println("Fin de Ciclo For Para agregar carta de maquina al tablero");

        // tableImageView.setImage(card.getImage());
        System.out.println("Se Supone qué no debe poner nada ???");

        System.out.println(table.getCardsTable().get(0).getColor() + " " + table.getCardsTable().get(0).getValue());
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            System.out.printf(machinePlayer.getCardsPlayer().get(i).getColor() + " "
                    + machinePlayer.getCardsPlayer().get(i).getValue() + " ");
        }
    }

    public void setHasPlayerPlayed(boolean hasPlayerPlayed) {
        this.hasPlayerPlayed = hasPlayerPlayed;
    }

    public boolean getHasPlayerPlayed() {
        System.out.println("Has Player Played: " + hasPlayerPlayed);
        return hasPlayerPlayed;
    }
}

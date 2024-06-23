package org.example.eiscuno.model.machine;

import javafx.scene.image.ImageView;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

public class ThreadPlayMachine extends Thread {
    private Table table;
    private Player machinePlayer;
    private Player humanPlayer;
    private ImageView tableImageView;
    private volatile boolean hasPlayerPlayed;
    private Deck deck;

    public ThreadPlayMachine(Table table, Player machinePlayer, ImageView tableImageView, Deck deck,
            Player humanPlayer) {
        this.table = table;
        this.machinePlayer = machinePlayer;
        this.tableImageView = tableImageView;
        this.hasPlayerPlayed = false;
        this.deck = deck;
        this.humanPlayer = humanPlayer;
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

    // volatile
    private void putCardOnTheTable() {
        boolean lastCardMachineNotMatch = false;
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            Card card = machinePlayer.getCard(i);
            // System.out.println(table.addCardOnTheTable(card));
            System.out.println("TURNO:");
            getHasPlayerPlayed();
            if (table.addCardOnTheTable(card) == true) {
                // humanPlayer = new Player("HUMAN_PLAYER");
                // ************
                if (getHasPlayerPlayed() == true) {
                    // System.out.println(validateSpecialCardMachine(card, humanPlayer));
                    if (validateSpecialCardMachine(card, humanPlayer) == true) {
                        System.out.println("Carta Seleccionada es Valida");
                        if (card.getValue().equals("RESERVE")) {
                            System.out.println("Tiro otro RESERVE");
                            tableImageView.setImage(card.getImage());
                            machinePlayer.getCardsPlayer().remove(i);
                            i = machinePlayer.getCardsPlayer().size() - 1;
                            try {
                                System.out.println("Hilo durmiendo por segundo Reserve");
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            putCardOnTheTable();
                            return;
                        }
                        if (card.getValue().equals("SKIP")) {
                            System.out.println("ENTRÖ AL CÖDIGO DE SKIP");
                            tableImageView.setImage(card.getImage());
                            machinePlayer.getCardsPlayer().remove(i);
                            i = machinePlayer.getCardsPlayer().size() - 1;
                            if (card.getValue().equals("SKIP")) {
                                System.out.println("Tiro otro SKIP");
                                tableImageView.setImage(card.getImage());
                                machinePlayer.getCardsPlayer().remove(i);
                                i = machinePlayer.getCardsPlayer().size() - 1;
                                try {
                                    System.out.println("Hilo durmiendo por segundo SKIP");
                                    Thread.sleep(2000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                putCardOnTheTable();
                                return;
                            }
                            // setHasPlayerPlayed(false);
                        }
                        if ((card.getColor().equals("RED") && card.getValue().equals("WILD"))
                                || (card.getColor().equals("BLUE") && card.getValue().equals("WILD"))
                                || (card.getColor().equals("GREEN") && card.getValue().equals("WILD"))
                                || (card.getColor().equals("YELLOW") && card.getValue().equals("WILD"))) {
                            setHasPlayerPlayed(true);
                            System.out.println("AHORA SÍ Verificó las WILD");
                        }

                        if ((card.getColor().equals("RED") && card.getValue().equals("+4"))
                                || (card.getColor().equals("BLUE") && card.getValue().equals("+4"))
                                || (card.getColor().equals("GREEN") && card.getValue().equals("+4"))
                                || (card.getColor().equals("YELLOW") && card.getValue().equals("+4"))) {
                            setHasPlayerPlayed(true);
                            System.out.println("AHORA SÍ Verificó el +4");

                        }
                        if ((card.getColor().equals("RED") && card.getValue().equals("+2"))
                                || (card.getColor().equals("BLUE") && card.getValue().equals("+2"))
                                || (card.getColor().equals("GREEN") && card.getValue().equals("+2"))
                                || (card.getColor().equals("YELLOW") && card.getValue().equals("+2"))) {
                            setHasPlayerPlayed(true);
                            System.out.println("AHORA SÍ Verificó el +2");

                        }
                        // if (getHasPlayerPlayed() == true) {
                        // tableImageView.setImage(card.getImage());
                        // machinePlayer.getCardsPlayer().remove(i);
                        // i = machinePlayer.getCardsPlayer().size() - 1;
                        // return;
                        // }
                    } else {
                        setHasPlayerPlayed(true);
                        tableImageView.setImage(card.getImage());
                        machinePlayer.getCardsPlayer().remove(i);
                        i = machinePlayer.getCardsPlayer().size() - 1;
                        return;
                    }

                }
                // ************
                // setHasPlayerPlayed(true);
                // tableImageView.setImage(card.getImage());
                // machinePlayer.getCardsPlayer().remove(i);
                // i = machinePlayer.getCardsPlayer().size() - 1;
                // System.out.println("CODIGO RECIEN AGREGADO");
                // return;

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
        System.out.println("Agregó carta de maquina al tablero");

        // tableImageView.setImage(card.getImage());
        // System.out.println("Se Supone qué no debe poner nada ???");

        // System.out.println(table.getCardsTable().get(0).getColor() + " " +
        // table.getCardsTable().get(0).getValue());
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

    public boolean validateSpecialCardMachine(Card card, Player player) {
        int numberOfCards = 0;
        if (numberOfCards > 0) {
            System.out.println(player.getTypePlayer() + " have: " + player.getCardsPlayer().size() + " cards");
        }

        for (int i = 0; i < numberOfCards; i++) {
            player.addCard(this.deck.takeCard());
        }

        if (numberOfCards > 0) {
            System.out.println(player.getTypePlayer() + " eat now: " + numberOfCards + " cards");
            System.out.println(player.getTypePlayer() + " have now: " + player.getCardsPlayer().size() + " cards");
        }
        // Check if the card is a special draw card
        if (card.getValue().contains("+2")) {
            numberOfCards = 2;
            for (int i = 0; i < numberOfCards; i++) {
                Card cardFromWild = deck.takeCard();
                System.out.println(i);
                player.addCard(cardFromWild);
            }
            return true;
        } else if (card.getValue().contains("+4")) {
            numberOfCards = 4;
            for (int i = 0; i < numberOfCards; i++) {
                System.out.println(i);
                Card cardFromWild = deck.takeCard();
                player.addCard(cardFromWild);
            }
            return true;

        } else if (card.getValue().contains("SKIP")) {
            numberOfCards = 1;
            return true;

        } else if (card.getValue().contains("RESERVE")) {
            numberOfCards = 1;
            return true;

        } else if (card.getValue().contains("WILD")) {
            numberOfCards = 1;
            return true;

        } else {
            return false;
        }

    }
}

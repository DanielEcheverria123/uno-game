package org.example.eiscuno.model.machine;

import org.example.eiscuno.controller.GameUnoController;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.application.Platform;

import java.util.ArrayList;

public class ThreadSingUNOMachine implements Runnable {
    private ArrayList<Card> cardsPlayer;
    private ArrayList<Card> cardsMachine;
    private Deck deck;
    private Player humanPlayer;
    private Player machinePlayer;
    private Table table;
    private GridPane gridPane;
    private ThreadPlayMachine threadPlayMachine;
    private GameUno gameUno;
    private ImageView tableImageView;
    public GameUnoController controller;

    public ThreadSingUNOMachine(ArrayList<Card> cardsPlayer, ArrayList<Card> cardsMachine,
            Deck deck,
            Player humanPlayer, Player machinePlayer, Table table, GridPane gridPane,
            ThreadPlayMachine threadPlayMachine, GameUno gameUno, ImageView tableImageView,
            GameUnoController controller) {
        this.cardsPlayer = cardsPlayer;
        this.cardsMachine = cardsMachine;
        this.deck = deck;
        this.humanPlayer = humanPlayer;
        this.machinePlayer = machinePlayer;
        this.table = table;
        this.gridPane = gridPane;
        this.threadPlayMachine = threadPlayMachine;
        this.gameUno = gameUno;
        this.tableImageView = tableImageView;
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep((long) (Math.random() * 5000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (hasOneCardTheHumanPlayer()) {
                Platform.runLater(() -> {
                    controller.printCardsHumanPlayer();
                });
            }

            if (hasOneCardTheMachinePlayer()) {
                Platform.runLater(() -> {
                    controller.showMachineCards();
                });
            }

        }
    }

    private boolean hasOneCardTheHumanPlayer() {
        if (cardsPlayer.size() == 1) {
            if (humanPlayer.isProtected() == false) {
                System.out.println("UNO para el Jugador");
                Card cardWhenUnoSung = deck.takeCard();
                humanPlayer.addCard(cardWhenUnoSung);
                humanPlayer.setProtected(false);
                // controller.printCardsHumanPlayer();
                // printCardsHumanPlayer();
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean hasOneCardTheMachinePlayer() {
        if (cardsMachine.size() == 1) {
            if (machinePlayer.isProtected() == false) {
                System.out.println("MAQUINA DEFIENDE DE UNO, NO TOMA");
                // Card cardWhenUnoSung = deck.takeCard();
                // machinePlayer.addCard(cardWhenUnoSung);
                machinePlayer.setProtected(true);
                return true;
            }
            return false;
        }
        return false;
    }

    private void printCardsHumanPlayer() {
        Platform.runLater(() -> {
            gridPane.getChildren().clear();
            Card[] currentVisibleCardsHumanPlayer = gameUno.getCurrentVisibleCardsHumanPlayer(0);

            for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
                Card card = currentVisibleCardsHumanPlayer[i];
                ImageView cardImageView = card.getCard();

                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("CLICKEE LA CARTA:" + card.getValue() + " " + card.getColor());
                    if (this.gameUno.playCard(card)) {
                        if (this.threadPlayMachine.getHasPlayerPlayed() == false) {
                            System.out.println("Carta Seleccionada es Valida");
                            tableImageView.setImage(card.getImage());
                            humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                            printCardsHumanPlayer();
                            if (gameUno.validateSpecialCard(card, machinePlayer) == true) {
                                if (card.getValue().equals("RESERVE")) {
                                    threadPlayMachine.setHasPlayerPlayed(false);
                                } else if (card.getValue().equals("SKIP")) {
                                    threadPlayMachine.setHasPlayerPlayed(false);
                                }
                                if ((card.getColor().equals("RED") && card.getValue().equals("WILD"))
                                        || (card.getColor().equals("BLUE") && card.getValue().equals("WILD"))
                                        || (card.getColor().equals("GREEN") && card.getValue().equals("WILD"))
                                        || (card.getColor().equals("YELLOW") && card.getValue().equals("WILD"))) {
                                    threadPlayMachine.setHasPlayerPlayed(true);
                                    System.out.println("AHORA SÍ Verificó las WILD");
                                }

                                if ((card.getColor().equals("RED") && card.getValue().equals("+4"))
                                        || (card.getColor().equals("BLUE") && card.getValue().equals("+4"))
                                        || (card.getColor().equals("GREEN") && card.getValue().equals("+4"))
                                        || (card.getColor().equals("YELLOW") && card.getValue().equals("+4"))) {
                                    threadPlayMachine.setHasPlayerPlayed(true);
                                    System.out.println("AHORA SÍ Verificó el +4");

                                }

                                if ((card.getColor().equals("RED") && card.getValue().equals("+2"))
                                        || (card.getColor().equals("BLUE") && card.getValue().equals("+2"))
                                        || (card.getColor().equals("GREEN") && card.getValue().equals("+2"))
                                        || (card.getColor().equals("YELLOW") && card.getValue().equals("+2"))) {
                                    threadPlayMachine.setHasPlayerPlayed(true);
                                    System.out.println("AHORA SÍ Verificó el +2");

                                }

                            } else {
                                threadPlayMachine.setHasPlayerPlayed(true);
                            }

                        }
                    }
                });
                gridPane.add(cardImageView, i, 0);
            }
        });
    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < humanPlayer.getCardsPlayer().size(); i++) {
            if (humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

}

package org.example.eiscuno.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.machine.ThreadPlayMachine;
import org.example.eiscuno.model.machine.ThreadSingUNOMachine;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Controller class for the Uno game.
 */
public class GameUnoController {

    @FXML
    private GridPane gridPaneCardsMachine;

    @FXML
    private GridPane gridPaneCardsPlayer;

    @FXML
    private ImageView tableImageView;

    private Player humanPlayer;
    private Player machinePlayer;
    private Deck deck;
    private Table table;
    private GameUno gameUno;
    private int posInitCardToShow;

    private ThreadSingUNOMachine threadSingUNOMachine;
    private ThreadPlayMachine threadPlayMachine;

    /**
     * Initializes the controller.
     */
    @FXML
    public void initialize() {
        initVariables();
        this.gameUno.startGame();
        this.tableImageView.setImage(this.table.getCardsTable().get(0).getImage());
        printCardsHumanPlayer();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView, this.deck,
                this.humanPlayer, this);
        threadPlayMachine.start();
        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer(),
                this.machinePlayer.getCardsPlayer(), this.deck, this.humanPlayer, this.machinePlayer, this.table,
                gridPaneCardsPlayer, threadPlayMachine, this.gameUno, this.tableImageView, this);
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        showMachineCards();

    }

    /**
     * Initializes the variables for the game.
     */
    private void initVariables() {
        this.humanPlayer = new Player("HUMAN_PLAYER");
        this.machinePlayer = new Player("MACHINE_PLAYER");
        this.deck = new Deck();
        this.table = new Table();
        this.gameUno = new GameUno(this.humanPlayer, this.machinePlayer, this.deck, this.table);
        this.posInitCardToShow = 0;
    }

    /**
     * Prints the human player's cards on the grid pane.
     */
    public void printCardsHumanPlayer() {
        Platform.runLater(() -> {

            this.gridPaneCardsPlayer.getChildren().clear();
            Card[] currentVisibleCardsHumanPlayer = this.gameUno
                    .getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

            for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
                Card card = currentVisibleCardsHumanPlayer[i];
                ImageView cardImageView = card.getCard();

                cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    System.out.println("CLICKEE LA CARTA:" + card.getValue() + " " + card.getColor());
                    if (this.gameUno.playCard(card)) {
                        if (this.threadPlayMachine.getHasPlayerPlayed() == false) {
                            System.out.println("Carta Seleccionada es Valida");
                            this.tableImageView.setImage(card.getImage());
                            this.humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                            printCardsHumanPlayer();
                            if (this.gameUno.validateSpecialCard(card, this.machinePlayer) == true) {
                                if (card.getValue().equals("RESERVE")) {
                                    this.threadPlayMachine.setHasPlayerPlayed(false);
                                    machinePlayer.setProtected(false);
                                    // humanPlayer.setProtected(false);
                                } else if (card.getValue().equals("SKIP")) {
                                    this.threadPlayMachine.setHasPlayerPlayed(false);
                                    machinePlayer.setProtected(false);
                                    // humanPlayer.setProtected(false);
                                }
                                if ((card.getColor().equals("RED") && card.getValue().equals("WILD"))
                                        || (card.getColor().equals("BLUE") && card.getValue().equals("WILD"))
                                        || (card.getColor().equals("GREEN") && card.getValue().equals("WILD"))
                                        || (card.getColor().equals("YELLOW") && card.getValue().equals("WILD"))) {
                                    this.threadPlayMachine.setHasPlayerPlayed(true);
                                    machinePlayer.setProtected(false);
                                    // humanPlayer.setProtected(false);
                                    System.out.println("AHORA SÍ Verificó las WILD");
                                }

                                if ((card.getColor().equals("RED") && card.getValue().equals("+4"))
                                        || (card.getColor().equals("BLUE") && card.getValue().equals("+4"))
                                        || (card.getColor().equals("GREEN") && card.getValue().equals("+4"))
                                        || (card.getColor().equals("YELLOW") && card.getValue().equals("+4"))) {
                                    this.threadPlayMachine.setHasPlayerPlayed(true);
                                    machinePlayer.setProtected(false);
                                    // humanPlayer.setProtected(false);
                                    System.out.println("AHORA SÍ Verificó el +4");

                                }

                                if ((card.getColor().equals("RED") && card.getValue().equals("+2"))
                                        || (card.getColor().equals("BLUE") && card.getValue().equals("+2"))
                                        || (card.getColor().equals("GREEN") && card.getValue().equals("+2"))
                                        || (card.getColor().equals("YELLOW") && card.getValue().equals("+2"))) {
                                    this.threadPlayMachine.setHasPlayerPlayed(true);
                                    machinePlayer.setProtected(false);
                                    // humanPlayer.setProtected(false);
                                    System.out.println("AHORA SÍ Verificó el +2");

                                }

                            } else {
                                this.threadPlayMachine.setHasPlayerPlayed(true);
                            }

                        }
                    }
                });
                this.gridPaneCardsPlayer.add(cardImageView, i, 0);
            }
        });

        System.out.println("\nCARTAS DE LA MÄQUINA");
        for (int i = 0; i < machinePlayer.getCardsPlayer().size(); i++) {
            System.out.printf(machinePlayer.getCardsPlayer().get(i).getColor() + " "
                    + machinePlayer.getCardsPlayer().get(i).getValue() + " ");
        }

        System.out.println("\nCARTAS DEL JUGADOR");
        for (int i = 0; i < humanPlayer.getCardsPlayer().size(); i++) {
            System.out.printf(humanPlayer.getCardsPlayer().get(i).getColor() + " "
                    + humanPlayer.getCardsPlayer().get(i).getValue() + " ");
        }
        System.out.println("\n");

    }

    /**
     * Finds the position of a specific card in the human player's hand.
     *
     * @param card the card to find
     * @return the position of the card, or -1 if not found
     */
    private Integer findPosCardsHumanPlayer(Card card) {
        for (int i = 0; i < this.humanPlayer.getCardsPlayer().size(); i++) {
            if (this.humanPlayer.getCardsPlayer().get(i).equals(card)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Handles the "Back" button action to show the previous set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleBack(ActionEvent event) {
        if (this.posInitCardToShow > 0) {
            this.posInitCardToShow--;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the "Next" button action to show the next set of cards.
     *
     * @param event the action event
     */
    @FXML
    void onHandleNext(ActionEvent event) {
        if (this.posInitCardToShow < this.humanPlayer.getCardsPlayer().size() - 4) {
            this.posInitCardToShow++;
            printCardsHumanPlayer();
        }
    }

    /**
     * Handles the action of taking a card.
     *
     * @param event the action event
     */
    @FXML
    void onHandleTakeCard(ActionEvent event) {

        // Replenish deck
        if (this.deck.isEmpty()) {
            System.out.println("THIS DECK IS EMPTY");
            replenishDeck();
        } else {
            if (humanPlayer.isProtected()) {
                humanPlayer.setProtected(false);
            }
            System.out.println("THIS DECK IS NOT EMPTY");
            Card card = this.deck.takeCard();
            if (this.threadPlayMachine.getHasPlayerPlayed() == false) {
                if (card != null) {
                    this.humanPlayer.addCard(card);
                    printCardsHumanPlayer(); // Update the displayed cards
                    System.out.println("Human player took a card: " + card.getValue() + " " + card.getColor());
                    this.threadPlayMachine.setHasPlayerPlayed(true);

                } else {
                    // Handle the case where the deck is empty (optional)
                    System.out.println("The deck is empty! Cannot take a card.");
                }

            } else {
                return;

            }
        }
        machinePlayer.setProtected(false);
        checkWinner();
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {

        int cardsLeft = this.humanPlayer.getCardsPlayer().size();
        int machineCardsLeft = this.machinePlayer.getCardsPlayer().size();

        if (machineCardsLeft == 1) {
            // Machine has only one card left, call the game logic to handle "UNO"
            this.gameUno.haveSungOne("HUMAN_PLAYER");
            System.out.println("Machine player said UNO.");
            showMachineCards();
        } else if (cardsLeft == 1) {
            // Player has only one card left, call the game logic to handle "UNO"
            // this.gameUno.haveSungOne("HUMAN_PLAYER");
            System.out.println("Human player said UNO.");
            System.out.println(humanPlayer.isProtected());
            System.out.println("NOS PROTEGEMOS DE LA MÁQUINA");
            humanPlayer.setProtected(true);
            System.out.println("VERIFICANDO SI ESTAMOS PROTEGIDOS, Y CUANTO DURA:" + humanPlayer.isProtected());
        } else {
            // this.gameUno.haveSungOne("MACHINE_PLAYER");
            System.out.println("You have more than one card. UNO cannot be called yet.");
        }
    }

    @FXML
    void onHandlerButtonExit(ActionEvent event) throws InvocationTargetException {
        // Obtener el Stage actual (ventana del juego)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Proceed to interrupt the current threads
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        currentGroup.interrupt();
        // Cerrar el Stage (esto cerrará la ventana del juego y finalizará la
        // aplicación)
        stage.close();
    }

    public void showMachineCards() {
        gridPaneCardsMachine.getChildren().clear(); // Limpiar cualquier carta previamente mostrada

        List<Card> machineCards = machinePlayer.getCardsPlayer();

        // Mostrar solo las primeras 4 cartas de la máquina
        int cardsToShow = Math.min(machineCards.size(), 4);

        int columnIndex = 0;
        int rowIndex = 0;

        // Recorrer las cartas de la máquina y agregar ImageView para mostrar el reverso
        for (int i = 0; i < cardsToShow; i++) {
            Card card = machineCards.get(i);

            ImageView cardImageView = new ImageView(new Image("org/example/eiscuno/cards-uno/card_uno.png")); // Ruta a
                                                                                                              // la
                                                                                                              // imagen
                                                                                                              // del
                                                                                                              // reverso
            cardImageView.setFitHeight(90); // Ajustar el tamaño según sea necesario
            cardImageView.setFitWidth(70); // Ajustar el tamaño según sea necesario

            gridPaneCardsMachine.add(cardImageView, columnIndex, rowIndex); // Agregar ImageView al GridPane

            columnIndex++;
            if (columnIndex == 4) { // Asumiendo que el GridPane tiene 4 columnas
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    public void checkWinner() {
        if (gameUno.checkForWinner().equals("HUMAN_WIN")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Fin del juego!");
            alert.setHeaderText(null);
            alert.setContentText("¡Felicidades! Has ganado el juego.");
            alert.showAndWait();

        } else if (gameUno.checkForWinner().equals("MACHINE_WIN")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Fin del juego!");
            alert.setHeaderText(null);
            alert.setContentText("¡La máquina ha ganado el juego!");
            alert.showAndWait();
        }
    }

    public void replenishDeck() {
        ArrayList<Card> oldCardsTable = table.getCardsTable();
        System.out.println("FOR REPLENISH DECK");

        // Imprimir las cartas en la mesa antes de procesarlas
        for (int i = 0; i < oldCardsTable.size(); i++) {
            System.out.println(oldCardsTable.get(i).getColor() + " " + oldCardsTable.get(i).getValue());
        }

        // Crear una nueva pila para el nuevo mazo
        Stack<Card> newDeck = new Stack<>();

        // Obtener la última carta y eliminarla de la mesa
        Card lastCard = oldCardsTable.get(oldCardsTable.size() - 1);
        oldCardsTable.remove(oldCardsTable.size() - 1);

        // Añadir todas las cartas restantes a la nueva pila
        newDeck.addAll(oldCardsTable);
        System.out.println(newDeck);
        System.out.println("Deck size: " + newDeck.size());
        System.out.println(newDeck);

        // Barajar las cartas en la nueva pila
        Collections.shuffle(newDeck);

        // Reponer el mazo hasta que esté vacío
        while (!newDeck.isEmpty()) {
            deck.replenishDeck(newDeck.pop());
        }
        System.out.println("Deck replenished");

        // Limpiar las cartas de la mesa y añadir de nuevo la última carta
        table.getCardsTable().clear();
        table.getCardsTable().add(lastCard);
        tableImageView.setImage(lastCard.getImage());
    }

}

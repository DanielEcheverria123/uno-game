package org.example.eiscuno.controller;

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

import java.util.List;

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

        threadSingUNOMachine = new ThreadSingUNOMachine(this.humanPlayer.getCardsPlayer());
        Thread t = new Thread(threadSingUNOMachine, "ThreadSingUNO");
        t.start();

        threadPlayMachine = new ThreadPlayMachine(this.table, this.machinePlayer, this.tableImageView);
        threadPlayMachine.start();
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
    private void printCardsHumanPlayer() {
        this.gridPaneCardsPlayer.getChildren().clear();
        Card[] currentVisibleCardsHumanPlayer = this.gameUno.getCurrentVisibleCardsHumanPlayer(this.posInitCardToShow);

        for (int i = 0; i < currentVisibleCardsHumanPlayer.length; i++) {
            Card card = currentVisibleCardsHumanPlayer[i];
            ImageView cardImageView = card.getCard();

            cardImageView.setOnMouseClicked((MouseEvent event) -> {
                    if(this.gameUno.playCard(card)){
                        this.tableImageView.setImage(card.getImage());
                        this.humanPlayer.removeCard(findPosCardsHumanPlayer(card));
                        this.threadPlayMachine.setHasPlayerPlayed(true);
                        printCardsHumanPlayer();
                        this.gameUno.validateSpecialCard(card, this.machinePlayer);
                    }
            });
            this.gridPaneCardsPlayer.add(cardImageView, i, 0);
        }
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
        Card card = this.deck.takeCard();
        if (card != null) {
            this.humanPlayer.addCard(card);
            printCardsHumanPlayer(); // Update the displayed cards
            System.out.println("Human player took a card: " + card.getValue() + " " + card.getColor());

        } else {
            // Handle the case where the deck is empty (optional)
            System.out.println("The deck is empty! Cannot take a card.");
        }
    }

    /**
     * Handles the action of saying "Uno".
     *
     * @param event the action event
     */
    @FXML
    void onHandleUno(ActionEvent event) {
        int cardsLeft = this.humanPlayer.getCardsPlayer().size();
        if (cardsLeft == 1) {
            // Player has only one card left, call the game logic to handle "UNO"
            this.gameUno.haveSungOne("HUMAN_PLAYER");
            System.out.println("Human player said UNO.");

        } else {
            System.out.println("You have more than one card. UNO cannot be called yet.");
        }
    }

    @FXML
    void onHandlerButtonExit(ActionEvent event) {
        // Obtener el Stage actual (ventana del juego)
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Cerrar el Stage (esto cerrará la ventana del juego y finalizará la aplicación)
        stage.close();
    }


    private void showMachineCards() {
        gridPaneCardsMachine.getChildren().clear(); // Limpiar cualquier carta previamente mostrada

        List<Card> machineCards = machinePlayer.getCardsPlayer();

        // Mostrar solo las primeras 4 cartas de la máquina
        int cardsToShow = Math.min(machineCards.size(), 4);

        int columnIndex = 0;
        int rowIndex = 0;

        // Recorrer las cartas de la máquina y agregar ImageView para mostrar el reverso
        for (int i = 0; i < cardsToShow; i++) {
            Card card = machineCards.get(i);

            ImageView cardImageView = new ImageView(new Image("org/example/eiscuno/cards-uno/card_uno.png")); // Ruta a la imagen del reverso
            cardImageView.setFitHeight(90); // Ajustar el tamaño según sea necesario
            cardImageView.setFitWidth(70);  // Ajustar el tamaño según sea necesario

            gridPaneCardsMachine.add(cardImageView, columnIndex, rowIndex); // Agregar ImageView al GridPane

            columnIndex++;
            if (columnIndex == 4) { // Asumiendo que el GridPane tiene 4 columnas
                columnIndex = 0;
                rowIndex++;
            }
        }
    }

    private void checkWinner() {
        if (gameUno.checkForWinner()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Fin del juego!");
            alert.setHeaderText(null);
            alert.setContentText("¡Felicidades! Has ganado el juego.");
            alert.showAndWait();

        }
    }
}

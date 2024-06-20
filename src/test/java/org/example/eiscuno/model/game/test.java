package org.example.eiscuno.model.game;

import javafx.stage.Stage;
import org.example.eiscuno.model.card.Card;
import org.example.eiscuno.model.deck.Deck;
import org.example.eiscuno.model.game.GameUno;
import org.example.eiscuno.model.player.Player;
import org.example.eiscuno.model.table.Table;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class TableTest extends ApplicationTest {

    @Override
    public void start(Stage stage) {
        // This is required to start the JavaFX application thread.
    }

    @Test
    void addCardOnTheTableTest(){
        var humanPlayer = new Player("HUMAN_PLAYER");
        var machinePlayer = new Player("MACHINE_PLAYER");
        var deck = new Deck();
        var table = new Table();
        var gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);

        boolean isRedCardPut = false;
        while (!isRedCardPut){
            var card = deck.takeCard();
            if(card.getColor().equals("RED")){
                table.setStartCard(card);
                isRedCardPut = true;
            }
        }
        assertEquals("RED", table.getCardsTable().get(0).getColor());
    }

    @Test
    void checkStartCardIsRed() {
        // Configuración inicial
        Player humanPlayer = new Player("HUMAN_PLAYER");
        Player machinePlayer = new Player("MACHINE_PLAYER");
        Deck deck = new Deck();
        Table table = new Table();
        GameUno gameUno = new GameUno(humanPlayer, machinePlayer, deck, table);

        // Colocar una carta roja en la mesa
        boolean isRedCardPut = false;
        while (!isRedCardPut) {
            var card = deck.takeCard();
            if (card.getColor().equals("RED")) {
                table.setStartCard(card);
                isRedCardPut = true;
            }
        }

        // Verificar que la primera carta en la mesa sea roja
        assertEquals("RED", table.getCardsTable().get(0).getColor());
    }




}
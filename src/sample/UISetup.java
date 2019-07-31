package sample;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.control.Button;
import javafx.geometry.Orientation;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.PopupControl;
import javafx.scene.Scene;
import javafx.stage.Modality;
import org.junit.Test;

public class UISetup {

    int currentpoints = 0;
    Label player1bid, player2bid, player3bid, player4bid, rounds, trumpsuit, highownerlab, lowownerlab, jackownerlab, gameownerlab, winnerlab, handwinner, gameover;
    Button playcard;
    BorderPane smallerpane, smallestpane;
    VBox bidinfobox;
    int numplayers;
    HBox hb1; //= new HBox(12);
    VBox display1, display2, display3, display4;
    VBox pointsbox, lastbidbox, roundwinners, handwinnerbox;
    HBox playedcards, bidinfo;
    Text currentpointstext;
    ImageView cardplyr1, cardply2, cardply3, cardply4;

    UISetup(int numplayers){
        this.numplayers = numplayers;
        hb1 = new HBox(12);
        playcard = new Button("play card");
        smallerpane = new BorderPane();
        smallestpane = new BorderPane();
        smallerpane.setCenter(smallestpane);
        cardplyr1= new ImageView();  cardply2= new ImageView(); cardply3= new ImageView(); cardply4= new ImageView();
        display1 = new VBox(); display2 = new VBox(); display3 = new VBox(); display4 = new VBox();
        pointsbox = new VBox(10);
        currentpointstext = new Text("Current points: \n");
        winnerlab = new Label();
        highownerlab = new Label();
        lowownerlab = new Label();
        jackownerlab = new Label();
        gameownerlab = new Label();
        roundwinners = new VBox();
        handwinner = new Label();
        handwinnerbox = new VBox();
        winnerlab.setText("Winners: ");
        highownerlab = new Label("High: Player " );
        lowownerlab = new Label("Low: Player ");
        jackownerlab = new Label("Jack: None ");
        gameownerlab = new Label("Game: Player " );
        gameover = new Label("Game over!");
    }


    UISetup(){}; //default constructor


    void setupgame(Pitch game, BorderPane startpane, HashMap<Integer, ArrayList<String>> playerinfo, Button startbutton, Button exitbutton, Button bidbutton, TextField bidfield2, Button nexthandbtn, Button playcardbtn, Button btn1 , Button btn2, Button btn3, Button btn4, Button btn5, Button btn6) {
        startpane.setPadding(new Insets(10)); /*set up 4 borders panes*/



        for (int i = 0; i < numplayers; i++){ //give users hand and display hand
            ArrayList<Card> cards = game.getHand(i); //returns object hand from class Player
            playerinfo.put((i), new ArrayList<String>()); //create hash map where key = player #
            for (int j =0; j < 6; j++){
                playerinfo.get((i)).add((cards.get(j).getID())); //populate hashmap array values with IDs
                System.out.println("Player" + i + " hand: " + playerinfo.get((i)).get(j));

            }
        }


        /*set start, exit, and play card buttons*/
        HBox bottombuttons = new HBox(12);
        startbutton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        exitbutton.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        nexthandbtn.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        //playcardbtn.setMinSize(Button.USE_PREF_SIZE, Button.USE_PREF_SIZE);
        final Pane spacer = new Pane(); /*first spacer*/
        HBox.setHgrow(spacer, Priority.ALWAYS);
        spacer.setMinSize(35, 0);
        final Pane spacer2 = new Pane(); /*second spacer*/
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        spacer2.setMinSize(40, 0);
        bottombuttons.setPadding(new Insets(10, 10, 10, 10));

        HBox playhand = new HBox(5);
        playhand.getChildren().addAll(playcardbtn);
        playcardbtn.setText("click to continue");
        bottombuttons.getChildren().addAll(startbutton, spacer, spacer2, exitbutton);

        smallerpane.setBottom(bottombuttons);
        startpane.setCenter(smallerpane);

        /*round label*/
        rounds = new Label("Round 1");
        smallerpane.setTop(rounds);
        smallerpane.setAlignment(rounds, Pos.TOP_CENTER);
        smallerpane.setBottom(bottombuttons);


        /*player cards at bottom*/
        hb1.getChildren().clear();
        hb1.setPadding(new Insets(10,  10, 5, 105));
        startpane.setBottom(hb1);


        /*set up player bid*/
        bidinfobox = new VBox();
        bidinfobox.setPadding(new Insets(10, 0, 0, 0 ));
        Label currentbids = new Label();
        Text currentbidtext = new Text("Current bids:");

        Text userbid = new Text("Player bid: ");
        userbid.setFont(Font.font("Verdana", 14));

        System.out.println("Numplayers in UI Setup: " + numplayers);
        player1bid = new Label("User bid: " + Integer.toString(Pitch.players.get(0).getBid()));
        player2bid = new Label("Player 2 bid: " + Integer.toString(Pitch.players.get(1).getBid()));
        if (numplayers == 3){
            player3bid = new Label("Player 3 bid: " + Integer.toString(Pitch.players.get(2).getBid())); }
        if (numplayers == 4){
            player3bid = new Label("Player 3 bid: " + Integer.toString(Pitch.players.get(2).getBid()));
            player4bid = new Label( "Player 4 bid: "  + Integer.toString(Pitch.players.get(3).getBid()));
        }

        switch(Pitch.players.size()){
            case 2: bidinfobox.getChildren().addAll(player1bid, player2bid); break;
            case 3: bidinfobox.getChildren().addAll(player1bid, player2bid, player3bid); break;
            case 4: bidinfobox.getChildren().addAll(player1bid, player2bid, player3bid, player4bid); break;

        }

        /*enter bid textfield and playerbid info*/
        pointsbox.setPadding(new Insets(0, 0 , 0 ,0 ));
        bidinfo = new HBox(bidfield2, bidbutton);
        lastbidbox = new VBox(bidinfo, bidinfobox);
        startpane.setLeft(lastbidbox);


        /*set up current points and nexthand button*/
        for (int i = 0; i < numplayers; i++) {
            currentpointstext.setText(currentpointstext.getText() + "Player " + (i + 1) + ": " + Pitch.players.get(i).getOverallScore() + " \n");
        }
        pointsbox.getChildren().addAll(nexthandbtn, currentpointstext);
        startpane.setAlignment(currentpointstext, Pos.TOP_RIGHT);
        startpane.setRight(pointsbox);

        /*set card images up at start screen*/
        for (int j = 0; j < 6; j++) {
            String imagename = playerinfo.get(0).get(j) + ".jpg";
            Image pic = new Image(imagename);
            ImageView v = new ImageView(pic);
            v.setFitHeight(175);
            v.setFitWidth(92.5);
            v.setPreserveRatio(true);
            switch(j){
                case 0: btn1.setGraphic(v); hb1.getChildren().add(btn1); break;
                case 1: btn2.setGraphic(v); hb1.getChildren().add(btn2); break;
                case 2: btn3.setGraphic(v); hb1.getChildren().add(btn3); break;
                case 3: btn4.setGraphic(v); hb1.getChildren().add(btn4); break;
                case 4: btn5.setGraphic(v); hb1.getChildren().add(btn5); break;
                case 5: btn6.setGraphic(v); hb1.getChildren().add(btn6); break;
            }
        }
    }


    void showCardClear(){
        display1.getChildren().clear();
        display2.getChildren().clear();
        display3.getChildren().clear();
        display4.getChildren().clear();
    }

    /*show played card in center*/
    void showCard(String cardname, int playernum){

        System.out.println("UI setup playernum: " + playernum);
        if (playernum == 0){
            Label owner1 = new Label ("Player " + (playernum+1) + " card");
            Image pic = new Image(cardname + ".jpg");
            cardplyr1.setImage(pic);
            cardplyr1.setFitHeight(250);
            cardplyr1.setFitHeight(250);
            cardplyr1.setFitWidth(150);
            cardplyr1.setPreserveRatio(true);
            display1.getChildren().addAll(cardplyr1, owner1);
            display1.maxHeightProperty().bind( display1.heightProperty());
        }else if (playernum == 1){
            Label owner2 = new Label ("Player " + (playernum+1)  + " card");
            Image pic = new Image(cardname + ".jpg");
            cardply2.setImage(pic);
            cardply2.setFitHeight(250);
            cardply2.setFitHeight(250);
            cardply2.setFitWidth(150);
            cardply2.setPreserveRatio(true);
            display2.getChildren().addAll(cardply2, owner2);
            display2.maxHeightProperty().bind( display1.heightProperty());
        }else if (playernum == 2){
            Label owner3 = new Label ("Player " + (playernum+1) + " card");
            Image pic = new Image(cardname + ".jpg");
            cardply3.setImage(pic);
            cardply3.setFitHeight(250);
            cardply3.setFitHeight(250);
            cardply3.setFitWidth(150);
            cardply3.setPreserveRatio(true);
            display3.getChildren().addAll(cardply3, owner3);
            display3.maxHeightProperty().bind( display1.heightProperty());
        }else{
            Label owner4 = new Label ("Player " + (playernum+1) + " card");
            Image pic = new Image(cardname + ".jpg");
            cardply4.setImage(pic);
            cardply4.setFitHeight(250);
            cardply4.setFitHeight(250);
            cardply4.setFitWidth(150);
            cardply4.setPreserveRatio(true);
            display4.getChildren().addAll(cardply4, owner4);
            display4.maxHeightProperty().bind( display1.heightProperty());
        }
        playedcards = new HBox(10); /*final box to display all played cards*/
        playedcards.getChildren().addAll(display1, display2, display3, display4);
        smallestpane.setBottom(playedcards);
    }

    void clearMiddle(){ //clear card image from middle
        smallestpane.setBottom(null);
    }

    void showGameOver(ArrayList<Integer> winners, Stage stage, Button start, Button exit){
        clearMiddle();
        if (winners.size() == 1) {
            gameover.setText("Game over! Winners: \n Player " + (winners.get(0)+1) + " with " + Pitch.players.get(winners.get(0)).getOverallScore() + " points!" );
        }
        else if (winners.size() == 2) {
            gameover.setText("Game over! Winners: \n Player " + (winners.get(0)+1) + " with " + Pitch.players.get(winners.get(0)).getOverallScore() + " points! \n" +
                    "Player " + (winners.get(1)+1) + "with " + Pitch.players.get(winners.get(1)).getOverallScore() + " points!" );
        }
        else if (winners.size() == 3){
            gameover.setText("Game over! Winners: \n Player " + (winners.get(0)+1) + " with " + Pitch.players.get(winners.get(0)).getOverallScore() + " points! \n" +
                    "Player " + (winners.get(1)+1) + "with " + Pitch.players.get(winners.get(1)).getOverallScore() + " points!\n" + "Player " + (winners.get(2)+1) +
                    " with " + Pitch.players.get(winners.get(2)).getOverallScore() + " points!");
        }else{
            gameover.setText("Game over! Winners: \n Player " + (winners.get(0)+1) + " with " + Pitch.players.get(winners.get(0)).getOverallScore() + " points! \n" +
                    "Player " + (winners.get(1)+1) + "with " + Pitch.players.get(winners.get(1)).getOverallScore() + " points!\n" + "Player " + (winners.get(2)+1) +
                    " with " + Pitch.players.get(winners.get(2)).getOverallScore() + " points!\n "+ "Player " + (winners.get(3)+1) +" with " +
                    Pitch.players.get(winners.get(3)).getOverallScore() + " points! \n");
        }

        VBox newbox = new VBox(gameover);
        HBox buttons = new HBox(start, exit);
        VBox finalv = new VBox(newbox, buttons);
        BorderPane endpane = new BorderPane();
        endpane.setCenter(finalv);
        Scene scene;
        scene = new Scene(endpane, 400, 500); //default
        stage.setScene(scene);
    }

    void updatebids(Scene scene ){
        player1bid.setText( "User bid: " + Integer.toString(Pitch.players.get(0).getBid()));
        player2bid.setText( "Player 2 bid: " + Integer.toString(Pitch.players.get(1).getBid()));
        if (Pitch.players.size() == 3){
            player3bid.setText( "Player 3 bid: " + Integer.toString(Pitch.players.get(2).getBid())); }
        if (Pitch.players.size() == 4){
            player4bid.setText( "Player 4 bid: " + Integer.toString(Pitch.players.get(3).getBid())); }

    }

    void updaterounds(int roundnum){
        rounds.setText("Round " + String.valueOf(roundnum));

    }

    void updatescores(){
        currentpointstext.setText(""); /*clear text*/
        for (int i = 0; i < numplayers; i++) {
            currentpointstext.setText(currentpointstext.getText() + "Player " + (i + 1) + ": " + Pitch.players.get(i).getOverallScore() + " \n");
        }
    }

    void showTrumpCard(Card card){
        trumpsuit = new Label("Trump suit: " + card.getSuit().getSuit());
        lastbidbox.getChildren().clear();
        lastbidbox.getChildren().addAll(bidinfo, bidinfobox, trumpsuit);
    }

    void hideTrumpCard(){
        trumpsuit.setText("");
    }

    void displayHandWinner(int winner, Button continuebtn){
        handwinnerbox.getChildren().clear();
        handwinner.setText("Player "+ (winner+1) +" won the hand");
        handwinnerbox.getChildren().addAll(handwinner, continuebtn);
        playedcards.getChildren().add(handwinnerbox);
    }

    void displayRoundWinners(int highowner, int lowowner, int jackowner, int gameowner){
        roundwinners.getChildren().clear();
        winnerlab.setText("Winners: ");
        highownerlab.setText("High: Player "  + (highowner+1));
        lowownerlab.setText("Low: Player " + (lowowner+1));
        if (jackowner == 10){ /*if jackowner = 10, jackcard is null*/
            jackownerlab.setText("Jack: None ");
        }else{
            jackownerlab.setText("Jack: Player " + (jackowner+1));
        }
        gameownerlab.setText("Game: Player " + (gameowner+1));
        roundwinners.getChildren().addAll(winnerlab, highownerlab, lowownerlab, jackownerlab, gameownerlab);
        smallestpane.setCenter(roundwinners);


    }

    void hideRoundWinner(){
        smallestpane.setCenter(null); /*this may cause problems when trying to put stuff back in?? idk*/
    }


    void updatecards(Button btn1, Button btn2, Button btn3, Button btn4, Button btn5, Button btn6){
        /*set card images up at start screen*/
        hb1.getChildren().clear(); //clear existing cards from HBox
        for (int j = 0; j < Pitch.players.get(0).hand.size() ; j++) {
            String imagename = Pitch.players.get(0).hand.get(j).getID() + ".jpg";
            Image pic = new Image(imagename);
            ImageView v = new ImageView(pic);
            v.setFitHeight(175);
            v.setFitWidth(92.5);
            v.setPreserveRatio(true);
            switch(j){
                case 0: btn1.setGraphic(v); hb1.getChildren().add(btn1); break;
                case 1: btn2.setGraphic(v); hb1.getChildren().add(btn2); break;
                case 2: btn3.setGraphic(v); hb1.getChildren().add(btn3); break;
                case 3: btn4.setGraphic(v); hb1.getChildren().add(btn4); break;
                case 4: btn5.setGraphic(v); hb1.getChildren().add(btn5); break;
                case 5: btn6.setGraphic(v); hb1.getChildren().add(btn6); break;
            }
        }
    }

    void playerturn(){ /*display that its the player's turn to play a card*/
        Label userturn = new Label("play card for this round");


    }


    void displayPopup(String message, Stage stage) {

        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Your turn to play card");

        Label label1= new Label(message + " \n");

        Button button1= new Button("Ok");
        button1.setOnAction(e -> popupwindow.close());

        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, button1);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 320, 250);

        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

    void displayBidPopup(String message, Stage stage, TextField bids) {

        Stage popupwindow=new Stage();

        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Your turn to play card");

        Label label1= new Label(message + " \n" );

        Button button1= new Button("Submit");
        button1.setOnAction(e -> popupwindow.close());

        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1, bids, button1);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 320, 250);

        popupwindow.setScene(scene1);
        popupwindow.showAndWait();

    }

}

/*  try {
                            TimeUnit.SECONDS.sleep(2);
                        } catch(InterruptedException e) {
                            System.out.println("got interrupted!");
                        }*/

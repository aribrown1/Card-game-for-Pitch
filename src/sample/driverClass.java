package sample; //keep

import java.util.Collections;
import java.util.HashMap;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class driverClass extends Application{

    TextField text, bidfield, playcardfield,  bidsretry;
    BorderPane startpane;
    Button btn, butn2, x, y, z, startbutton, a, b, c, exitbutton, bid, newexit, nexthandbtn, continuebtn, newstart;
    Button cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6, bidsretrybtn;
    Stage myStage;
    Scene scene, scene2, welcomescene;
    HashMap<String, Scene> sceneMap; /*hash map to contain scences*/
    HashMap<Integer, ArrayList<String>> playerinfo;
    UISetup uistuff;
    Pitch game, newgame;
    PitchDealer pitchDealer;
    Dealer dealer;
    Player player;
    AIPlayer aiplayer;
    GameInfo gameinfo;
    boolean bidsallzero, roundwinner;
    int numplayers, round, playerbid;
    static int winngingbidplyr;
    static  Card trumpcard;
    int gamewin = 7;
    boolean firstofround = true;
    int trickcount;
    int owner;
    boolean restart = false;

    /*need to fix to include overall trump*/

    public static void main(String[] args) {
        launch(args);

    }

    void updateplayerinfo(){
        for (int i = 0; i < numplayers; i++){ //give users hand and display hand
            ArrayList<Card> cards = game.getHand(i); //returns object hand from class Player
            playerinfo.put((i), new ArrayList<String>()); //create hash map where key = player #
            for (int j =0; j < cards.size() ; j++){
                playerinfo.get((i)).add((cards.get(j).getID())); //populate hashmap array values with IDs
                System.out.println("Player" + i + " hand: " + playerinfo.get((i)).get(j));

            }
        }
    }


    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Our first JavaFX Experience!");

        myStage = primaryStage;
        text = new TextField();
        bidfield = new TextField();
        playcardfield = new TextField();
        btn = new Button();
        butn2 = new Button("change scene");
        bid = new Button("bid");
        exitbutton = new Button("exit");
        continuebtn = new Button("continue");
        newexit = new Button("exit");
        newstart = new Button("start");
        startbutton = new Button("start");
        nexthandbtn = new Button("next hand");
        sceneMap = new HashMap<String, Scene>();
        numplayers = 2; /*default to 2*/
        playerinfo = new HashMap<Integer, ArrayList<String>>();
        bidfield = new TextField();
        bidsallzero = true;
        round = 1;
        startpane = new BorderPane();
        welcomescene = new Scene(startpane, 950, 700); //welcome scene
        winngingbidplyr = 0;
        trickcount = 1;
        player = new Player();
        aiplayer = new AIPlayer();
        bidsretry = new TextField();
        bidsretrybtn = new Button("submit");

        x = new Button("one"); /* for scenes */
        y = new Button("two");
        z = new Button("three");

        a = new Button("Two players");
        b = new Button("Three players");
        c = new Button("Four players");


        EventHandler<ActionEvent> exitGame = new EventHandler<ActionEvent>(){ /* exit button*/ //anonymous class
            public void handle(ActionEvent event){

                Button b = (Button)event.getSource(); /*so we can disable button*/
                System.out.println("Goodbye!");
                System.exit(0);

            }
        };


        exitbutton.setOnAction(exitGame);


        butn2.setOnAction(new EventHandler<ActionEvent>(){ //regular class

            public void handle(ActionEvent event){
                myStage.setScene(sceneMap.get("gamePlay"));
            }
        });

        btn.setOnAction(new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event){
                System.out.println(text.getText());
                text.clear();
            }
        });

        cardbtn1 = new Button(); cardbtn1.setId("btn1");
        cardbtn2 = new Button(); cardbtn2.setId("btn2");
        cardbtn3 = new Button(); cardbtn3.setId("btn3");
        cardbtn4 = new Button(); cardbtn4.setId("btn4");
        cardbtn5 = new Button(); cardbtn5.setId("btn5");
        cardbtn6 = new Button(); cardbtn6.setId("btn6");


        startbutton.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {


                int rankvals[]; String suitvals[];
                sceneMap.put("start", welcomescene);
                myStage.setTitle("In Game");
                myStage.setScene(sceneMap.get("start"));

                /*give users hands*/
                /*human player will always be player 1 and start with player.get(1)*/

                uistuff = new UISetup(numplayers);
                game = new Pitch(numplayers);

                System.out.println("numplayers" + numplayers);


                /*set up welcome screen*/
                uistuff.setupgame(game, startpane, playerinfo, newstart, newexit, bid, bidfield, nexthandbtn, continuebtn, cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6);
            }
        });

        a.setOnAction(event -> numplayers = 2); //lambda function
        b.setOnAction(event -> numplayers = 3); //lambda function
        c.setOnAction(event -> numplayers = 4); //lambda function

        newexit.setOnAction(new EventHandler<ActionEvent>(){ /*when exit button is pressed from game playing screen*/

            public void handle(ActionEvent event){
                myStage.setScene(sceneMap.get("welcome"));
                myStage.show();
                startpane.getChildren().clear();
                playerinfo.clear();

            }
        });
        newstart.setOnAction(new EventHandler<ActionEvent>(){ /*when start button is pressed from game playing screen*/

            public void handle(ActionEvent event){
                newgame = new Pitch(numplayers);
                round = 1;
                trickcount = 1;
                playerinfo.clear();
                updateplayerinfo();
                uistuff.hideTrumpCard();
                uistuff.updatecards(cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6);
                uistuff.updatebids(sceneMap.get("welcome"));
                uistuff.updaterounds(round);
            }
        });

        bid.setOnAction(new EventHandler<ActionEvent>() {

            public void handle(ActionEvent event) {

                pitchDealer = new PitchDealer(6);

                String stringbid = bidfield.getText();
                if (stringbid.equals("smudge") || stringbid.equals("Smudge") || stringbid.equals("SMUDGE")){
                    playerbid = 5;
                }
                playerbid = Integer.parseInt(bidfield.getText()); /*convert string to int*/
                game.players.get(0).setBid(playerbid);
                game.AIbid(); /*make AIs bid*/

                /*check if bids are all 0 */
                bidsallzero = game.getBids(playerbid);
                while (bidsallzero) {
                    if (numplayers * 6 > pitchDealer.cardstodeal.size()){ //if weve run out of cards to deal*/
                        GameInfo.clearArray();
                        GameInfo.createcards();
                        System.out.println("Allcards size: " + GameInfo.allcards.size());
                        pitchDealer.copyArray(GameInfo.allcards);

                        for (int i = 0; i < numplayers; i++){
                            game.players.get(i).setEntireHand(pitchDealer.dealHand());
                            game.players.get(i).setHandOwner(game.players.get(i), i);
                        }

                    }
                    System.out.println("bid loop ");
                    for (int i =0 ; i < numplayers; i++){
                        game.players.get(i).clearHand();
                        game.players.get(i).setEntireHand(pitchDealer.dealHand());
                    }
                    uistuff.displayBidPopup("Bids were all zero! Please bid again: " , myStage, bidsretry);
                    playerbid = Integer.parseInt(bidsretry.getText()); /*convert string to int*/
                    bidsallzero = game.getBids(playerbid);
                    if (!bidsallzero){ updateplayerinfo(); uistuff.updatecards(cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6); game.players.get(0).setBid(playerbid); break; }

                }
                /*we know we can proceed with the round*/
                winngingbidplyr = game.getWinningBid();
                uistuff.updatebids(sceneMap.get("welcome"));

                if (trickcount == 1){
                    game.firstwinner = winngingbidplyr; /*get first winner for end of game scoring*/
                }

                ArrayList<Integer> losers = new ArrayList<Integer>(game.loserArray(winngingbidplyr));


                if (winngingbidplyr == 0){
                    uistuff.displayPopup("Your turn! Click a card to play", myStage);
                }else { /*an AI won bid*/

                    /*playcard for winning AI player*/
                    roundwinner = true;
                    Card temp = game.AIplaycard(game.players.get(winngingbidplyr), roundwinner);
                    game.firstplayed = temp; /*set first played of the round*/
                    if (firstofround) {
                        trumpcard = temp;
                        game.trumpcard = trumpcard;
                    }
                    /*set trump card for game*/
                    uistuff.showTrumpCard(trumpcard);
                    uistuff.showCard(temp.ID, winngingbidplyr);
                    game.cardsinround.add(temp);

                    roundwinner = false; /*have losing AIs and user play*/


                    /*loop through losing AIs*/
                    aiplayer.play(losers, game, uistuff, roundwinner);


                    uistuff.displayPopup("Your turn! Click a card to play", myStage);

                }
            }
        });
        nexthandbtn.setOnAction(new EventHandler<ActionEvent>(){ //start disapears when you return back to screen, fix

            public void handle(ActionEvent event){
                //System.out.println("in driver, cards to deal size : " + pitchDealer.cardstodeal.size());
                System.out.println("in driver, cards to deal size : " + pitchDealer.getNumcardstodeal());


                /*check for winenrs at end of round*/
                for (int i = 0; i < numplayers; i++){
                    if (game.players.get(i).getOverallScore() >= 7){
                       System.out.println("get overall score: " + game.players.get(i).getOverallScore());
                    }
                }
                ArrayList<Integer> winners = new ArrayList<Integer>();
                for (int i = 0; i < numplayers; i++){
                    if (game.players.get(i).getOverallScore() >= 7){
                        winners.add(i); //for multiple winners
                        System.out.println("recognizze");
                    }
                }
                System.out.println("Winners size: "+ winners.size());
                if (winners.size() > 0){ uistuff.showGameOver(winners, myStage, startbutton, exitbutton);}


                    if (numplayers == 2) {
                        if (pitchDealer.getNumcardstodeal() < 12) {
                            GameInfo.clearArray();
                            GameInfo.createcards();
                            pitchDealer = new PitchDealer(numplayers);
                            System.out.println("Allcards size: " + GameInfo.allcards.size());
                            pitchDealer.copyArray(GameInfo.allcards);
                        }
                    }
                    if (numplayers == 3) {
                        if (pitchDealer.getNumcardstodeal() < 18) {
                            GameInfo.clearArray();
                            GameInfo.createcards();
                            System.out.println("Allcards size: " + GameInfo.allcards.size());
                            pitchDealer.copyArray(GameInfo.allcards);
                        }
                    }
                    if (numplayers == 4) {
                        if (pitchDealer.getNumcardstodeal() < 24) {
                            GameInfo.clearArray();
                            GameInfo.createcards();
                            System.out.println("Allcards size: " + GameInfo.allcards.size());
                            pitchDealer.copyArray(GameInfo.allcards);
                        }
                    }

                    uistuff.clearMiddle();
                    uistuff.updaterounds(round++);
                    uistuff.hideRoundWinner();
                    uistuff.showCardClear();
                    firstofround = true;
                    trickcount = 1;
                    /*redeal cards, reinitialize cardsinround array*/
                    for (int i = 0; i < numplayers; i++) {
                        game.players.get(i).clearRoundDeck();
                        game.players.get(i).clearPlayedCards();
                        game.players.get(i).clearBid();
                        game.players.get(i).clearHand();
                        game.players.get(i).setRoundscore(0);
                        game.players.get(i).setEntireHand(pitchDealer.dealHand());
                        game.players.get(i).setHandOwner(game.players.get(i), i);
                    }


                    updateplayerinfo();
                    uistuff.updatecards(cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6);
                }
            //}
        });
        continuebtn.setOnAction(new EventHandler<ActionEvent>(){ //start disapears when you return back to screen, fix

            public void handle(ActionEvent event) {

                if (trickcount == 7) { //end of round

                        // uistuff.updatecards(cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6);
                    System.out.println("trick count is 7");
                    winngingbidplyr = owner;

                    game.endOfRound(winngingbidplyr);
                    game.cardsinround.clear();

                    uistuff.clearMiddle();
                    uistuff.displayRoundWinners(game.highowner, game.lowowner, game.jackowner, game.gameowner);
                    uistuff.updatecards(cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6);

                    uistuff.displayPopup("Round is over! Please press 'next hand' button", myStage);
                    uistuff.updatescores();
                    firstofround = true;
                    restart = true;

                } else {
                    restart = false;
                    uistuff.updaterounds(trickcount);
                    game.cardsinround.clear();
                    uistuff.clearMiddle();
                    uistuff.showCardClear();

                    updateplayerinfo();
                    uistuff.updatecards(cardbtn1, cardbtn2, cardbtn3, cardbtn4, cardbtn5, cardbtn6);

                    //System.out.println("Hand size: " + game.players.get(0).hand.size());

                    /*owner is player who won trick of last round*/
                    if (winngingbidplyr == 0) {
                        uistuff.displayPopup("Your won last trick! Play a card first", myStage);
                    } else {

                        /*call function to have AIs play*/
                        aiplayer.firstplay(winngingbidplyr, game, uistuff);

                        if (trickcount == 1){
                            uistuff.showTrumpCard(trumpcard);
                        }

                        /*call function to create losers lists excluding winning AI player*/
                        ArrayList<Integer> losers = new ArrayList<Integer>(game.loserArray(winngingbidplyr));

                        roundwinner = false; /*have losing AIs and user play*/
                        /*loop through losing AIs*/
                        aiplayer.play(losers, game, uistuff, false);

                        if (restart) {
                            uistuff.displayPopup("AI player " + (winngingbidplyr + 1) + " won the bid. Your turn! ", myStage);
                        } else {
                            uistuff.displayPopup("AI player " + (winngingbidplyr + 1) + " went first. \n Your turn to choose a card ", myStage);
                        }
                    }
                }
            }
        });



        EventHandler<ActionEvent> playcard = new EventHandler<ActionEvent>(){

            public void handle(ActionEvent event) {

                String cardtoplay = ((Button) event.getSource()).getId(); //get button that was pressed
                player.userPlayCard(winngingbidplyr, cardtoplay, uistuff, firstofround, game);


                if (winngingbidplyr == 0) {
                    uistuff.showTrumpCard(trumpcard);
                    /*set up array for losing AI players */
                    ArrayList<Integer> losers = new ArrayList<Integer>(game.loserArray(0));

                    System.out.println("losers size is: "  + losers.size());
                    //game.firstplayed = game.players.get(0).getPlayedcard(game.players.get(0).getPlayedcardSize() - 1); /*set first played of the round*/
                    /*most recently played card was the card added to the arraylist last*/

                    roundwinner = false; /*have losing AIs and user play*/
                    /*loop through losing AIs*/
                    aiplayer.play(losers, game, uistuff, false);
                }

                /*for this one trick, get highest card*/
               // Card highestcard = game.getHighCard();
                Card highestcard;
                //if (trickcount == 6) { // my highest card in round function sucks lmao
                   // highestcard = game.highestCardInRound();
               // }else{
                    highestcard = game.getHighCard();
                //}
                System.out.println("Highest card: " + highestcard.getSuit() + highestcard.getRank());
                owner = highestcard.getOwner();
                System.out.println("Owner: " + owner);

                /*the owner of the highest card gets all the cards just played*/

                System.out.println("first played: " + game.firstplayed.getID());
                System.out.println("Cards in round size: " + game.cardsinround.size());
                for (int i = 0; i < game.cardsinround.size(); i++) {
                    int cardowner = game.cardsinround.get(i).getOwner();
                    Card cardremoval = game.cardsinround.get(i);
                    System.out.println("Card: " + cardremoval.getID() + " - "+ "removed from " + cardremoval.getOwner());
                   // for (int j = 0 ; j < game.players.get(cardowner).hand.size(); j++){
                     //   System.out.println("card in hand: "+ game.players.get(cardowner).hand.get(j).getID());
                     //   System.out.println("last played card: " + game.players.get(cardowner).getPlayedcard(game.players.get(cardowner).getPlayedcardSize()-1).getID());
                    //}
                    game.players.get(cardowner).hand.remove(cardremoval);



                    updateplayerinfo();
                    game.cardsinround.get(i).setOwner(owner);

                    /*add card to winner's round deck and give them points for the card*/
                    System.out.println("Givin card to winner: "+ owner);
                    game.players.get(owner).addRounddeck(game.cardsinround.get(i));
                    game.players.get(owner).addToRoundScore(game.cardsinround.get(i).getRank().getVal());

                }
                game.cardsinround.clear();//newly ADDED
                winngingbidplyr = owner;
                uistuff.displayHandWinner(winngingbidplyr, continuebtn);

                /*check for game winners*/
                uistuff.updatescores();
                ArrayList<Integer> winners = new ArrayList<Integer>();
                for (int i = 0; i < numplayers; i++){
                    if (game.players.get(i).getOverallScore() == gamewin){ //check if any users have gotten to 7 points
                        winners.add(i);
                    }
                }
                if (winners.size() > 0){ //if winners size >0, there is at least 1 winner of the game
                    uistuff.showGameOver(winners, myStage, startbutton, exitbutton); return;
                }

                /*reset stuff*/
                firstofround = false;
                trickcount++;
                //game.cardsinround.clear();
            }
        };

        cardbtn1.setOnAction(playcard);
        cardbtn2.setOnAction(playcard);
        cardbtn3.setOnAction(playcard);
        cardbtn4.setOnAction(playcard);
        cardbtn5.setOnAction(playcard);
        cardbtn6.setOnAction(playcard);


        //replace param with name of your own picture. Make sure
        //its in the src folder
        Image pic = new Image("2D.jpg");
        ImageView v = new ImageView(pic);
        v.setFitHeight(200);
        v.setFitWidth(100);
        v.setPreserveRatio(true);

        btn.setGraphic(v);
        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(70));

        VBox paneCenter = new VBox(10, text, btn);
        paneCenter.setPadding(new Insets(10, 50, 50, 50));
        paneCenter.setSpacing(10);

        pane.setCenter(paneCenter);

        //pane.setTop(btn2); /*change scene button*/
        pane.setLeft(startbutton);
        pane.setRight(exitbutton);

        HBox newRoot = new HBox( 10, x, y, z); //for scene
        HBox startplayers = new HBox(); //start scene root


        paneCenter.getChildren().add(a); //line player2,3,4 buttons up
        paneCenter.getChildren().add(b);
        paneCenter.getChildren().add(c);

        scene = new Scene(pane, 400, 500); //default
        scene2 = new Scene(newRoot, 400, 500); //for scence


        sceneMap.put("welcome", scene); //put scenes in hash map of scenes
        sceneMap.put("gamePlay", scene2); //for change scenes

        primaryStage.setScene(sceneMap.get("welcome"));
        primaryStage.show();

    }
}

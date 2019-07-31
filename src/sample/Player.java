package sample;

import java.util.ArrayList;

public class Player{
    ArrayList<Card> hand; /*cards user currently owns*/
    private int overallscore;
    private int roundscore;
    int numcards;
    private int bid;
    int handpoints; /*int amount of how many points current hand is*/
    private ArrayList<Card> playedcards; /*list of cards user played*/
    private Deck rounddeck; /*cards the user won during the round*/
    private boolean bidsmudge;

    Player(){} //default constructor

    Player( int overallscore, int roundscore, ArrayList<Card> hand){
        this.hand = new ArrayList<Card>(hand);
        this.overallscore = 0;
        this.roundscore = roundscore;
        this.numcards = 0;
        this.bid = 0;
        this.handpoints = 0;
        this.playedcards = new ArrayList<Card>();
        this.rounddeck = new Deck(0, 6);
        this.bidsmudge = false;

    }

    Card getPlayedcard(int i ){ return this.playedcards.get(i); }

    void setPlayedcard(Card card){ this.playedcards.add(card); }

    int getPlayedcardSize(){ return this.playedcards.size(); }

    void addToRoundScore(int count ){ this.roundscore+= count; }

    void setRoundscore(int count){ this.roundscore = count; }

    int getOverallScore(){ return this.overallscore; }

    void addToOverallScore(int num ){ this.overallscore+= num;
    System.out.println("Adding to overall score: " + num); }

    int getRoundscore(){ return this.roundscore; }

    void removefromHand(Player player, String ID) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getID() == ID) {
                hand.remove(i);
                break;
            } else {
                continue;
            }
        }
    }

    void setHandOwner(Player player, int val){
        for (int i = 0; i < hand.size(); i++){
            player.hand.get(i).setOwner(val);

        }
    }


    int getBid(){ return this.bid; }

    void setBid(int bid){ this.bid = bid; if (bid == 5){ this.bidsmudge = true; }}

    Deck getRounddeck(){ return this.rounddeck; }

    void setRounddeck(Card card){ this.rounddeck.addCard(card);}

    void addRounddeck(Card card){ rounddeck.getDeck().add(card); }

    boolean isDeckEmpty(){
        if (this.hand.size() == 0){ return true; }
        else{ return false; }
    }

    void clearRoundDeck(){
        rounddeck.getDeck().clear();
    }

    void clearPlayedCards(){
        playedcards.clear();
    }

    void clearHand(){
        hand.clear();
    }

    boolean isRounddeckClear(){
        if (this.rounddeck.getDeck().size() == 0){
            return true;
        }else{
            return false;
        }
    }

    boolean isBidClear(){
        if (this.bid == 0){ return true; }
        else{ return false; }
    }


    void clearBid(){
        this.bid = 0;
    }

    void setHand(Card card){
        this.hand.add(card);
    }

    void setEntireHand(ArrayList<Card> deck){
        this.hand.addAll(deck);
    }

    void userPlayCard(int winngingbidplyr, String cardtoplay, UISetup uistuff, boolean firstofround, Pitch game){

        switch (cardtoplay) {
            case "btn1":
                Pitch.players.get(0).setPlayedcard(Pitch.players.get(0).hand.get(0));
                uistuff.showCard(Pitch.players.get(0).hand.get(0).getID(), 0);
                if (winngingbidplyr == 0) {
                    game.firstplayed = Pitch.players.get(0).hand.get(0);
                    if (firstofround){
                        driverClass.trumpcard = Pitch.players.get(0).hand.get(0);  game.trumpcard = driverClass.trumpcard; /*set trumpcard in pitch class as well*/}
                }
                game.cardsinround.add(Pitch.players.get(0).hand.get(0));
                Pitch.players.get(0).hand.remove(0);
                break;
            case "btn2":
                Pitch.players.get(0).setPlayedcard(Pitch.players.get(0).hand.get(1));
                uistuff.showCard(Pitch.players.get(0).hand.get(1).getID(), 0);
                if (winngingbidplyr == 0) {
                    game.firstplayed = Pitch.players.get(0).hand.get(1);
                    if (firstofround){
                        driverClass.trumpcard = Pitch.players.get(0).hand.get(1);  game.trumpcard = driverClass.trumpcard; /*set trumpcard in pitch class as well*/}
                }
                game.cardsinround.add(Pitch.players.get(0).hand.get(1));
                Pitch.players.get(0).hand.remove(1);
                break;
            case "btn3":
                Pitch.players.get(0).setPlayedcard(Pitch.players.get(0).hand.get(2));
                uistuff.showCard(Pitch.players.get(0).hand.get(2).getID(), 0);
                if (winngingbidplyr == 0) {
                    game.firstplayed = Pitch.players.get(0).hand.get(2);
                    if (firstofround){
                        driverClass.trumpcard = Pitch.players.get(0).hand.get(2); game.trumpcard = driverClass.trumpcard; /*set trumpcard in pitch class as well*/}
                }
                game.cardsinround.add(Pitch.players.get(0).hand.get(2));
                Pitch.players.get(0).hand.remove(2);
                break;
            case "btn4":
                Pitch.players.get(0).setPlayedcard(Pitch.players.get(0).hand.get(3));
                uistuff.showCard(Pitch.players.get(0).hand.get(3).getID(), 0);
                if (winngingbidplyr == 0) {
                    game.firstplayed = Pitch.players.get(0).hand.get(3);
                    if (firstofround){
                        driverClass.trumpcard = Pitch.players.get(0).hand.get(3);  game.trumpcard = driverClass.trumpcard; /*set trumpcard in pitch class as well*/}
                }
                game.cardsinround.add(Pitch.players.get(0).hand.get(3));
                Pitch.players.get(0).hand.remove(3);

                break;
            case "btn5":
                Pitch.players.get(0).setPlayedcard(Pitch.players.get(0).hand.get(4));
                uistuff.showCard(Pitch.players.get(0).hand.get(4).getID(), 0);
                if (winngingbidplyr == 0) {
                    game.firstplayed = Pitch.players.get(0).hand.get(4);
                    if (firstofround){
                        driverClass.trumpcard = Pitch.players.get(0).hand.get(4); game.trumpcard = driverClass.trumpcard; /*set trumpcard in pitch class as well*/}
                }
                game.cardsinround.add(Pitch.players.get(0).hand.get(4));
                Pitch.players.get(0).hand.remove(4);

                break;
            case "btn6":
                Pitch.players.get(0).setPlayedcard(Pitch.players.get(0).hand.get(5));
                uistuff.showCard(Pitch.players.get(0).hand.get(5).getID(), 0);
                if (winngingbidplyr == 0) {
                    game.firstplayed = Pitch.players.get(0).hand.get(5);
                    if (firstofround){
                        driverClass.trumpcard = Pitch.players.get(0).hand.get(5); game.trumpcard = driverClass.trumpcard; /*set trumpcard in pitch class as well*/}
                }
                game.cardsinround.add(Pitch.players.get(0).hand.get(5));
                Pitch.players.get(0).hand.remove(5);
                break;
        }
    }


}

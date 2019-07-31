package sample;

import javafx.application.Application;
import java.io.*;
import java.util.*;

/*to do:
- get ais to play cards
- fix end of round and scoring function
- display winning screen
- test cases
 */

/*if playing a card thats not in hand just dont display any card*/

public class GameInfo {

    static ArrayList<Card> allcards; //array to hold all 52 cards, need to turn into arraylist?
    Deck gamedeck;
    static Rank[] rankarray = {Rank.ACE, Rank.KING, Rank.QUEEN, Rank.JACK, Rank.TEN, Rank.NINE,
            Rank.EIGHT, Rank.SEVEN, Rank.SIX, Rank.FIVE, Rank.FOUR, Rank.THREE, Rank.TWO};
    static Suit[] suitarray = {Suit.HEART, Suit.DIAMOND, Suit.SPADE, Suit.CLUB};


    GameInfo(int players){
        allcards = new ArrayList<Card>(52);//new Card[52]
        createcards();

    }

    boolean addtoAllCards(Card num){
        if (allcards.size() >= 52){
            return false;
        }else{
            allcards.add(num);
            return true;
        }
    }

    static void clearArray(){
        allcards.clear();
    }

    void removeallcards(int num){
        this.allcards.remove(num);
    }

    Card removeSpecificCard(Card card){
        this.allcards.remove(card);
        return card;
    }



    public static void createcards(){
        int count = 0;
        for (int i = 0; i < 4; i++){
            for (int j =0; j < 13; j++){
                Card card = new Card(suitarray[i], rankarray[j]); /*Constructor format: Card(suit, rank) */
                allcards.add(card);
                count++;
            }
        }
        System.out.println("create cards final: " + count);
    }

    int  allcardssize(){
        return this.allcards.size();
    }

    public static enum Suit{
        HEART("heart", "H"), DIAMOND("diamond", "D"), SPADE("spade", "S"), CLUB("club", "C");

        private String val;
        private String ID;

        Suit(String val, String ID){
            this.val = val;
            this.ID = ID;
        }

        public String getSuit(){
            return this.val;
        }


        public String getID(){
            return this.ID;
        }

    }


    public enum Rank{
        ACE(4 , "A", 13) , KING(3, "K", 12), QUEEN(2, "Q", 11), JACK(1, "J", 10), TEN(10, "10", 9),
        NINE(0, "9", 8), EIGHT(0 , "8", 7), SEVEN(0 ,"7", 6), SIX(0, "6", 5), FIVE(0, "5", 4),
        FOUR(0, "4", 3), THREE(0, "3", 2), TWO(0, "2", 1);

        private int val;
        private String ID;
        private int rank;

        Rank(int val, String ID, int rank ){
            this.val = val;
            this.ID = ID;
            this.rank = rank;
        }

        public int getVal(){
            return this.val;
        }

        public String getID(){
            return this.ID;
        }

        public int getRankVal(){ return this.rank; }

    }

}

class Deck{
    private ArrayList<Card> cards;
    private int numcards;

    Deck(int num, int numcards){
        cards = new ArrayList<Card>(num);
        this.numcards = numcards;
    }

    void addCard(Card card){
        cards.add(card);
    }

    ArrayList<Card> getDeck(){ return cards; }

    int deckSize(){
        return cards.size();
    }

    void clearDeck(){
        cards.clear();
    }

    int getNumcards(){ return this.numcards; }

    boolean  removeFromDeck(Card card){
        if (deckSize() == 0){ return false; }
        for (int i = 0; i < deckSize(); i++){
            if (getDeck().get(i).getID() == card.getID()){
                this.cards.remove(card);
                return true;
            }
        }
        return false;
    }

}




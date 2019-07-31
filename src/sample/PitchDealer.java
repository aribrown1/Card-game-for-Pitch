package sample;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

//put these in new files
interface DealerType{
    public Dealer createDealer();
}


interface Dealer{

    public ArrayList<Card> dealHand();
}

public class PitchDealer implements Dealer { /*made static so can be called in outside class*/

    private Integer numcardstodeal;
    ArrayList<sample.Card> temphand;
    int cardsremoved;
    ArrayList<Card> cardstodeal;
    int numplayers;

    PitchDealer(int num) {
        numcardstodeal = num;
        temphand = new ArrayList<Card>();
        cardsremoved = 0;
        cardstodeal = new ArrayList<Card>(GameInfo.allcards); //copy allcards to new arraylist
        numplayers = Pitch.numplayers;
    }

    public int checkCardsToDeal(){
        return this.cardstodeal.size();
    }

    public int getNumcardstodeal(){
        return this.numcardstodeal;
    }

    public void setnumcardstodeal(int val){
        this.numcardstodeal = val;
    }
    int numcardstodeal(){
        return this.numcardstodeal;
    }

    int getNumplayers(){
        return this.numplayers;
    }

    int cardsarraysize(){
        return this.cardstodeal.size();
    }

    int getCardsremoved(){
        return this.cardsremoved;
    }

    void addtoCardsRemove(int add){
        this.cardsremoved+= add;
    }

    void copyArray(ArrayList<Card> arraylist){
        this.cardstodeal.clear();
        this.cardstodeal.addAll(arraylist);
        System.out.println("copy array cards to deal size:" + cardstodeal.size());
    }


    public ArrayList<Card> dealHand() {

        Collections.shuffle(cardstodeal); /*shuffle deck*/
        temphand.clear();

        System.out.println("Deal hand cardstodeal: " + cardstodeal.size());
        System.out.println("Cardstodeal size - cards removed: " + cardstodeal.size() + " - 1  = " + (cardstodeal.size()-1 ));

        for (int j = 0; j < 6; j++) {
            Random rand = new Random();
            /*shuffle deck as you deal cards by choosing a random int*/
            int n = rand.nextInt((cardstodeal.size()-1));
            System.out.println("Random n: " + n);
            System.out.println("dealing: "+cardstodeal.get(n).getID() );
            temphand.add(cardstodeal.get(n));
            cardstodeal.remove(n); /*remove that one from the cards to deal so its not repeated*/
            cardsremoved+= 1;
            numcardstodeal--;

        }

        System.out.println("Deal hand cardstodeal: " + cardstodeal.size());
        System.out.println("Cardstodeal size - cards removed: " + cardstodeal.size() + " - 1  = " + (cardstodeal.size()-1 ));

        System.out.println("cards removed: " + cardsremoved);

        return temphand;
    }


    public void fastRedeal(){ /*function to start over and redeal cards if all bids are 0*/
        /*put cards back into deck*/

        System.out.println("festRedeal cardstodeal: " + cardstodeal.size());
        for (int i =0; i < numplayers; i++){
            for (int j =0; j < Pitch.players.get(i).hand.size() ; j++) {
                cardstodeal.add(Pitch.players.get(i).hand.get(j));
                //Pitch.players.get(i).hand.get(j).setOwner(i);
                cardsremoved--;
            }
            Pitch.players.get(i).hand.clear(); /*empty all cards from hand*/
        }


        /*shuffle cards*/
        Collections.shuffle(cardstodeal);

        /* now deal cards back out*/
        for (int i =0; i < numplayers; i++){
            Pitch.players.get(i).setEntireHand(dealHand());
        }
    }

}

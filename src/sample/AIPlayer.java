package sample;
import java.util.ArrayList;

public class AIPlayer extends Player{

    Pitch game;
    AIPlayer( ){
       // game = new Pitch(2);
    }

    int getOwnerofCard(Card card){
        return card.getOwner();
    }


    void play(ArrayList<Integer> losers, Pitch game, UISetup uistuff, boolean roundwinner){
        for (int i = 0; i < losers.size(); i++) {
            Card playedcard = game.AIplaycard(game.players.get(losers.get(i)), roundwinner);
            System.out.println("AI playing: " + losers.get(i));
            playedcard.setOwner(losers.get(i));
            game.players.get(losers.get(i)).setPlayedcard(playedcard); /*set played card*/
            uistuff.showCard(playedcard.getID(), losers.get(i));
            System.out.println("Loser Adding " + playedcard.getID() + " to cards in round");
            game.cardsinround.add(playedcard);

        }
    }

    void firstplay(int player, Pitch game, UISetup uistuff){
        Card playcard = game.AIplaycard(Pitch.players.get(player), true);
        game.players.get(player).setPlayedcard(playcard);
        System.out.println("AI playing: " + player);
        playcard.setOwner(player);
        driverClass.trumpcard = playcard;
        game.firstplayed = playcard;
        uistuff.showCard(playcard.getID(), player);
        System.out.println("Firstplay Adding " + playcard.getID() + " to cards in round");
        game.cardsinround.add(playcard);

    }

    public ArrayList<Card> getlowcards(Player player){
        ArrayList<Card> lowcard = new ArrayList<Card>();
        for (int i = 0; i < player.hand.size(); i++){
            if (player.hand.get(i).getRank().getRankVal() < 5){ //collect 2s, 3s, and 4s
                if (player.hand.get(i).getSuit().getSuit() != game.trumpcard.getSuit().getSuit()) { //only add if its not a trump card
                    lowcard.add(player.hand.get(i));
                }
            }
        }

        return lowcard;
    }

    public ArrayList<Card> gethighcards(Player player){
        ArrayList<Card> highcard = new ArrayList<Card>();
        for (int i = 0; i < player.hand.size(); i++){
            if (player.hand.get(i).getRank().getRankVal() >= 10 ){ //collect 2s, 3s, and 4s
                if (player.hand.get(i).getSuit().getSuit() != game.trumpcard.getSuit().getSuit()) { //only add if its not a trump card
                    highcard.add(player.hand.get(i));
                }
            }
        }

        return highcard;
    }


}

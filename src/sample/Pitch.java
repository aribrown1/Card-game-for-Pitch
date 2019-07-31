package sample;
import java.util.Random;
import java.util.ArrayList;
import sample.GameInfo;
import java.util.Collections;



public class Pitch implements DealerType{
    static int numplayers;
    int numAI, gamepointcount, rankval,  highowner, lowowner, jackowner, gameowner, gamepointwinner, firstwinner, numgamepntswon, count, rank, counter;
    GameInfo info;
    static ArrayList<Player> players; /*array of player classes*/
    Card trumpcard, highest, lowest, jack, gamecard, highcard, lowcard, jackcard, firstplayed, playedcard;
    ArrayList<Card> cardsinround;
    AIPlayer ai;
    boolean highneeded; boolean lowneeded; boolean jackneeded; boolean gameneeded;
    Dealer dealer;
    ArrayList<Card> equalcards; ArrayList<Card> firstplayedcards; ArrayList<Card> trumpcardsplayed;


    Pitch(int numplayers){

        this.numplayers = numplayers;
        info = new GameInfo(numplayers);
        highest = new Card(GameInfo.suitarray[0], GameInfo.rankarray[1]); /*filler*/
        lowest = new Card(GameInfo.suitarray[0], GameInfo.rankarray[1]); /*filler*/
        jack = new Card(GameInfo.suitarray[0], GameInfo.rankarray[12]); /*set initial jack rank low*/
        gamecard = new Card(GameInfo.suitarray[0], GameInfo.rankarray[3]); /*set initial jack rank low*/
        gamepointcount = 0;
        rankval = 0;
        players = new ArrayList<Player>(numplayers);
        cardsinround = new ArrayList<Card>();
        ai = new AIPlayer();
        dealer = createDealer();

        for (int i =0; i < numplayers; i++) { //create players
            players.add(new Player( 0, 0, dealer.dealHand())); /*figure out how to get names later*/
            for (int j = 0; j < 6; j++) {
                players.get(i).hand.get(j).setOwner(i);  /*assign card owner number*/
            }
            players.get(i).numcards = 6;
            players.get(i).setBid(0);
        }

        if (numplayers == 2){ this.numAI = 1; }
        if (numplayers == 3){ this.numAI = 2; }
        if (numplayers == 4){ this.numAI = 3; }

        highneeded = true; lowneeded = true; jackneeded = true; gameneeded = true;
        count = 0;
        playedcard = new Card(GameInfo.suitarray[0], GameInfo.rankarray[1]);
        equalcards = new ArrayList<Card>();
        firstplayedcards = new ArrayList<Card>();
        trumpcardsplayed = new ArrayList<Card>(); //list of all trump cards already playedin round
        rank = 2;
        counter = 0;

    }


    public Dealer createDealer() {
        return new PitchDealer(6);
    }

    public void bid(int playerbid){
        players.get(0).setBid(playerbid); /*set user player bid*/

    }


    public boolean getBids(int playerbid){ /*calls playerbid and aibid functions and checks if all bids are zero*/
        boolean bids = true;
        if (playerbid != 2 && playerbid != 3 && playerbid != 4 && playerbid != 5) {
            playerbid = 0; //default to pass
        }
        System.out.println(playerbid);
        bid(playerbid);
        AIbid();


        for (int i = 0; i < numplayers; i++) {
            if (Pitch.players.get(i).getBid() != 0) {
                bids = false; return bids;
            }
        }

        return bids;
    }

    public int getWinningBid(){
        int winning = 0;
        int playernum = 0;
        for (int i = 0; i < numplayers; i++) {
            if (Pitch.players.get(i).getBid()>= winning ) { /*>= if 2 players had same # winning bid, just choose one to go first*/
                winning = Pitch.players.get(i).getBid();
                playernum = i;
            }
        }
        return playernum;
    }


    public boolean isinStack(Player player, String ID){
        boolean isEqual = false;
        for (int i = 0; i < player.hand.size(); i++) {
            if (ID == player.hand.get(i).getID()) {
                System.out.println("is true");
                return true;
            }else{ continue; }
        }
        System.out.println("returning false");
        return false;
    }


    public ArrayList<Card> getHand(int player){

        return players.get(player).hand;
    }


    public int countHand(Player player){ /*count points in hand*/
        /*You get the most game points in the round. All 10s are worth 10, aces are
        worth 4, kings are worth 3, queens are worth 2, and jacks are worth 1. Other cards
        aren’t worth anything.*/
        int count = 0;
        System.out.println("Player numcards: " + player.numcards);
        for (int i =0; i < player.numcards; i++){
            switch(player.hand.get(i).getRank()){
                case TEN: count += 10; break;
                case ACE: count += 4;  break;
                case KING: count += 3; break;
                case QUEEN: count += 2; break;
                case JACK: count+= 1; break;
                case NINE:
                case EIGHT:
                case SEVEN:
                case SIX:
                case FIVE:
                case FOUR:
                case THREE:
                case TWO: count += 0; break;
            }
        }
        System.out.println("done");
        return count;
    }

    public void newOverallScore(){
        for (int i =0; i < numplayers; i++){
            players.get(i).addToOverallScore(players.get(i).getRoundscore());
        }
    }

    public Card highestCardInRound(){

        int max = 0;
        for (int i =0; i < cardsinround.size(); i++){ /*find highest ranking card*/
            if (cardsinround.get(i).getRank().getRankVal() > max ){
                if (cardsinround.get(i).getSuit() == trumpcard.getSuit()) { /*make sure card follows trump suit*/
                    max = cardsinround.get(i).getRank().getRankVal();
                    highest = cardsinround.get(i);

                }
            }
        }

        if (max != 0){ return highest; }
        System.out.println("returning highest: " + highest.getID());

        max = firstplayed.getRank().getRankVal();
        if (max == 0) {
            for (int i = 0; i < cardsinround.size(); i++) {/*if max is still 0, no trump cards were played so highest card with leading suit wins*/
                if (cardsinround.get(i).getRank().getRankVal() > max ) {
                    if (cardsinround.get(i).getSuit() == firstplayed.getSuit()) {
                        max = cardsinround.get(i).getRank().getRankVal();
                        highest = cardsinround.get(i);
                    }
                }
            }
        }

        if (max != 0){ return highest; }
        System.out.println("returning highest: " + highest.getID());

        if (max == 0){ //if max is still zero, highest leading card is the first played
            highest = firstplayed; return highest;

        }

        return highest;

    }

    public Card getHighCard(){
        int max = 0;


        int counter = 0;  int ownerofcard = 0; ArrayList<Card> card = new ArrayList<Card>();
        for (int i = 0; i < cardsinround.size(); i++){
            if (cardsinround.get(i).getSuit().getSuit().equals(trumpcard.getSuit().getSuit())){
                counter++;
                ownerofcard = cardsinround.get(i).getOwner();
                card.add(cardsinround.get(i));
            }
        }

        if (counter == 1){
            for (int i = 0; i < players.get(ownerofcard).hand.size(); i++){
                if (players.get(ownerofcard).hand.get(i).getID().equals(card.get(0))){
                    highest = players.get(ownerofcard).hand.get(i); return highest;
                }
            }
        }

        int count = 0; //since no trumps were played, check to see if anyone matched first cards suit. if they did not, first card wins hand
        for (int i = 0; i <cardsinround.size(); i++){
            if (cardsinround.get(i).getSuit().getSuit() == firstplayed.getSuit().getSuit()){
                count++;
                System.out.println("we are here");
            }
        }
        if (count == 1){
            System.out.println("we are here!! firstplayed suit matching is one ");
            highest = firstplayed;
            return highest;
        }

        for (int i =0; i < cardsinround.size(); i++){ /*find highest ranking card*/
            if (cardsinround.get(i).getRank().getRankVal() > max ){
                if (cardsinround.get(i).getSuit() == trumpcard.getSuit()) { /*make sure card follows trump suit*/
                    max = cardsinround.get(i).getRank().getRankVal();
                    highest = cardsinround.get(i);
                }
            }
        }

        if (max == 0) {
            max = firstplayed.getRank().getRankVal();
            for (int i = 0; i < cardsinround.size(); i++) {/*if max is still 0, no trump cards were played so highest card with leading suit wins*/
                if (cardsinround.get(i).getRank().getRankVal() >= max ) {
                    if (cardsinround.get(i).getSuit().getSuit() == firstplayed.getSuit().getSuit()) {
                        max = cardsinround.get(i).getRank().getRankVal();
                        highest = cardsinround.get(i);
                    }
                }
            }
        }

        if (max == firstplayed.getRank().getRankVal()){ highest = firstplayed; return highest; }


        if (max == 0){ /*if max is still 0, no trump cards were played and no other leading suits were played so leading suit wins*/
            highest = firstplayed;
        }

        return highest;
    }

    public Card getLowCard(){
        int min = 100;
        for (int i =0; i < cardsinround.size(); i++){ /*find lowest ranking card*/
            if (cardsinround.get(i).getRank().getRankVal() < min ){
                if (cardsinround.get(i).getSuit() == trumpcard.getSuit()) { /*make sure card follows trump suit*/
                    min = cardsinround.get(i).getRank().getRankVal();
                    lowest = cardsinround.get(i);
                }
            }
        }

        if (min == 100) {
            for (int i = 0; i < cardsinround.size(); i++) {/*if max is still 0, no trump cards were played so highest card with leading suit wins*/
                if (cardsinround.get(i).getRank().getRankVal() < min) {
                    if (cardsinround.get(i).getSuit() == firstplayed.getSuit()) {
                        min = cardsinround.get(i).getRank().getRankVal();
                        lowest = cardsinround.get(i);

                    }
                }
            }
        }

        if (min == 100){ /*if min is still 100, no trump cards were played and we need to find the lowest card regardless of suit*/
            for (int i =0; i < cardsinround.size(); i++){
                if (cardsinround.get(i).getRank().getRankVal() < min ){
                    min = cardsinround.get(i).getRank().getRankVal();
                    lowest = cardsinround.get(i);

                }
            }
        }

        return lowest;
    }

    public Card getJackCard(){
        int jackmax = 0;
        String jackstr = "J";

        System.out.println("in jackcard trumpcard suit is: " + trumpcard.getSuit().getSuit());
        for (int i =0; i < cardsinround.size(); i++){
            jack = cardsinround.get(i);
            if (jackstr.equals(jack.getRank().getID())){  /*if card is a jack*/
                if (jack.getSuit().getSuit() == trumpcard.getSuit().getSuit()) { /*make sure card follows trump suit*/
                        return jack; //we found jack in played cards*/
                }
            }
        }

        if (jackmax == 0){ /*if jack max is STILL zero, no jacks were played*/
            System.out.println("returning null in jack card");
            return null;
        }

        return jack;
    }

    public Card getGamePoint(){ //find highest amount of points

        System.out.println("Game card is starting off as:" + gamecard.getRank() + gamecard.getSuit()) ;

        for (int i =0; i < numplayers; i++){
            Card playedcard = players.get(i).getPlayedcard(players.get(i).getPlayedcardSize()-1); //get last played card
            System.out.println("Playedcard: " + playedcard.getRank() + playedcard.getSuit());
            //find card in rankarray and get the points for it
            for (int k = 0; k < GameInfo.rankarray.length; k++){
                if (playedcard.getRank().getID() == GameInfo.rankarray[k].getID()){
                    rankval = GameInfo.rankarray[k].getVal();
                    System.out.println("Rankval: " + rankval);
                }
            }

            //now compare to largest gamepoint count so far
            if (rankval  > gamepointcount){
                gamepointcount = rankval;
                //gamecard = players.get(i).playedcards.;
            }

        }

        return gamecard;
    }

    public int overallGamePoint(){
        /*go through all cards played in round*/

        Card playermax = new Card(GameInfo.suitarray[2], GameInfo.rankarray[12]); /*set to lowest rank of 2 to start off */
        int count = 0;
        int max = 0;
        int winningplayer = 0;
        for (int i =0 ; i < numplayers; i ++){
            Deck tempdeck = players.get(i).getRounddeck();
            for (int j = 0; j < tempdeck.getDeck().size(); j++) {
                count += tempdeck.getDeck().get(j).getRank().getRankVal(); //sum all the rank values in that hand
            }

            if (count > max ){ /*if the sum of all rankvals is greater then current max, make it the nex max and change winning player*/
                max = count;
                winningplayer = i;
            }
        }

        numgamepntswon = max; /*set number of game points won*/
        System.out.println("Winnging player: " + winningplayer);
        return winningplayer;
    }

    void resetRoundDecks() {
        for (int i = 0; i < numplayers; i++) {
            players.get(i).clearRoundDeck();
        }
    }

    void endOfRound(int winngingbidplyr){

        ArrayList<Card> allplayed = new ArrayList<Card>();
        for (int i =0; i < numplayers; i++){
            /*go through all played cards and add to deck*/
            int sizeofdeck = players.get(i).getPlayedcardSize();
            System.out.println("Player " + i + "playcard size: " + sizeofdeck);
            for (int j = 0; j < sizeofdeck; j++){
                allplayed.add(players.get(i).getPlayedcard(j));
            }
        }
        /*so we can use cardsinround array to get final score*/
        cardsinround.clear();
        cardsinround.addAll(allplayed);


        /*now we have an array of all the cards played, and owners are assigned to each card by who won that card*/
        /*now assess and display winners*/
        highcard = getHighCard();
        lowcard = getLowCard();
        jackcard = getJackCard();
        gamepointwinner = overallGamePoint(); /*return player who scored the most points*/

        System.out.println("Highcard: " + highcard.getID());
        System.out.println("Lowcard: " + lowcard.getID());
        if (jackcard != null){
        System.out.println("Jackard: " + jackcard.getID()); }
        System.out.println("Overall owner: " + gamepointwinner);


        /*make sure pitcher did not overbid*/
        //first winner = pitcher
        int bid = players.get(firstwinner).getBid();
        int pointscount = 0;
        boolean correctbid = false;
        boolean smudge;


        /*get owners of winning cards*/
        highowner =  highcard.getOwner();
        lowowner = lowcard.getOwner();
        if (jackcard!= null){ jackowner = jackcard.getOwner();  }
        else{ jackowner = 10; }
        gameowner = gamepointwinner;

        System.out.println("end of game info: ");
        System.out.println(firstwinner);
        System.out.println(highowner);
        System.out.println(highcard.getOwner());

        if (firstwinner == (highcard.getOwner())){
            pointscount++;
        }
        if (firstwinner == (lowcard.getOwner())){
            pointscount++;
        }
        if (jackcard != null) {
            if (firstwinner == (jackcard.getOwner() )) {
                pointscount++;
            }
        }
        if (firstwinner == (gameowner)){
            pointscount++;
        }
        if (pointscount == 4){ smudge = true; }


        if (bid <= pointscount) { correctbid = true; }
        else { correctbid = false;  }


        /*dish out points based on if pitcher won or not */
        /*assess bids and deduct from inaccurate bids*/
        if (correctbid){
            players.get(highowner).addToOverallScore(1);
            players.get(lowowner).addToOverallScore(1);
            if (jackcard != null){ /*make sure a jack was actually played*/
                players.get(jackowner).addToOverallScore(1);
                System.out.println("Jack card owner is: " + (jackcard.getOwner()+1));
            }
            players.get(gameowner).addToOverallScore(1); /*add rank val to winner of game point*/

        } else if (!correctbid){
            /*figure out if owner of any cards is pitcher that lost bid*/
            int pitchercards = 0;

            for (int i = 0; i < 4; i ++){
                if (firstwinner == highowner){ pitchercards++; highneeded = false;  }
                if (firstwinner == lowowner){ pitchercards++; lowneeded = false;  }
                if (jackcard != null){
                    if (firstwinner == jackowner){ pitchercards++; jackneeded = false; }
                }
                if (jackowner == 10){ /*jack was not played put played needed more points??? */
                    jackneeded = true;
                }
                if (firstwinner == gameowner){ pitchercards++; gameneeded = false; }
            }

            System.out.println("First winner bid: " + players.get(firstwinner).getBid() + "- "+ -1*(players.get(firstwinner).getBid()));
            players.get(firstwinner).addToOverallScore((-1*(players.get(firstwinner).getBid())));

            /*now we cards that were needed = true*/
            if (highowner!= firstwinner){
                players.get(highowner).addToOverallScore(1);
           }

            if (lowowner != firstwinner){
                players.get(lowowner).addToOverallScore(1);
            }

            if (jackcard != null) {
                if (jackowner != firstwinner) {
                    players.get(jackowner).addToOverallScore(1);
                }
            }
          /*if no jack was played but played needed the points, deduct points*/

            if (gameowner != firstwinner){
                players.get(gameowner).addToOverallScore(1);
            }

        }

        /*check for smudge*/
        for (int i = 0; i < numplayers; i++){
            if (players.get(i).getOverallScore() == 4){ //got all 4 points
                if (players.get(i).getBid() == 5){
                    players.get(i).addToOverallScore(1); //add one more to get 5 points*/
                }
            }
        }

        /*display new overall scores*/

        System.out.println("High card owner is: " + (highcard.getOwner()+1));
        System.out.println("Low card owner is: " + (lowcard.getOwner()+1));
        System.out.println("Game card owner is: " + (gamepointwinner));

    }

   public ArrayList<Integer> loserArray(int winngingbidplyr) {
       /*set up array for losing players */
       ArrayList<Integer> losers = new ArrayList<Integer>();
       int count = 0;
       if (winngingbidplyr!= 0){ count = numplayers-1; } /*AI player is winning player so we need to exclude one from array*/
        count = numplayers;  /*user is winner so need to put all AIs in array*/
       for (int i = 1; i < count; i++) { /*start from 1 so we dont add user*/
           if (i == winngingbidplyr) {
               continue;
           } /*skip over adding winning bid player*/
           losers.add(i);
           System.out.println("Adding to failure list player: " + i);
       }

       return losers;
   }

   public int AIbiddecision(Player player){
        int bid = 0;
        if (player.handpoints >= 10){ bid+=2; }
        for (int i = 0; i < player.numcards; i++){
            switch(player.hand.get(i).getRank().getID()){
                case "jack": bid += 1; break;
                case "ace": bid +=3; break;
                case "queen": bid +=1; break;
                case "king": bid += 2; break;
                case "two": bid += 3; break;
                case "three": bid += 2; break;
                case "four": bid +=1; break;
            }
        }

       return bid;
   }

    public Card AIplaycard(Player player, Boolean roundwinner) { //changed to referance boolean
        boolean isEqual;


        equalcards.clear();
        firstplayedcards.clear();
        trumpcardsplayed.clear();


        if (roundwinner == true && player.hand.size() == 6){
            //we are setting the trumpcard for the game.
            int heartcount = 0; int clubcount = 0; int diamondcount = 0; int spadecount = 0;
            for (int i = 0; i < player.hand.size(); i++){
                switch(player.hand.get(i).getSuit().getSuit()){
                    case ("heart"): heartcount++; continue;
                    case("diamond"): diamondcount++; continue;
                    case("spade"): spadecount++; continue;
                    case("club"): clubcount++; continue;
                }
            }

            int max = heartcount;
            String maxcards = "heart";

            if (diamondcount > max) {
                max = diamondcount;
                maxcards = "diamond";
            }
            if (spadecount > max) {
                max = spadecount;
                maxcards = "spade";
            }
            if (clubcount > max) {
                max = clubcount;
                maxcards = "club";
            }


            rank = 2;
            for (int i = 0; i < player.hand.size(); i++){
                if (player.hand.get(i).getRank().getRankVal() >= rank){
                    if (maxcards.equals(player.hand.get(i).getSuit().getSuit())) {
                        playedcard = player.hand.get(i);
                        rank = player.hand.get(i).getRank().getRankVal();
                    }
                }
            }

           // System.out.println("Returning: " + playedcard.getID());
            player.hand.remove(playedcard);
            return playedcard;
        }

       // System.out.println("AI playing: "+ player);
        for (int j = 0; j < player.hand.size(); j++) {
            /*see if suits are equal for both trumpcard and overalltrump card*/
            isEqual = driverClass.trumpcard.getSuit().equals(player.hand.get(j).getSuit());
            if (isEqual) {
                equalcards.add(player.hand.get(j));

            }
        }


        if (roundwinner == true) { /*if AI is the roundwinner, choosing a card isnt as complicated*/

            if (equalcards.size() > 0){ //if the roundwinner
                int rank = 2;
                for (int i = 0; i < equalcards.size(); i++){
                    if (equalcards.get(i).getRank().getRankVal() >= rank){
                        if (trumpcard.getSuit().getSuit().equals(equalcards.get(i).getSuit().getSuit())) {
                            playedcard = equalcards.get(i);
                            rank = equalcards.get(i).getRank().getRankVal();

                        }
                    }
                }
                player.hand.remove(playedcard);
                return playedcard;
            }else{

                rank = 2;
                for (int i = 0; i < player.hand.size(); i++){
                    if (player.hand.get(i).getRank().getRankVal() >= rank){
                            playedcard = player.hand.get(i);
                            rank = player.hand.get(i).getRank().getRankVal();
                    }
                }
                player.hand.remove(playedcard);
                return playedcard;
            }

        }else{ //not round winner

            /*create trump cards played*/
            for (int i = 0; i < cardsinround.size(); i++){
                if (cardsinround.get(i).getSuit().getSuit() == trumpcard.getSuit().getSuit()){
                    trumpcardsplayed.add(cardsinround.get(i));
                }
            }


            if (equalcards.size() > 0){ //player has trump card
                //System.out.println("has at least 1 trunmp suit");
                if (trumpcardsplayed.size() == 0){
                    /*find highest trump card and play it */
                    System.out.println("trump card has not been played. playing highes trump card");
                    rank = 2; counter = 0;
                    for (int i = 0; i < equalcards.size(); i++){
                        if (equalcards.get(i).getRank().getRankVal() >= rank){
                            if (trumpcard.getSuit().getSuit().equals(equalcards.get(i).getSuit().getSuit())) {
                                playedcard = equalcards.get(i);
                                rank = equalcards.get(i).getRank().getRankVal();
                                counter+= 1;
                            }
                        }
                    }
                    if (counter == equalcards.size()){ counter = counter - 1; }
                    playedcard = equalcards.get(counter);
                    player.hand.remove(playedcard);
                    return playedcard;
                }else{ /*if trumpcard has already been played*/
                    rank = 2;
                    for (int i = 0; i < trumpcardsplayed.size(); i++){ //get highest ranking trump card
                        if (trumpcardsplayed.get(i).getRank().getRankVal() > rank){
                            rank = trumpcardsplayed.get(i).getRank().getRankVal();
                        }
                    }

                    /*compare highest ranking trump card played with cards player has*/
                    boolean found = false; int count1 = 0;
                    for (int i = 0; i < equalcards.size(); i++){
                        if (equalcards.get(i).getRank().getRankVal() > rank){
                            playedcard = equalcards.get(i);
                            rank = equalcards.get(i).getRank().getRankVal();
                            found = true;
                            count1+=1;
                        }
                    }
                    if (found) {
                        //System.out.println("found higher ranking trump card");
                        playedcard = equalcards.get(count1-1);
                        player.hand.remove(playedcard);
                        return playedcard;
                    }
                    if (!found){ //we couldnt find a card higher rank than a trump card already played, play throw away card*/
                       // System.out.println("playing throw away card");
                        for (int i = 0; i < player.hand.size(); i++){
                            if (player.hand.get(i).getSuit().getSuit() == trumpcard.getSuit().getSuit()){
                                continue;
                            }else{
                                if (player.hand.get(i).getRank().getRankVal() < 10){
                                    playedcard = player.hand.get(i);
                                    player.hand.remove(playedcard); return playedcard;
                                }else{
                                    playedcard = player.hand.get(i);
                                    player.hand.remove(playedcard); return playedcard;
                                }
                            }
                        }

                    }
                }
            }
            else{ //have no equal trump cards
                //populate array of cards played that match the suit of first card played
                for (int i = 0; i < cardsinround.size(); i++){
                    if (cardsinround.get(i).getSuit().getSuit() == firstplayed.getSuit().getSuit()){
                        firstplayedcards.add(cardsinround.get(i));
                    }
                }
                /*check to see if matching suits in player's deck*/
                boolean matchingsuit = false;
                for (int i = 0; i < player.hand.size(); i++){
                    if (player.hand.get(i).getSuit().getSuit() == firstplayed.getSuit().getSuit()){
                        playedcard = player.hand.get(i);
                        player.hand.remove(playedcard);
                        return playedcard;
                    }
                }

                if (!matchingsuit){ //dont have trump card or matching suit so play low value card
                    for (int i = 0; i < player.hand.size(); i++){
                        if (player.hand.get(i).getRank().getRankVal() <= 10){
                            if (player.hand.get(i).getSuit().getSuit() != trumpcard.getSuit().getSuit()){
                                playedcard = player.hand.get(i);
                                player.hand.remove(playedcard);
                                return playedcard;
                            }
                        }
                    }
                }

            }
        }

        playedcard = player.hand.get(0);
    return playedcard;
    }


    public void AIbid(){ /*can either bid 2, 3, 4, or smudge*/
        /* first, total points for each AI*/
        int count;
        for (int i = 1; i < (numAI+1); i++){ /*AI will be player[1] - players[numAI] */
            int points = countHand(players.get(i));
            players.get(i).handpoints = points;
            int bid = AIbiddecision(players.get(i));
            if (bid > 5){ bid = 5; }
            players.get(i).setBid(bid);

        }

    }

}



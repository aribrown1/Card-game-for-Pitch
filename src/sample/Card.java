package sample;

public class Card {
    GameInfo.Suit suit;
    GameInfo.Rank rank;
    String ID;
    private int owner;

    Card(GameInfo.Suit suit, GameInfo.Rank rank) {
        this.suit = suit;
        this.rank = rank;
        this.ID = rank.getID() + suit.getID();
    }

    public GameInfo.Suit getSuit() {
        return this.suit;
    }

    public GameInfo.Rank getRank() {
        return this.rank;
    }

    public String getID(){
        return this.ID;
    }

    public int getOwner(){ return this.owner; }

    void setOwner(int owner){ this.owner = owner; }

    void setSuit(GameInfo.Suit suit){
        this.suit = suit;
    }

    void setCard(Card card){
        this.suit = card.suit;
        this.rank = card.rank;
    }


}

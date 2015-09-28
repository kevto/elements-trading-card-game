package elementstcg;

/**
 * Created by Maarten on 28-9-2015.
 */
public class Player {

    private int hp;
    private String name;
    private Hand hand;
    private Deck deck;

    public void Player(int hp, String name){
        this.hp = hp;
        this.name = name;
    }

    public Card drawCard(){
        //TODO: implement draw card
        return null;
    }

    public void modifyHp(int hp){
        //TODO: implement modifyHp
    }

    public int getHp(){
        return hp;
    }

    public Hand getHand(){
        return hand;
    }

    public String getName(){
        return name;
    }
}

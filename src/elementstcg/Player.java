package elementstcg;

/**
 * Created by Maarten on 28-9-2015.
 */
public class Player {

    private int hp;
    private String name;
    private Hand hand;
    private Deck deck;

    /**
     * Initilizes the Player class with the given hp and name
     * @param hp the amount of HP the player object starts with
     * @param name the name of the player object
     */
    public Player(int hp, String name){
        this.hp = hp;
        this.name = name;
    }

    /**
     * Draw a card from the deck and return the drawn Card object
     * @return the drawn Card object from the deck
     */
    public Card drawCard(){
        //TODO: implement draw card
        return null;
    }

    /**
     * Increase or decrease the hp of the player object
     * Value can be less than zero
     * @param hp the amount by wich the player hp should be changed
     */
    public void modifyHp(int hp){
        //TODO: implement modifyHp
        hp -= hp;
    }

    /**
     * Get the current amount of hp of the player object
     * @return current hp of the player object
     */
    public int getHp(){
        return hp;
    }

    /**
     * Get the Hand object of the player object
     * @return current Hand object
     */
    public Hand getHand(){
        return hand;
    }

    /**
     * Get the name of the player object
     * @return current name field
     */
    public String getName(){
        return name;
    }
}

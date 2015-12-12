package com.elementstcg.client;

public class Player {

    private int hp;
    private String name;
    private Hand hand;
    private Deck deck;

    /**
     * Initilizes the Player class with the given hp and name
     * @author Maarten Verboogen
     * @param hp the amount of HP the player object starts with
     * @param name the name of the player object
     */
    public Player(int hp, String name){
        //TODO: Implement deck a proper way or remove all together
        //Deck is empty. Do we still need a deck
        //Or do we need the refactor player to exclude a deck from the client side
        this(hp, name, new Deck());
    }

    /**
    * Initializes the Player class with the given hp, name and deck
    * @author Maarten Verboogen
    * @param hp the amount of HP the player object starts with
    * @param name the name of the player object
    * @param deck the deck that the player will be playing with
    */
    public Player(int hp, String name, Deck deck){
        this.hp = hp;
        this.name = name;

        if(deck == null) {
            this.deck = new Deck();
        } else {
            this.deck = deck;
        }

        hand = new Hand();
    }

    /**
     * Setting the deck of the player if the deck is null
     * @param deck that the player will be playing with.
     */
    public void setDeck(Deck deck) {
        //TODO Check if failing even if null.
        //if(deck == null)
        if(this.deck == null)
            this.deck = deck;
    }

    /**
     * Draw a card from the deck and return the drawn Card object
     * @author Maarten Verboogen
     * @return the drawn Card object from the deck
     */
    public Card drawCard(){
        Card card = deck.getRandomCard();

        if(card != null) {
            hand.addCard(card);
        }

        return card;
    }

    /**
     * Increase or decrease the hp of the player object
     * Value can be less than zero
     * @author Maarten Verboogen
     * @param hp the amount by wich the player hp should be changed
     */
    public void modifyHp(int hp){
        this.hp -= hp;
        if(this.hp <= 0)
            this.hp = 0;
    }

    /**
     * Get the current amount of hp of the player object
     * @author Maarten Verboogen
     * @return current hp of the player object
     */
    public int getHp(){
        return hp;
    }

    /**
     * Get the Hand object of the player object
     * @author Maarten Verboogen
     * @return current Hand object
     */
    public Hand getHand(){
        return hand;
    }

    /**
     * Get the name of the player object
     * @author Maarten Verboogen
     * @return current name field
     */
    public String getName(){
        return name;
    }

    /**

     * Retrieves the amount of cards remaining the player's deck.
     * @return Amount of the cards remaining the player's deck in int.
    */
    public int getAmountCardsInDeck() {
        return deck.getAmountCards();
    }

    /**
     * Get the current deck.
     * @return The current deck object.
     */
    public Deck getDeck() { return deck; }
}

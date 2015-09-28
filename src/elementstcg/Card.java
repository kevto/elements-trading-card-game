package elementstcg;

/**
 * Created by Mick on 28-9-2015.
 */
public class Card {

    private int attack;
    private int hp;
    private String name;
    private boolean attacked;
    private int capacityPoints;

    private Element element;

    /**
     * Constructor of Card, creates a new instance of Card.
     * @author Mick Wonnink
     * @param element
     * @param attack
     * @param hp
     * @param name
     * @param cPoints
     */
    public Card(Element element, int attack, int hp, String name, int cPoints){
        this.attack = attack;
        this.hp = hp;
        this.name = name;
        this.element = element;
        this.capacityPoints = cPoints;
    }

    /**
     * Returns the hp field.
     * @return
     */
    public int getHP(){
        return hp;
    }

    /**
     * Returns the name field.
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the attacked field.
     * @return
     */
    public boolean getAttacked(){
        return attacked;
    }

    /**
     * Sets the attacked field.
     * @param attacked
     */
    public void setAttacked(boolean attacked){
        this.attacked = attacked;
    }

    /**
     * Return the element Enumeration field.
     * @return
     */
    public Element getElement(){
        return this.element;
    }

    /**
     * Return the capacityPoints field.
     * @return
     */
    public int getCapacityPoints(){
        return this.capacityPoints;
    }

    /**
     * Add param value, positive or negative, to the hp field.
     * @param change
     * @return
     */
    public int modifyHP(int change){
        hp += change;
        return hp;
    }

    /**
     * Returns the attack field.
     * @return
     */
    public int getAttack(){
        return attack;
    }




}

package com.elementstcg.shared.trait;

public interface ICard {

    int getHP();

    String getName();

    boolean getAttacked();

    void setAttacked(boolean attacked);

    Element getElement();

    int getCapacityPoints();

    int modifyHP(int change);

    int getAttack();

}

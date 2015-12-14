package com.elementstcg.shared.trait;

import java.io.Serializable;

public interface ICard extends Serializable {

    int getHP();

    String getName();

    boolean getAttacked();

    void setAttacked(boolean attacked);

    Element getElement();

    int getCapacityPoints();

    int modifyHP(int change);

    int getAttack();

}

package elementstcg.util;

import elementstcg.Card;
import elementstcg.Element;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class contains multiple default decks that can be used.
 *
 * @author Kevin Berendsen
 * @since 2015-10-26
 */
public class DefaultDeck {
    public static ArrayList<Card> getDeckOne() {
        ArrayList<Card> cards = new ArrayList<>();

        cards.add(new Card(Element.Fire, 3, 10, "Fire 1", 1));
        cards.add(new Card(Element.Fire, 4, 10, "Fire 2", 1));
        cards.add(new Card(Element.Fire, 5, 9, "Fire 3",  2));
        cards.add(new Card(Element.Fire, 6, 8, "Fire 4",  2));
        cards.add(new Card(Element.Fire, 8, 8, "Fire 5",  2));
        cards.add(new Card(Element.Fire, 9, 9, "Fire 6",  3));
        cards.add(new Card(Element.Fire, 11, 8, "Fire 7", 3));
        cards.add(new Card(Element.Fire, 13, 8, "Fire 8", 3));
        cards.add(new Card(Element.Air, 3, 10, "Air 1", 1));
        cards.add(new Card(Element.Air, 4, 10, "Air 2", 1));
        cards.add(new Card(Element.Air, 5, 9,  "Air 3", 2));
        cards.add(new Card(Element.Air, 6, 8,  "Air 4", 2));
        cards.add(new Card(Element.Air, 8, 8,  "Air 5", 2));
        cards.add(new Card(Element.Air, 9, 9,  "Air 6", 3));
        cards.add(new Card(Element.Air, 11, 8, "Air 7", 3));
        cards.add(new Card(Element.Air, 13, 8, "Air 8", 3));
        cards.add(new Card(Element.Earth, 3, 10, "Earth 1", 1));
        cards.add(new Card(Element.Earth, 4, 10, "Earth 2", 1));
        cards.add(new Card(Element.Earth, 5, 9,  "Earth 3", 2));
        cards.add(new Card(Element.Earth, 6, 8,  "Earth 4", 2));
        cards.add(new Card(Element.Earth, 8, 8,  "Earth 5", 2));
        cards.add(new Card(Element.Earth, 9, 9,  "Earth 6", 3));
        cards.add(new Card(Element.Earth, 11, 8, "Earth 7", 3));
        cards.add(new Card(Element.Earth, 13, 8, "Earth 8", 3));
        cards.add(new Card(Element.Thunder, 3, 10, "Thunder 1", 1));
        cards.add(new Card(Element.Thunder, 4, 10, "Thunder 2", 1));
        cards.add(new Card(Element.Thunder, 5, 9,  "Thunder 3", 2));
        cards.add(new Card(Element.Thunder, 6, 8,  "Thunder 4", 2));
        cards.add(new Card(Element.Thunder, 8, 8,  "Thunder 5", 2));
        cards.add(new Card(Element.Thunder, 9, 9,  "Thunder 6", 3));
        cards.add(new Card(Element.Thunder, 11, 8, "Thunder 7", 3));
        cards.add(new Card(Element.Thunder, 13, 8, "Thunder 8", 3));
        cards.add(new Card(Element.Water, 3, 10, "Water 1", 1));
        cards.add(new Card(Element.Water, 4, 10, "Water 2", 1));
        cards.add(new Card(Element.Water, 5, 9,  "Water 3", 2));
        cards.add(new Card(Element.Water, 6, 8,  "Water 4", 2));
        cards.add(new Card(Element.Water, 8, 8,  "Water 5", 2));
        cards.add(new Card(Element.Water, 9, 9,  "Water 6", 3));
        cards.add(new Card(Element.Water, 11, 8, "Water 7", 3));
        cards.add(new Card(Element.Water, 13, 8, "Water 8", 3));

        Collections.shuffle(cards);

        return cards;
    }
}

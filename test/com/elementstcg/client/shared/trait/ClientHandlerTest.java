package com.elementstcg.client.shared.trait;

import com.elementstcg.client.shared.trait.dummy.ClientHandlerMock;
import com.elementstcg.client.shared.trait.dummy.ServerHandlerMock;
import com.elementstcg.shared.trait.Card;
import com.elementstcg.shared.trait.Element;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

/**
 * Created by Maarten on 16-1-2016.
 */
public class ClientHandlerTest extends TestCase{

    private ClientHandlerMock player;
    private ClientHandlerMock enemy;
    private ServerHandlerMock handler;
    private Card dummyCard = new Card(Element.Air, 2, 2, "Dummy", 2);

    @Before
    public void setUp() {
        try {
            player = new ClientHandlerMock().getInstance();
            enemy = new ClientHandlerMock().getInstance();
            handler = new ServerHandlerMock();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSetupMatch() throws RemoteException {
        assertTrue("Something went wrong while setting up a match", player.setupMatch("Enemy", true));
        assertEquals("Name was not properly set", player.getBoardController().getEnemyName(), "Enemy");
        assertNotNull("There was no boardController made", player.getBoardController());
    }

    @Test
    public void testUpdatePlayerHP() throws RemoteException {
        player.updatePlayerHP(20);
        assertEquals("New hp was not set", 20, player.getBoardController().getBoard().getPlayer().getHp());
        player.updatePlayerHP(35);
        assertEquals("New hp was not set", 35, player.getBoardController().getBoard().getPlayer().getHp());
    }

    @Test
    public void testUpdateDeckCount() throws RemoteException {
        player.updateDeckCount(20);
        assertEquals("Deck amount was not set", "20", player.getBoardController().labelPlayerDeckSize);
        player.updateDeckCount(32);
        assertEquals("Deck amount was not set", "32", player.getBoardController().labelPlayerDeckSize);
    }

    @Test
    public void testAddCardToHand() throws RemoteException {
        player.addCardToHand(dummyCard);
        boolean found = false;
        for (Card handCard : player.getBoardController().getBoard().getPlayer().getHand().getCards()) {
            if(dummyCard == handCard) {
                found = true;
            }
        }
        assertTrue("Card was not added to the hand", found);
    }

    @Test
    public void testPlaceCard() throws RemoteException {
        player.placeCard(dummyCard, 4);

        assertEquals("The card was not placed on the field", dummyCard, player.getBoardController().getBoard().getPlayerField().get(4));
    }

    @Test
    public void testRemoveCard() throws RemoteException {
        Card rCard = new Card(Element.Fire, 2, 2, "RemoveCard", 2);

        player.placeCard(rCard, 12);
        player.removeCard(12);

        assertNull("Card was found on the field while it was removed", player.getBoardController().getBoard().getPlayerField().get(12));
    }

    @Test
    public void testSetCardHp() throws RemoteException {
        Card hpCard = new Card(Element.Water, 2, 2, "hpCard", 2);

        player.placeCard(hpCard, 11);
        player.setCardHp(11, 11);

        assertEquals("", 11, hpCard.getHP());
    }

    @Test
    public void testRemoveCardFromHand() throws RemoteException {
        player.addCardToHand(dummyCard);

        int cardAmount = player.getBoardController().getBoard().getPlayer().getHand().getAmountCards();
        player.removeCardFromHand(0);

        assertEquals("Card was not removed from hand", cardAmount - 1, player.getBoardController().getBoard().getPlayer().getHand().getAmountCards());
    }

    @Test
    public void testGetSessionKey () throws RemoteException {
        player.setSessionKey("TEST");
        assertEquals("Wrong session key returned", "TEST", player.getSessionKey());
    }

    @Test
    public void testSetSessionKey() throws RemoteException {
        player.setSessionKey("TEST");
        assertEquals("Wrong session key returned", "TEST", player.getSessionKey());
    }

    @Test
    public void testEnemyUpdatePlayerHp() throws RemoteException {
        player.enemyUpdatePlayerHP(20);
        assertEquals("New hp was not set", 20, player.getBoardController().getBoard().getEnemy().getHp());
        player.enemyUpdatePlayerHP(35);
        assertEquals("New hp was not set", 35, player.getBoardController().getBoard().getEnemy().getHp());
    }

    @Test
    public void testEnemyUpdateDeckCount() throws RemoteException {
        player.enemyUpdateDeckCount(20);
        assertEquals("Deck amount was not set", "20", player.getBoardController().labelEnemyDeckSize);
        player.enemyUpdateDeckCount(32);
        assertEquals("Deck amount was not set", "32", player.getBoardController().labelEnemyDeckSize);
    }

    @Test
    public void testEnemyPlaceCard() throws RemoteException {
        player.enemyPlaceCard(dummyCard, 4);

        assertEquals("The card was not placed on the field", dummyCard, player.getBoardController().getBoard().getEnemyField().get(4));
    }

    @Test
    public void testEnemyRemoveCard() throws RemoteException {
        Card rCard = new Card(Element.Fire, 2, 2, "RemoveCard", 2);

        player.enemyPlaceCard(rCard, 12);
        player.enemyRemoveCard(12);

        assertNull("Card was found on the field while it was removed", player.getBoardController().getBoard().getEnemyField().get(12));
    }

    @Test
    public void testEnemySetCardHp() throws RemoteException {
        Card hpCard = new Card(Element.Water, 2, 2, "hpCard", 2);

        player.enemyPlaceCard(hpCard, 11);
        player.enemySetCardHp(11, 11);

        assertEquals("", 11, hpCard.getHP());
    }

    @Test
    public void testNextTurn() throws RemoteException {
        player.nextTurn(true);
        assertTrue("Turn was not set properly", player.getBoardController().getBoard().getTurn());
        player.nextTurn(false);
        assertFalse("Turn was not set properly", player.getBoardController().getBoard().getTurn());
    }

    @Test
    public void testReceiveMessage() throws RemoteException {
        player.recieveMessage("You'll regret this day, friend");
        assertEquals("", 1, player.getBoardController().chatMessages.size());
        player.recieveMessage("I'm not your friend, buddy");
        assertEquals("", 2, player.getBoardController().chatMessages.size());
        player.recieveMessage("I'm not your buddy, guy");
        assertEquals("", 3, player.getBoardController().chatMessages.size());
        player.recieveMessage("He's not your guy, friend");
        assertEquals("", 4, player.getBoardController().chatMessages.size());
        player.recieveMessage("I'm not your friend, buddy");
        assertEquals("", 5, player.getBoardController().chatMessages.size());
        player.recieveMessage("Whe're not your buddy, guy");
        assertEquals("", 6, player.getBoardController().chatMessages.size());
        player.recieveMessage("Iiiiiiii'm noooooot yourrrrrrr guuuuuuyy, frieeeeend");
        assertEquals("", 7, player.getBoardController().chatMessages.size());

    }

    @Test
    public void testPing() {
        assertTrue("Wut this didn't work", player.ping());
    }
}

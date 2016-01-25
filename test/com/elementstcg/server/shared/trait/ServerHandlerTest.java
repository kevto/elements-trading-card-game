package com.elementstcg.server.shared.trait;

import com.elementstcg.server.game.Account;
import com.elementstcg.server.game.Board;
import com.elementstcg.server.game.Player;
import com.elementstcg.server.game.util.CustomException.EmptyFieldException;
import com.elementstcg.server.game.util.CustomException.ExceedCapacityException;
import com.elementstcg.server.game.util.CustomException.OccupiedFieldException;
import com.elementstcg.server.handlers.Response;
import com.elementstcg.server.handlers.ServerHandler;
import com.elementstcg.server.handlers.Session;
import com.elementstcg.shared.trait.*;
import junit.framework.TestCase;
import org.junit.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Component test of IServerHandler.
 *
 * @author Kevin Berendsen
 * @since 2016-01-13
 */
public class ServerHandlerTest extends TestCase {

    IServerHandler handler;
    IClientHandler client;
    IClientHandler secondClient;

    String keyOne;
    String keyTwo;

    @Before
    public void setUp() {
        try {
            handler = new ServerHandlerMock();
            client = new ClientHandlerMock();
            secondClient = new ClientHandlerMock();
        } catch (Exception ex) {}
    }

    @Test
    public void testLogin() throws RemoteException {
        IResponse response = handler.login(client, "Kevin", "Kevin");
        assertTrue("Login should be successful.", response.wasSuccessful());

        response = handler.login(client, "fail", "fail");
        assertFalse("fail account doesn't exist.", response.wasSuccessful());

        response = handler.login(client, "Kevin", "Kevin");
        assertFalse("Account is already logged in. This is not possible to login twice with the same account",
                response.wasSuccessful());

        response = handler.login(client, null, "");
        assertFalse("Can't login with an empty username", response.wasSuccessful());
    }

    @Ignore
    @Test
    public void testRegister() throws RemoteException {
        IResponse response = handler.register("KevinKevin", "dddKevinKevin", "email@hmail.com");
        assertTrue("Should be able to register that account.", response.wasSuccessful());

        response = handler.register("KevinKevin", "dddKevinKevin", "email@hmail.com");
        assertFalse("Account is already registered.", response.wasSuccessful());

        response = handler.register("KevinKevindd", "ddddJIEIJEIJI", "benen@dndindi");
        assertFalse("Invalid email used.", response.wasSuccessful());

        response = handler.register("kev", "ddddddddd", "berendsen.d@gmail.com");
        assertFalse("Doesn't length requirements username", response.wasSuccessful());

        response = handler.register("dddddddddd", "kev", "berendsen.d@gmail.com");
        assertFalse("Doesn't length requirements password", response.wasSuccessful());

        response = handler.register("dddddk?ev", "ddddddddd", "berendsen.d@gmail.com");
        assertFalse("Username contains non ASCII characters", response.wasSuccessful());

        response = handler.register("dddddddddd", "????????", "berendsen.d@gmail.com");
        assertFalse("Password contains non ASCII characters", response.wasSuccessful());
    }

    @Test
    public void testFindMatch() throws RemoteException {
        registerTwoSessionKeys();

        IResponse res = handler.findMatch(keyOne);
        assertTrue("Player should be searching for a match.", res.wasSuccessful());

        res = handler.findMatch(keyTwo);
        assertTrue("Second player should be able to find a match.", res.wasSuccessful());
    }

    @Test
    public void testPlaceCard() throws RemoteException {
        registerTwoSessionKeys();
        handler.findMatch(keyOne);
        handler.findMatch(keyTwo);


        IResponse res = handler.placeCard(keyTwo, 0, 0);
        assertTrue("Player one should be able to place the card.", res.wasSuccessful());

        try {
            res = handler.placeCard(keyTwo, 0, 0);
            assertFalse("Card already has been placed there. You need to call replaceCard.", res.wasSuccessful());
        } catch(Exception ex) {

        }

        try {
            res = handler.placeCard(keyTwo, -1, 1);
            assertFalse("There's no card on that index.", res.wasSuccessful());
        } catch (Exception ex) {}

        res = handler.placeCard(keyOne, 0, 0);
        assertFalse("Can't place the card because it's not his turn.", res.wasSuccessful());
    }

    @Test
    public void testNextTurn() throws RemoteException {
        registerTwoSessionKeys();
        handler.findMatch(keyOne);
        handler.findMatch(keyTwo);

        IResponse res = handler.nextTurn(keyOne);
        assertFalse("It's not the turn of this player.", res.wasSuccessful());

        res = handler.nextTurn(keyTwo);
        assertTrue("This player should be able to change turns.", res.wasSuccessful());

        res = handler.nextTurn(keyOne);
        assertTrue("This player should be able to change the turns now.", res.wasSuccessful());
    }


    @Test
    public void testReplaceCard() throws RemoteException {
        registerTwoSessionKeys();
        handler.findMatch(keyOne);
        handler.findMatch(keyTwo);

        handler.placeCard(keyTwo, 0, 0);
        handler.placeCard(keyTwo, 0, 10);
        handler.placeCard(keyTwo, 0, 2);

        IResponse res = handler.replaceCard(keyTwo, 0, 0);
        assertTrue("Should be able to replace cards.", res.wasSuccessful());

        handler.nextTurn(keyTwo);
        handler.nextTurn(keyOne);
        res = handler.attackEnemy(keyTwo, 10);
        assertTrue("Should be able to attack the enemy directly", res.wasSuccessful());
    }

    @Test
    public void testAttackCard() throws RemoteException {
        registerTwoSessionKeys();
        handler.findMatch(keyOne);
        handler.findMatch(keyTwo);

        handler.placeCard(keyTwo, 0, 0);
        handler.placeCard(keyTwo, 0, 10);
        handler.placeCard(keyTwo, 0, 2);

        handler.nextTurn(keyTwo);
        handler.placeCard(keyOne, 0, 10);
        handler.placeCard(keyOne, 0, 11);
        handler.placeCard(keyOne, 0, 12);

        IResponse res = handler.attackCard(keyOne, 10, 10);
        assertTrue("Should be able to attack the enemy card.", res.wasSuccessful());
        res = handler.attackCard(keyOne, 11, 2);
        assertTrue("Should be able to attack the enemy card.", res.wasSuccessful());

        res = handler.attackCard(keyOne, 10, 0);
        assertFalse("Can't attack twice per round.", res.wasSuccessful());
    }

    @Test
    public void testAttackEnemy() throws RemoteException {
        registerTwoSessionKeys();
        handler.findMatch(keyOne);
        handler.findMatch(keyTwo);

        handler.placeCard(keyTwo, 0, 0);
        handler.placeCard(keyTwo, 0, 10);
        handler.placeCard(keyTwo, 0, 2);

        handler.nextTurn(keyTwo);
        handler.nextTurn(keyOne);
        IResponse res = handler.attackEnemy(keyTwo, 10);
        assertTrue("Should be able to attack the enemy.", res.wasSuccessful());

        handler.nextTurn(keyTwo);
        handler.placeCard(keyOne, 0, 10);
        handler.placeCard(keyOne, 0, 11);
        handler.placeCard(keyOne, 0, 12);

        res = handler.attackEnemy(keyOne, 10);
        assertFalse("Can't attack if there are defence cards on the table.", res.wasSuccessful());
    }

    @Test
    public void testSendMessage() throws RemoteException {
        registerTwoSessionKeys();

        IResponse res;

        try {
            res = handler.sendMessage(keyOne, "Hello!");
            assertFalse("Player is not in a match and can't send messages then.", res.wasSuccessful());
        } catch (Exception ex) {}

        handler.findMatch(keyOne);
        handler.findMatch(keyTwo);

        res = handler.sendMessage(keyTwo, "Hello !!!");
        assertTrue("They are in a match, they should be able to chat.", res.wasSuccessful());

        res = handler.sendMessage(keyOne, "Hello back");
        assertTrue("They are in a match, they should be able to chat.", res.wasSuccessful());
    }

    void registerTwoSessionKeys() throws RemoteException {
        keyOne = handler.login(client, "Kevin", "Kevin").getMessage();
        keyTwo = handler.login(secondClient, "Bryan", "bryan").getMessage();
    }


    /**
     * Mock class implementing IClientHandler.
     */
    static class ClientHandlerMock extends UnicastRemoteObject implements IClientHandler {

        protected ClientHandlerMock() throws RemoteException {
        }

        @Override
        public boolean setupMatch(String enemyName, boolean startTurn) throws RemoteException {
            return true;
        }

        @Override
        public void updatePlayerHP(int hp) throws RemoteException {

        }

        @Override
        public void updateDeckCount(int amount) throws RemoteException {

        }

        @Override
        public void addCardToHand(ICard card) throws RemoteException {

        }

        @Override
        public void placeCard(ICard card, int point) throws RemoteException {

        }

        @Override
        public void removeCard(int pointer) throws RemoteException {

        }

        @Override
        public void setCardHp(int point, int hp) throws RemoteException {

        }

        @Override
        public void removeCardFromHand(int index) throws RemoteException {

        }

        @Override
        public String getSessionKey() throws RemoteException {
            return "sessionkey";
        }

        @Override
        public void setSessionKey(String key) throws RemoteException {

        }

        @Override
        public void enemyUpdatePlayerHP(int hp) throws RemoteException {

        }

        @Override
        public void enemyUpdateDeckCount(int count) throws RemoteException {

        }

        @Override
        public void enemyAddCardToHand() throws RemoteException {

        }

        @Override
        public void enemyPlaceCard(ICard card, int point) throws RemoteException {

        }

        @Override
        public void enemyRemoveCard(int point) throws RemoteException {

        }

        @Override
        public void enemySetCardHp(int point, int hp) throws RemoteException {

        }

        @Override
        public void enemyRemoveCardFromHand() throws RemoteException {

        }

        @Override
        public void nextTurn(Boolean isThisClientsTurn) throws RemoteException {

        }

        @Override
        public void endMatch(String message, boolean won) throws RemoteException {

        }

        @Override
        public void recieveMessage(String message) throws RemoteException {

        }

        @Override
        public boolean ping() throws RemoteException {
            return true;
        }
    }
    static class ServerHandlerMock extends UnicastRemoteObject implements IServerHandler {
        private Lock lock;
        private Condition boardsBusy;
        private Condition databaseBusy;
        private HashMap<String, Session> clients;
        private HashMap<String, Session> searchingPlayers;
        private HashMap<String, Board> games;
        private ExecutorService tPool;
        private static IResponse inst;

        protected ServerHandlerMock() throws RemoteException {
            lock = new ReentrantLock();
            boardsBusy = lock.newCondition();
            databaseBusy = lock.newCondition();
            // Available processors multiplied by two.
            tPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
            System.out.println(String.format("ServerHandler initialized with a thread pool consisting of %d threads.",
                    Runtime.getRuntime().availableProcessors() * 2));
            clients = new HashMap<>();
            games = new HashMap<>();
            searchingPlayers = new HashMap<>();
        }

        public IResponse login(IClientHandler client, String username, String password) throws RemoteException {
            if("".equals(username) || "".equals(password)) {
                return new Response(false, 999, "An empty username or password was supplied");
            }

            if(client == null) {
                return new Response(false, 999, "IClientHandler was null");
            }



            if(username.equals("fail")) {
                return new Response(false, 999, "No account was found with the provided username and password");
            } else {
                // Generating session key.
                String key = "";
                try {
                    key = MessageDigest.getInstance("MD5").digest(String.valueOf(System.currentTimeMillis()).getBytes()).toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if(key != null && !key.isEmpty()) {

                    // Checking if the account is already logged in.
                    for(Session ses : clients.values()) {
                        if(ses.getAccount().getUserName().equals(username)) {
                            return new Response(false, 1, "Account is already logged in!");
                        }
                    }

                    // Creating new session.
                    Account acc = new Account(username, password, "");
                    clients.put(key, new Session(key, client, acc));
                    return new Response(true, 1, key);
                } else {
                    return new Response(false, 2, "Couldn't generate session key.");
                }
            }
        }

        public IResponse register(String username, String password, String email) throws RemoteException {
            return new Response(true);
        }

        public IResponse placeCard(String key, int selected, int point) throws RemoteException {
            //Key is the player that places the card, int is the selected card in the hand, and point is where it gets placed
            //First, find the right board to place the card on, then place the card
            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());

            if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                    (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {

                Player player = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
                Player enemy = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerTwo() : board.getPlayerOne());

                //Place the card at the right spot
                //It first gets the right board, then gets the needed card and needed player to place the right card.
                try {

                    Card card = player.getHand().getCard(selected);

                    // Checking CAP points.
                    int cap = 0;
                    if(player == board.getPlayerOne()) {
                        for (Card cc : board.getPlayerOneField().values()) {
                            if (cc != null) {
                                cap += cc.getCapacityPoints();
                            }
                        }
                    } else {
                        for (Card cc : board.getPlayerTwoField().values()) {
                            if (cc != null) {
                                cap += cc.getCapacityPoints();
                            }
                        }
                    }
                    cap += card.getCapacityPoints();

                    if(cap > Board.MAX_CAP_POINTS) {
                        return new Response(false, "Can not place that card because it would exceed the maximum amount of CAP points.");
                    }

                    card = player.getHand().playCard(selected); // Actual plays the card on the field (removing from the hand etc...)
                    board.putCardPlayer(point, card, player);


                    String messageX;
                    messageX = player.getName() + " Places " + card.getName();
                    sendMessage(key, messageX);
                    //System.out.println(player.getName() + " placed card " + player.getHand().getCard(selected).getName());

                    caller.getClient().removeCardFromHand(selected);
                    caller.getClient().placeCard(card, point);
                    enemy.getSession().getClient().enemyPlaceCard(card, point);
                } catch (OccupiedFieldException e) {
                    e.printStackTrace();
                    return new Response(false, e.getMessage());
                } catch (ExceedCapacityException e) {
                    e.printStackTrace();
                    return new Response(false, e.getMessage());
                }

                return new Response(true);
            } else {
                return new Response(false, "It's not your turn to place a card!");
            }
        }

        public IResponse nextTurn(String key) throws RemoteException {

            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());

            if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                    (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {
                board.nextTurn();

                Player turn = (board.getTurn() ? board.getPlayerOne() : board.getPlayerTwo());
                Player notTurn = (!board.getTurn() ? board.getPlayerOne() : board.getPlayerTwo());

                // Drawing cards for the next turn player.
                if(turn.getAmountCardsInDeck() > 0) {
                    ICard drawedCard = turn.drawCard();
                    turn.getSession().getClient().addCardToHand(drawedCard);

                    // Updating deck counts.
                    turn.getSession().getClient().updateDeckCount(turn.getAmountCardsInDeck());
                    notTurn.getSession().getClient().enemyUpdateDeckCount(turn.getAmountCardsInDeck());
                    //TODO Display new card in the hand of the enemy player.
                    //notTurn.getSession().getClient().enemyAddCardToHand();
                }

                notTurn.getSession().getClient().nextTurn(false);
                turn.getSession().getClient().nextTurn(true);
            } else {
                return new Response(false, 1, "You can't force next turn if it's not your turn!");
            }

            return new Response(true);
        }

        public IResponse replaceCard(String key, int selected, int point) throws RemoteException {
            //First bring the card that's currently there back to the hand, then place the given card there.
            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());


            //Get the right card and remove it from the board.
            //check which players turn it is, and replace the correct card.

            //Check the board to see whose turn it is; if it's the caller: proceed.
            if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                    (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {

                Player player = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
                Player enemy = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerTwo() : board.getPlayerOne());

                Card oldCard = board.getPlayerOne().equals(player) ? board.getPlayerOneField().get(point) : board.getPlayerTwoField().get(point);
                Card card = player.getHand().getCard(selected);

                // Checking CAP points.
                int cap = 0;
                if(player == board.getPlayerOne()) {
                    for (Card cc : board.getPlayerOneField().values()) {
                        if (cc != null) {
                            cap += cc.getCapacityPoints();
                        }
                    }
                } else {
                    for (Card cc : board.getPlayerTwoField().values()) {
                        if (cc != null) {
                            cap += cc.getCapacityPoints();
                        }
                    }
                }
                cap -= oldCard.getCapacityPoints();
                cap += card.getCapacityPoints();

                if(cap > Board.MAX_CAP_POINTS) {
                    return new Response(false, "Can not place that card because it would exceed the maximum amount of CAP points.");
                }

                // Removing card from the field.
                if (player != board.getPlayerOne()) {
                    board.removePlayerTwoCard(point);
                } else {
                    board.removePlayerOneCard(point);
                }

                //Play the card that replaces the old one.
                try {
                    card = player.getHand().playCard(selected); // Actual plays the card on the field (removing from the hand etc...)

                    if(card.getAttacked()) {
                        return new Response(false, "Card can't be removed from the field due to it already attacked this turn.");
                    }

                    //Add the card to the hand.
                    if(oldCard != null) {
                        player.getHand().addCard(oldCard);
                    }

                    board.putCardPlayer(point, card, player);

                    //Removing cards visually
                    caller.getClient().removeCard(point);
                    enemy.getSession().getClient().enemyRemoveCard(point);

                    // Removes the card from the hand of the player.
                    caller.getClient().removeCardFromHand(selected);
                    if(oldCard != null) {
                        caller.getClient().addCardToHand(oldCard);
                    }

                    // Places the new card on the board of the player and enemy.
                    caller.getClient().placeCard(card, point);
                    enemy.getSession().getClient().enemyPlaceCard(card, point);
                    //TODO Display new card in the hand of the enemy player.
                    // notPlayer.getSession().getClient().enemyAddCardToHand();

                } catch (OccupiedFieldException e) {
                    e.printStackTrace();
                } catch (ExceedCapacityException e) {
                    e.printStackTrace();
                }
                sendMessage(key, player.getName() + " Replaced " + oldCard.getName() + "with " + card.getName());
                return new Response(true);
            } else {
                return new Response(false, "It's not your turn");
            }
        }


        public IResponse attackCard(String key, int point, int enemyPoint) throws RemoteException {
            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());
            Card selectedCard;
            Card enemyCard;
            Player attacker;
            Player defender;


            if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                    (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {

                // Defender cards are not allowed to attack. Any point below 6 is a defender card.
                if(point < 6) {
                    return new Response(false, "This is a defender card. You can't attack with this card!");
                }

                // Getting the right variables (player).
                attacker = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
                defender = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerTwo() : board.getPlayerOne());
                boolean isPlayerOne = (board.getPlayerOne().equals(attacker) ? true : false);

                selectedCard = (isPlayerOne ? board.getPlayerOneField().get(point) : board.getPlayerTwoField().get(point));
                enemyCard = (isPlayerOne ? board.getPlayerTwoField().get(enemyPoint) : board.getPlayerOneField().get(enemyPoint));

                if(selectedCard.getAttacked()) {
                    return new Response(false, 3, "The selected card already attacked this turn!");
                }

                try {
                    board.attackCard(attacker, point, enemyPoint, () -> {
                        try {
                            attacker.getSession().getClient().enemyRemoveCard(enemyPoint);
                            defender.getSession().getClient().removeCard(enemyPoint);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (EmptyFieldException e) {
                    return new Response(false, 4, "Your opponent has no card laying on that position!");
                }

                Card defenderCard = (isPlayerOne ? board.getPlayerTwoField().get(enemyPoint) : board.getPlayerOneField().get(enemyPoint));
                if(defenderCard != null) {
                    attacker.getSession().getClient().enemySetCardHp(enemyPoint, defenderCard.getHP());
                    defender.getSession().getClient().setCardHp(enemyPoint, defenderCard.getHP());
                }
            } else {
                return new Response(false, 1, "This is not your turn!");
            }
            sendMessage(key, attacker.getName() + " attacks with " + selectedCard.getName() + "on " + enemyCard.getName());
            return new Response(true, "You damaged your opponent!");
        }

        public IResponse attackEnemy(String key, int point) throws RemoteException {
            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());

            if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                    (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {

                // Check if it's the first turn.
                if(board.isFirstTurn()) {
                    return new Response(false, "You can't attack the enemy the first round!");
                }

                // Defender cards are not allowed to attack. Any point below 6 is a defender card.
                if(point < 6) {
                    return new Response(false, "This is a defender card. You can't attack with this card!");
                }

                // Getting the right variables (player).
                Player attacker = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
                boolean isPlayerOne = (board.getPlayerOne().equals(attacker) ? true : false);

                // Checking if the defend fields are empty.
                boolean hasDefenseCards = false;

                // Looping through all the cards om field.
                for(Map.Entry<Integer, Card> entry : (isPlayerOne ? board.getPlayerTwoField().entrySet() : board.getPlayerOneField().entrySet())) {
                    if(entry.getKey() >= 0 && entry.getKey() <= 5) {
                        hasDefenseCards = true;
                    }
                }

                if(hasDefenseCards) {
                    return new Response(false, 2, "Your opponent has defense cards on his field!");
                } else {
                    Card selectedCard = (isPlayerOne ? board.getPlayerOneField().get(point) : board.getPlayerTwoField().get(point));

                    if(selectedCard.getAttacked()) {
                        return new Response(false, 3, "The selected card already attacked this turn!");
                    }

                    // Updating the HP at both sides.
                    if(isPlayerOne) {
                        board.updatePlayerTwoHP(selectedCard.getAttack());
                        caller.getClient().enemyUpdatePlayerHP(board.getPlayerTwo().getHp());
                        board.getPlayerTwo().getSession().getClient().updatePlayerHP(board.getPlayerTwo().getHp());
                    } else {
                        board.updatePlayerOneHP(selectedCard.getAttack());
                        caller.getClient().enemyUpdatePlayerHP(board.getPlayerOne().getHp());
                        board.getPlayerOne().getSession().getClient().updatePlayerHP(board.getPlayerOne().getHp());
                    }

                    selectedCard.setAttacked(true);

                    // Game over
                    if(board.isGameOver()) {
                        String winMessage = "%s has won the match with %d HP left! You won 3 Gold!";
                        String loseMessage = "%s has won the match with %d HP left! You lost 1 Gold!";

                        // Sending the endMatch message.
                        if(board.getPlayerOne().getHp() < 1) {
                            board.getPlayerOne().getSession().getClient().endMatch(loseMessage, false);
                            board.getPlayerTwo().getSession().getClient().endMatch(winMessage, true);
                        } else {
                            board.getPlayerOne().getSession().getClient().endMatch(winMessage, true);
                            board.getPlayerTwo().getSession().getClient().endMatch(loseMessage, false);
                        }

                        // Removing the board session.
                        removeBoardSession(board);
                    }
                }

            } else {
                return new Response(false, 1, "This is not your turn!");
            }

            return new Response(true, "You damaged your opponent!");
        }

        public IResponse findMatch(String key) throws RemoteException {
            //TODO: make sure this is thread-safe.
            Session playerSession = clients.get(key);
            Session matchSession = null;

            searchingPlayers.put(key, playerSession);

            for(Map.Entry<String, Session> entry : searchingPlayers.entrySet()) {
                if(!entry.getValue().equals(playerSession)) {

                    //TODO Implement the elo system thingy.

                    matchSession = entry.getValue();
                    break;
                }
            }

            //TODO Schedule to check every second for a new match.

            if(matchSession != null)
                createBoardSession(playerSession, matchSession);

            return new Response(true);
        }

        /**
         * Creates  a new board session.
         * @param ses1 Session object of the searching player.
         * @param ses2 Session object of the second searching player.
         */
        private void createBoardSession(Session ses1, Session ses2) throws RemoteException {
            try {
                String key = MessageDigest.getInstance("MD5").digest(String.valueOf(System.currentTimeMillis()).getBytes()).toString();
                ses1.setBoardKey(key);
                ses2.setBoardKey(key);
                Board board = new Board(key, ses1, ses2);
                games.put(key, board);
                searchingPlayers.remove(ses1.getSessionKey());
                searchingPlayers.remove(ses2.getSessionKey());

                if(ses1.getClient().setupMatch("Test", true)) {
                    ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                    ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                    ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                    ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                    ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                }

                if(ses2.getClient().setupMatch("Test", false)) {
                    ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                    ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                    ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                    ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                    ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                }

                // Updating deck counts.
                ses1.getClient().updateDeckCount(board.getPlayerOne().getAmountCardsInDeck());
                ses2.getClient().updateDeckCount(board.getPlayerTwo().getAmountCardsInDeck());
                ses1.getClient().enemyUpdateDeckCount(board.getPlayerTwo().getAmountCardsInDeck());
                ses2.getClient().enemyUpdateDeckCount(board.getPlayerOne().getAmountCardsInDeck());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        public IResponse quitMatch(String key) throws RemoteException {
            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());

            boolean isPlayerOne = (board.getPlayerOne().getSession().equals(caller) ? true : false);
            String message = "You've won! %s chickened out!!";
            sendMessage(key, message);

            if(isPlayerOne) {
                message = String.format(message, board.getPlayerOne().getName());
                board.getPlayerTwo().getSession().getClient().endMatch(message, true);
            } else {
                message = String.format(message, board.getPlayerTwo().getName());
                board.getPlayerOne().getSession().getClient().endMatch(message, true);
            }

            removeBoardSession(board);

            return new Response(true);
        }

        public IResponse sendMessage(String key, String message) throws RemoteException {
            Session caller = clients.get(key);
            Board board = games.get(caller.getBoardKey());

            board.getPlayerOne().getSession().getClient().recieveMessage(message);
            board.getPlayerTwo().getSession().getClient().recieveMessage(message);
            return new Response(true);
        }

        private void removeBoardSession(Board board) {
            board.getPlayerOne().getSession().setBoardKey(null);
            board.getPlayerTwo().getSession().setBoardKey(null);

            games.remove(board.getSessionKey());
        }
    }
}

package com.elementstcg.server.handlers;

import com.elementstcg.server.game.Account;
import com.elementstcg.server.game.Board;
import com.elementstcg.shared.trait.Card;
import com.elementstcg.server.game.util.CustomException.ExceedCapacityException;
import com.elementstcg.server.game.util.CustomException.OccupiedFieldException;
import com.elementstcg.server.game.Player;
import com.elementstcg.server.game.util.CustomException.EmptyFieldException;
import com.elementstcg.shared.trait.ICard;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of the IServerHandler interface. Is a UnicastRemoteObject because it should be used by the clients.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public class ServerHandler extends UnicastRemoteObject implements IServerHandler {

    private Lock lock;
    private Condition boardsBusy;
    private Condition databaseBusy;
    private HashMap<String, Session> clients;
    private HashMap<String, Session> searchingPlayers;
    private HashMap<String, Board> games;
    private ExecutorService tPool;
    private static IResponse inst;

    /**
     * Public constructor for UnicastRemoteObject. Initializing the variables in this class.
     * Amount of threads in the thread pool is your CPU processors multiplied by two.
     * @throws RemoteException
     */
    public ServerHandler() throws RemoteException {
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

        // Trying to obtain an Account object based on user credentials.
        Account acc = Account.find.where()
                .eq("username", username)
                .eq("password", password)
                .findUnique();

        if(acc == null || !acc.getUserName().equals(username)) {
            return new Response(false, 999, "No account was found with the provided username and password");
        } else {
            // Generating session key.
            String key = "";
            try {
                key = MessageDigest.getInstance("MD5").digest((acc.getUserName() + String.valueOf(System.currentTimeMillis())).getBytes()).toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if(key != null && !key.isEmpty()) {

                // Checking if the account is already logged in.
                for(Session ses : clients.values()) {
                    if(ses.getAccount().getUserName().equals(acc.getUserName())) {
                        return new Response(false, 1, "Account is already logged in!");
                    }
                }

                // Creating new session.
                clients.put(key, new Session(key, client, acc));
                return new Response(true, 1, key);
            } else {
                return new Response(false, 2, "Couldn't generate session key.");
            }
        }
    }

    public IResponse register(String username, String password, String email) throws RemoteException {

        Account usernameAcc = Account.find.where()
                .eq("username", username)
                .findUnique();

        Account emailAcc = Account.find.where()
                .eq("email", email)
                .findUnique();

        if(usernameAcc != null || emailAcc != null) {
            return new Response(false, 999, "Username or Email is already in use");
        } else {
            if(Account.register(username, password, email)) {
                Account acc = new Account(username, password, email);
                acc.save();

                return new Response(true, 1, "Account has been created");
            } else {
                return new Response(false, 999, "Illegal use of characters in username/password, or invalid email");
            }
        }
    }

    public IResponse placeCard(String key, int selected, int point) throws RemoteException {
        //Key is the player that places the card, int is the selected card in the hand, and point is where it gets placed
        //First, find the right board to place the card on, then place the card
        Session caller = clients.get(key);
        Board board = games.get(caller.getBoardKey());
        Player player = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
        Player notPlayer = (!board.getTurn() ? board.getPlayerOne() : board.getPlayerTwo());
        Player enemy = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerTwo() : board.getPlayerOne());

        //Place the card at the right spot
        //It first gets the right board, then gets the needed card and needed player to place the right card.
        try {
            board.putCardPlayer(point, player.getHand().getCard(selected), player);

            //Call ClientHandler of both players to place the cards on their fields visually.
            caller.getClient().placeCard(player.getHand().getCard(selected), point);
            notPlayer.getSession().getClient().placeCard(notPlayer.getHand().getCard(selected), point);

            System.out.println(player.getName() + " placed card " + player.getHand().getCard(selected).getName());

            caller.getClient().placeCard(player.getHand().getCard(selected), point);
            enemy.getSession().getClient().enemyPlaceCard(player.getHand().getCard(selected), point);
        } catch (OccupiedFieldException e) {
            e.printStackTrace();
        } catch (ExceedCapacityException e) {
            e.printStackTrace();
        }

        return new Response(true);
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
                ICard drawnCard = turn.drawCard();
                turn.getSession().getClient().addCardToHand(drawnCard);
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
        Player player = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
        Player notPlayer = (!board.getTurn() ? board.getPlayerOne() : board.getPlayerTwo());

        //Get the right card and remove it from the board.
        //TODO: check which players turn it is, and replace the correct card.
        Card c;
        c = player == board.getPlayerOne() ? board.getPlayerOneField().get(selected) : board.getPlayerTwoField().get(selected);

        if (player != board.getPlayerOne()) {
            board.removePlayerTwoCard(point);
        } else {
            board.removePlayerOneCard(point);
        }

        //Add the card to the hand.
        player.getHand().addCard(c);

        //Play the card that replaces the old one.
        try {
            board.putCardPlayer(point, player.getHand().getCard(selected), player);
            //Call ClientHandler of both players to place the cards on their fields visually.
            caller.getClient().placeCard(player.getHand().getCard(selected), point);
            notPlayer.getSession().getClient().placeCard(notPlayer.getHand().getCard(selected), point);
            //TODO Display new card in the hand of the enemy player.
            //notPlayer.getSession().getClient().enemyAddCardToHand();
        } catch (OccupiedFieldException e) {
            e.printStackTrace();
        } catch (ExceedCapacityException e) {
            e.printStackTrace();
        }

        return new Response(true);

    }


    public IResponse attackCard(String key, int point, int enemyPoint) throws RemoteException {
        Session caller = clients.get(key);
        Board board = games.get(caller.getBoardKey());

        if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {

            // Getting the right variables (player).
            Player attacker = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerOne() : board.getPlayerTwo());
            Player defender = (board.getPlayerOne().getSession().equals(caller) ? board.getPlayerTwo() : board.getPlayerOne());
            boolean isPlayerOne = (board.getPlayerOne().equals(attacker) ? true : false);

            Card selectedCard = (isPlayerOne ? board.getPlayerOneField().get(point) : board.getPlayerTwoField().get(point));

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

        return new Response(true, "You damaged your opponent!");
    }

    public IResponse attackEnemy(String key, int point) throws RemoteException {
        Session caller = clients.get(key);
        Board board = games.get(caller.getBoardKey());

        if((board.getTurn() && board.getPlayerOne().getSession().equals(caller)) ||
                (!board.getTurn() && board.getPlayerTwo().getSession().equals(caller))) {

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

                // Game over.
                if(board.isGameOver()) {
                    String message = "%s has won the match with %d HP left!";
                    if(board.getPlayerOne().getHp() < 1) {
                        message = String.format(message, board.getPlayerTwo().getName(), board.getPlayerOne().getHp());
                    } else {
                        message = String.format(message, board.getPlayerOne().getName(), board.getPlayerOne().getHp());
                    }

                    //TODO Client needs endMatch method with String for the message.
                    board.getPlayerOne().getSession().getClient().endMatch(message);
                    board.getPlayerTwo().getSession().getClient().endMatch(message);

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
            String key = MessageDigest.getInstance("MD5").digest((ses1.getAccount().getUserName() + String.valueOf(System.currentTimeMillis()) + ses2.getAccount().getUserName()).getBytes()).toString();
            ses1.setBoardKey(key);
            ses2.setBoardKey(key);
            Board board = new Board(key, ses1, ses2);
            games.put(key, board);
            searchingPlayers.remove(ses1);
            searchingPlayers.remove(ses2);

            if(ses1.getClient().setupMatch(ses2.getAccount().getUserName())) {
                ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
                ses1.getClient().addCardToHand(board.getPlayerOne().drawCard());
            }

            if(ses2.getClient().setupMatch(ses1.getAccount().getUserName())) {
                ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
                ses2.getClient().addCardToHand(board.getPlayerTwo().drawCard());
            }

            //TODO Update enemy amount of cards in deck.
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public IResponse quitMatch(String key) throws RemoteException {
        Session caller = clients.get(key);
        Board board = games.get(caller.getBoardKey());

        boolean isPlayerOne = (board.getPlayerOne().getSession().equals(caller) ? true : false);
        String message = "You've won! %s chickened out!!";

        if(isPlayerOne) {
            message = String.format(message, board.getPlayerOne().getName());
            board.getPlayerTwo().getSession().getClient().endMatch(message);
        } else {
            message = String.format(message, board.getPlayerTwo().getName());
            board.getPlayerOne().getSession().getClient().endMatch(message);
        }

        removeBoardSession(board);

        return new Response(true);
    }

    private void removeBoardSession(Board board) {
        board.getPlayerOne().getSession().setBoardKey(null);
        board.getPlayerTwo().getSession().setBoardKey(null);

        games.remove(board.getSessionKey());
    }
}
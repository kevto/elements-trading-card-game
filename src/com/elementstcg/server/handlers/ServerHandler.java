package com.elementstcg.server.handlers;

import com.elementstcg.server.game.Account;
import com.elementstcg.server.game.Board;
import com.elementstcg.server.game.Card;
import com.elementstcg.server.game.Player;
import com.elementstcg.shared.trait.ICard;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;
import com.sun.xml.internal.ws.encoding.MtomCodec;

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

    //TODO: add a list/HashMapfor players who are searching for a match
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
                return new Response(true, 1, "Account found, client added");
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
        return null;
    }

    /**
     * Next turn of a player.
     * @param key mag niet leeg of null zijn
     * @return IResponse object.
     * @throws RemoteException
     */
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
        return null;
    }

    public IResponse attackCard(String key, int point, int enemyPoint) throws RemoteException {
        return null;
    }

    public IResponse attackEnemy(String key, int point) throws RemoteException {
        return null;
    }

    public IResponse findMatch(String key) throws RemoteException {
        //TODO: make sure this is thread-safe.
        String givenKey = key;
        Session playerSession = clients.get(key);
        int playerElo = playerSession.getAccount().getElo();
        int itElo;
        int tempScore;
        int score = 10000;
        Account match = null;
        Session matchSession = null;
        Iterator it = searchingPlayers.entrySet().iterator();

        while (it.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry)it.next();
            matchSession = (Session)pair.getValue();
            itElo = matchSession.getAccount().getElo();

            if (itElo == playerElo) //Need a check to see if player(s) are in a match already
            {
                createBoardSession(playerSession, matchSession);
                return new Response(true);
            }

            if (itElo < playerElo)
            {
                tempScore = playerElo - itElo;
                if (tempScore < score)
                {
                    match = matchSession.getAccount();
                    score = tempScore;
                }
            }

            if (itElo > playerElo)
            {
                tempScore = itElo - playerElo;
                if (tempScore < score)
                {
                    match = matchSession.getAccount();
                    score = tempScore;
                }
            }
        }

        //check if a match has been found, if not then keep searching
        if (match == null && matchSession == null)
        {
            //TODO Let thread sleep for 1000ms.
            findMatch(givenKey);
        }

        //Not sure what to return as match, will return to later.
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
            Board board = new Board(ses1, ses2);
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
        return null;
    }
}
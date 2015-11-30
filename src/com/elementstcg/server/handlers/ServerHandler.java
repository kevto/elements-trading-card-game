package com.elementstcg.server.handlers;

import com.elementstcg.server.game.Account;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
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
            //TODO: Create session
            //TODO: Give session to client
            return new Response(true, 1, "Account found, client added");
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

    public IResponse nextTurn(String key) throws RemoteException {
        return null;
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
                Response foundMatch = new Response(true);
                return foundMatch;
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
            findMatch(givenKey);
        }

        //Not sure what to return as match, will return to later.
        //TODO: connect these players so they can actually play against each other.
        searchingPlayers.remove(playerSession);
        searchingPlayers.remove(matchSession);
        
        Response foundMatch = new Response(true);
        return foundMatch;

    }

    public IResponse quitMatch(String key) throws RemoteException {
        return null;
    }
}
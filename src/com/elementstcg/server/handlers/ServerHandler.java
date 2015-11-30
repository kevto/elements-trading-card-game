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
        //TODO Implement ServerHandler.login method.
        return new Response(false, 999, "Not implemented yet!");
    }

    public IResponse register(String username, String password, String email) throws RemoteException {
        //TODO Implement ServerHandler.register method.
        return new Response(false, 999, "Not implemented yet!");
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
        //Need a way to find the ELO of the given player (key)
        Session playerSession = clients.get(key);
        int playerElo = playerSession.getAccount().getElo();
        int itElo;
        int tempScore;
        int score = 10000;
        Account match = null;
        Iterator it = searchingPlayers.entrySet().iterator();

        while (it.hasNext())
        {
            //TODO: rebuild to iterator.
            HashMap.Entry pair = (HashMap.Entry)it.next();
            Session accSession = (Session)pair.getValue();
            itElo = accSession.getAccount().getElo();

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
                    match = accSession.getAccount();
                    score = tempScore;
                }
            }

            if (itElo > playerElo)
            {
                tempScore = itElo - playerElo;
                if (tempScore < score)
                {
                    match = accSession.getAccount();
                    score = tempScore;
                }
            }
        }

        //check if a match has been found, if not then keep searching
        //TODO: remove players from the pool of players currently searching for a match.
        if (match == null)
        {
            findMatch(givenKey);
        }

        else
        {
            //Not sure what to return as match, will return to later.
            //TODO: connect these players so they can actually play against each other.
            Response foundMatch = new Response(true);
            return foundMatch;
        }
    }

    public IResponse quitMatch(String key) throws RemoteException {
        return null;
    }
}

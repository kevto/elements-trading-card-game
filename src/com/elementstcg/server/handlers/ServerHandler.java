package com.elementstcg.server.handlers;

import com.elementstcg.server.game.Account;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
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
    private List<Account> clients;
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
        clients = new ArrayList<>();
        // Available processors multiplied by two.
        tPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        System.out.println(String.format("ServerHandler initialized with a thread pool consisting of %d threads.",
                Runtime.getRuntime().availableProcessors() * 2));
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

    /**
     * Try to find a match for the given player.
     * @param key is the identifier for the person trying to find a match.
     * @return Instance of IResponse which shows if it was succesfull or an error code.
     * @throws RemoteException
     */
    public IResponse findMatch(String key) throws RemoteException {
        //TODO: remove the player from the pool of people searching for a match.
        //TODO: is there a pool of players looking for a match?

        String givenKey = key;
        //Need a way to find the ELO of the given player (key)
        int playerElo = 500 //test Elo, this will be the ELO of the key
        Account match = null;
        int tempScore;
        int score = 10000;

        for (Account x : clients)
        {
            if (x.getElo() == playerElo) //Need a check to see if player(s) are in a match already
            {
                //TODO: return this as match
            }

            if (x.getElo() < playerElo)
            {
                tempScore = playerElo - x.getElo();
                if (tempScore < score)
                {
                    match = x;
                    score = tempScore;
                }
            }

            if (x.getElo() > playerElo)
            {
                tempScore = x.getElo() - playerElo;
                if (tempScore < score)
                {
                    match = x;
                    score = tempScore;
                }
            }
        }

        //check if a match has been found, if not then keep searching
        if (match == null)
        {
            findMatch(givenKey);
        }

        else
        {
            //Not sure what to return as match, will return to later.
            IResponse theMatch = match;
            return theMatch;
        }
    }

    public IResponse quitMatch(String key) throws RemoteException {
        return null;
    }
}

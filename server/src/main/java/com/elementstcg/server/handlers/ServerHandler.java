package com.elementstcg.server.handlers;

import com.elementstcg.server.game.Account;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of the IServerHandler interface. Is a UnicastRemoteObject because it should be used by the clients.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public class ServerHandler extends UnicastRemoteObject implements IServerHandler  {

    private Lock lock;
    private Condition boardsBusy;
    private Condition databaseBusy;
    private List<Account> clients;
    private ExecutorService tPool;

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

    public IResponse login(String username, String password) throws RemoteException {
        //TODO Implement ServerHandler.login method.
        return new Response(false, 999, "Not implemented yet!");
    }

    public IResponse register(String username, String password, String email) throws RemoteException {
        //TODO Implement ServerHandler.register method.
        return new Response(false, 999, "Not implemented yet!");
    }
}

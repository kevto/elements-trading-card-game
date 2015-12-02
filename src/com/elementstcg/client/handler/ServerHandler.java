package com.elementstcg.client.handler;

import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IResponse;
import com.elementstcg.shared.trait.IServerHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by danny on 30-11-15.
 */
public class ServerHandler extends UnicastRemoteObject implements IServerHandler {

    protected ServerHandler() throws RemoteException {
        setupServerConnection();
    }

    @Override
    public IResponse login(IClientHandler client, String username, String password) throws RemoteException {
        return null;
    }

    @Override
    public IResponse register(String username, String password, String email) throws RemoteException {
        return null;
    }

    @Override
    public IResponse placeCard(String key, int selected, int point) throws RemoteException {
        return null;
    }

    @Override
    public IResponse nextTurn(String key) throws RemoteException {
        return null;
    }

    @Override
    public IResponse replaceCard(String key, int selected, int point) throws RemoteException {
        return null;
    }

    @Override
    public IResponse attackCard(String key, int point, int enemyPoint) throws RemoteException {
        return null;
    }

    @Override
    public IResponse attackEnemy(String key, int point) throws RemoteException {
        return null;
    }

    @Override
    public IResponse findMatch(String key) throws RemoteException {
        return null;
    }

    @Override
    public IResponse quitMatch(String key) throws RemoteException {
        return null;
    }
}

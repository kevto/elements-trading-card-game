package com.elementstcg.server.handlers;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by the clients to communicate with the server using RMI.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public interface IServerHandler extends Remote {
    /**
     * Logs a user in on request. Only if the user exists and is not logged in yet.
     * @param username that was filled in on request.
     * @param password that was filled in on request. Password that should belong to the user.
     * @return IResponse for the caller.
     * @throws RemoteException
     */
    IResponse login(String username, String password) throws RemoteException;

    /**
     * Registers a user when the email is not in use yet nor is the username.
     * @param username at least contain 7 characters and only ASCII characters.
     * @param password should contain at least 7 characters and only ASCII characters.
     * @param email should be valid.
     * @return IResponse for the caller.
     * @throws RemoteException
     */
    IResponse register(String username, String password, String email) throws RemoteException;

    /**
     * Finds the best match for the given player, in a list of all players searching for a match.
     * @param players are all the players looking for a match.
     * @param player is the one who just started searching for a match.
     * @throws RemoteException
     */
    List<Player> findMatch(List<Player> players, Player player) throws RemoteException;

}

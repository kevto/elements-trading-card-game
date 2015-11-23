package com.elementstcg.server.handlers;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Response interface to send to the user if anything happened.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public interface IResponse extends Remote, Serializable {

    /**
     * Gets the error message if any error occurred or was not successful.
     * @return null if there is no error. Any error message if the user's request wasn't successful.
     * @throws RemoteException
     */
    String getMessage() throws RemoteException;

    /**
     * Gets an error code if successful was false.
     * @return -1 if it has no error. Otherwise will return any error code.
     * @throws RemoteException
     */
    int getErrorCode() throws RemoteException;

    /**
     * Gets a boolean whether the request of the client was successful or not.
     * @return true if it was successful, false if it was not.
     * @throws RemoteException
     */
    boolean wasSuccessful() throws RemoteException;
}

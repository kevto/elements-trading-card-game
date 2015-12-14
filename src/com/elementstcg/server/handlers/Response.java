package com.elementstcg.server.handlers;

import com.elementstcg.shared.trait.IResponse;

import java.rmi.RemoteException;

/**
 * Description here.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public class Response implements IResponse {
    private boolean successful;
    private String message;
    private int errorCode;

    /**
     * Public constructor filling the response
     * @param success true if the request went successful else false
     * @param error code in int. can't be below -1.
     * @param message of the response. Could be anything.
     * @throws IllegalArgumentException
     */
    public Response(boolean success, int error, String message) throws IllegalArgumentException {
        this.successful = success;
        if(error < -1) {
            throw new IllegalArgumentException("Error code must be at least -1 or higher.");
        }
        this.errorCode = error;
        this.message = message;
    }

    /**
     * Public constructor filling the response
     * @param success true if the request went successful else false
     * @param message of the response. Could be anything.
     * @throws IllegalArgumentException
     */
    public Response(boolean success, String message) {
        this(success, -1, message);
    }

    /**
     * Public constructor filling the response
     * @param success true if the request went successful else false
     * @throws IllegalArgumentException
     */
    public Response(boolean success) {
        this(success, -1, null);
    }


    @Override
    public String getMessage() throws RemoteException {
        return message;
    }

    @Override
    public int getErrorCode() throws RemoteException {
        return errorCode;
    }

    @Override
    public boolean wasSuccessful() throws RemoteException {
        return successful;
    }
}

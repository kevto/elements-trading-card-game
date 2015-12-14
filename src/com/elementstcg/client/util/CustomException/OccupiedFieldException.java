package com.elementstcg.client.util.CustomException;

/**
 * Created by maart on 12-10-2015.
 */
public class OccupiedFieldException extends Exception {

    public OccupiedFieldException(String message) {
        super(message);
    }

    public OccupiedFieldException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

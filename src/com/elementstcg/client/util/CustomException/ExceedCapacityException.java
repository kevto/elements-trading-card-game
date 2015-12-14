package com.elementstcg.client.util.CustomException;

/**
 * Created by maart on 12-10-2015.
 */
public class ExceedCapacityException extends Exception {

    public ExceedCapacityException(String message) {
        super(message);
    }

    public ExceedCapacityException(String message, Throwable throwable) {
        super(message, throwable);
    }

}

package com.elementstcg.server.handlers;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.*;

/**
 * Testing the response methods.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public class ResponseTest extends TestCase {
    Response resp;

    @Before
    public void setUp() {
        resp = new Response(true, 0, "nothing");
    }

    /**
     * Testing the constructor.
     * @throws RemoteException
     */
    @Test
    public void testConstructor() throws RemoteException {
        assertNotNull("resp shouldn't be null. correct variables as parameters.", resp);
        assertEquals("Response should be successful", true, resp.wasSuccessful());
        assertEquals("Error code should be 0", 0, resp.getErrorCode());
        assertEquals("Message should be nothing", true, resp.getMessage().equals("nothing"));

        // Different constructor
        resp = new Response(true, "");
        assertNotNull("resp shouldn't be null.", resp);
        assertEquals("Response should be successful", true, resp.wasSuccessful());

        // Last constructor
        resp = new Response(false);
        assertNotNull("resp shouldn't be null.", resp);
        assertEquals("Response should not be successful", false, resp.wasSuccessful());
    }

    /**
     * Testing the get message method/
     * @throws RemoteException
     */
    @Test
    public void testGetMessage() throws RemoteException {
        assertEquals("Messages arent equal to each other.", true, resp.getMessage().equals("nothing"));

        // Testing on null.
        resp = new Response(false, null);
        assertNull("Message should not have been set.", resp.getMessage());
        resp = new Response(false);
        assertNull("Message should not have been set.", resp.getMessage());

        // Testing on empty.
        resp = new Response(false, "");
        assertEquals("Message should be empty.", true, resp.getMessage().equals(""));
        assertEquals("Message should be empty.", true, resp.getMessage().isEmpty());
    }

    /**
     * Testing the get error code method.
     * @throws RemoteException
     */
    @Test
    public void testGetErrorCode() throws RemoteException {
        assertEquals("Error code should be equal to each other.", 0, resp.getErrorCode());

        // Error code incorrect value.
        try {
            resp = new Response(false, -2, "");
            fail("Error code may not go lower than -1");
        } catch (IllegalArgumentException ex) {}

        // Error code incorrect value again.
        try {
            resp = new Response(false, -5, "");
        } catch (IllegalArgumentException ex) {}
    }

    /**
     * Testing the was successful method.
     * @throws RemoteException
     */
    @Test
    public void testWasSuccessful() throws RemoteException {
        assertTrue("Should be successful", resp.wasSuccessful());
        resp = new Response(false, -1, "");
        assertFalse("Should not be successful", resp.wasSuccessful());
    }
}

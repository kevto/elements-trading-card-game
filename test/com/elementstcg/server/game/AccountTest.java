package com.elementstcg.server.game;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Maarten on 28-9-2015.
 */
public class AccountTest extends TestCase {


    @Before
    public void setUp()
    {
        Account.register("testuser", "testpassword", "test@email.nl");
        Account.login("testuser", "testpassword");
        Account.getInstance().setIPAndPort("192.168.7.10", 2100);
    }

    /**
     * Tests the Login method.
     * @throws Exception
     */
    @Test
    @Ignore("Account login method needs to be fixed.")
    public void testLogin() throws Exception {
        assertEquals("Account was not logged in", true, Account.login("testuser", "testpassword"));
        assertEquals("An invalid username was given", false, Account.login("INVALIDUSERNAME", "password"));
        assertEquals("An invalid password was given", false, Account.login("username", "INVALIDPASSWORD"));
        assertEquals("Logged in to an invalid account", false, Account.login("INVALIDUSERNAME", "INVALIDPASSWORD"));
    }

    /**
     * Tests the Register method.
     * @throws Exception
     */
    @Ignore("Account register method needs to be fixed.")
    @Test
    public void testRegister() throws Exception {
        assertTrue("An account could not be created", Account.register("username", "password", "email@test.nl"));
        assertTrue("An account already existed with the same data",Account.register("username", "password", "email@test.nl"));
        assertFalse("An account was created with an invalid email", Account.register("username", "password", "NOPE"));


        //ILLEGAL CHARACTERS USERNAME
        assertFalse("Account created with illegal character in username", Account.register(";test", "password", "email@test.nl"));
        assertFalse("Account created with illegal character in username", Account.register("@test", "password", "email@test.nl"));
        assertFalse("Account created with illegal character in username", Account.register("/test", "password", "email@test.nl"));
        assertFalse("Account created with illegal character in username", Account.register(".test", "password", "email@test.nl"));

        // ILLEGAL CHARACTERS PASSWORD
        assertEquals("An illegal character was allowed in a password.", false, Account.register("username", ";test", "email@test.nl"));
        assertEquals("An illegal character was allowed in a password.", false, Account.register("username", "\"test", "email@test.nl"));
        assertEquals("An illegal character was allowed in a password.", false, Account.register("username", "'test", "email@test.nl"));
        try
        {
            Account.register("username", ";password;", "bad@email.com");
            fail("Registering was allowed with an illegal character in the password.");
        }
        catch (IllegalArgumentException ex) {}
        //BAD EMAIL ADDRESS(ES)
        assertFalse("A bad email got through!", Account.register("username", "password", "bademail"));
    }

    /**
     * Tests to make sure you can't use an empty Constructor.
     */
    @Test
    public void testEmptyConstructor()
    {
        assertEquals("Account was created with empty password.", false, Account.register("asdf", "", "info@example.nl"));
        assertEquals("Account was created with empty username", false, Account.register("", "ddd", "info@example.nl"));
    }

    /**
     * Tests the getInstance method.
     */
    @Test
    public void testGetInstance() {
        assertEquals("Wrong acc returned", "test@email.nl", Account.getInstance().getEmail());
    }

    /**
     * Tests the getEmail method to see if it returns the right email.
     */
    @Test
    public void testGetEmail() {
        assertEquals("Wrong email", "test@email.nl", Account.getInstance().getEmail());
        assertNotNull("Null was returned on email", Account.getInstance().getEmail());
    }

    /**
     * Tests if the right port is returned from getPort.
     */
    @Test
    public void testGetPort() {
        assertEquals("Port wasn't right", 2100, Account.getInstance().getPort());
        assertNotNull("Port was null", Account.getInstance().getPort());
    }

    /**
     * Test to see if the right IP is returned with getIp.
     */
    @Test
    public void testGetIp() {
        assertNotNull("IP was null", Account.getInstance().getIp());
        assertEquals("IP was not correct", "192.168.7.10", Account.getInstance().getIp());
    }

    /**
     * Test to see if getUserName functions properly.
     */
    @Test
    public void testGetUserName() {
        assertNotNull("Username was null", Account.getInstance().getUserName());
        assertEquals("Username was not correct", "testuser", Account.getInstance().getUserName());
    }

    /**
     * Test to see if setIpAndPort works correctly.
     */
    @Test
    public void testSetIPAndPort() {
        //Happy flow

        Account.getInstance().setEmail("test@email.nl");

        Account.getInstance().setIPAndPort("192.168.1.2", 2200);
        assertEquals("The new IP was not added", "192.168.1.2", Account.getInstance().getIp());
        assertEquals("The new Port was not added", 2200, Account.getInstance().getPort());

        //Try to set an negative port number
        try {
            Account.getInstance().setIPAndPort("192.168.1.1", -2);
            fail("An port has been set to -2 which should not be possible (0-65535)");
        }
        catch (IllegalArgumentException IAE) {
        }

        //Try to set an port number greater than the maximum
        try{
            Account.getInstance().setIPAndPort("192.168.1.1", 65536);
            fail("An port has been set to 65536 which should not be possible (0-65535)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an ip without dots
        try{
            Account.getInstance().setIPAndPort("19216811", 80);
            fail("An ip has been set without any seperation 19216811 (xxx.xxx.xxx)");
        }
        catch(IllegalArgumentException IAE) {
        }

        //try to set an too small IP address
        try {
            Account.getInstance().setIPAndPort("192.168.1", 80);
            fail("An ip has been set with only 3 parts 192.168.1 (xxx.xxx.xxx.xxx)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an too big IP address
        try {
            Account.getInstance().setIPAndPort("192.168.1.1.1", 80);
            fail("An ip has been set with 5 parts 192.168.1.1.1 (xxx.xxx.xxx.xxx)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an ip with an too high range
        try {
            Account.getInstance().setIPAndPort("192.300.1.1", 80);
            fail("An ip has been set with an value of a too high range 300 (0-255)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an ip with an negative range
        try {
            Account.getInstance().setIPAndPort("192.-100.1.1", 80);
            fail("An ip has been set with an negative range -100 (0-255)");
        }
        catch(IllegalArgumentException IAE){
        }
    }

    @Test
    public void testLogOut() throws Exception
    {

    }
}
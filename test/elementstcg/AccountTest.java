package elementstcg;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Maarten on 28-9-2015.
 */
public class AccountTest extends TestCase {
    private Account account = Account.getInstance();

    @Test
    public void testLogin() throws Exception {
        assertEquals("Account was not logged in", true, account.login("username", "password"));
        assertEquals("An invalid username was given", false, account.login("INVALIDUSERNAME", "password"));
        assertEquals("An invalid password was given", false, account.login("username", "INVALIDPASSWORD"));
        assertEquals("Logged in to an invalid account", false, account.login("INVALIDUSERNAME", "INVALIDPASSWORD"));
    }

    @Test
    public void testRegister() throws Exception {
        assertEquals("An account could not be created", true, account.register("username", "password", "email@test.nl"));
        assertEquals("An account already existed with the same data", true, account.register("username", "password", "email@test.nl"));
        assertEquals("An account was created with an invalid email", false, account.register("username", "password", "NOPE"));


        //ILLEGAL CHARACTERS USERNAME
        try {
            account.register(";test", "password", "email@test.nl");
            fail("Account created with illegal character in username");
        }
        catch (IllegalArgumentException ex) {}

        try {
            account.register("@test", "password", "email@test.nl");
            fail("Account created with illegal character in username");
        }
        catch (IllegalArgumentException ex) {}

        try {
            account.register("/test", "password", "email@test.nl");
            fail("Account created with illegal character in username");
        }
        catch (IllegalArgumentException ex) {}

        try {
            account.register(".test", "password", "email@test.nl");
            fail("Account created with illegal character in username");
        }
        catch (IllegalArgumentException ex) {}


        // ILLEGAL CHARACTERS PASSWORD
        try {
            account.register("username", ";test", "email@test.nl");
            fail("An illegal character was allowed in a password.");
        }
        catch (IllegalArgumentException ex) {}

        try {
            account.register("username", "\"test", "email@test.nl");
            fail("An illegal character was allowed in a password.");
        }
        catch (IllegalArgumentException ex) {}

        try {
            account.register("username", "'test", "email@test.nl");
            fail("An illegal character was allowed in a password.");
        }
        catch (IllegalArgumentException ex) {}

    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegister2()
    {
        try
        {
            assertEquals("An account was created with an empty password", false, account.register("username", "", "email@test.nl"));
        }
        catch (IllegalArgumentException ex) {}

        assertEquals("An account was created with an empty username", false, account.register("", "password", "email@test.nl"));
    }

    @Test
    public void testGetInstance() throws Exception {
        assertEquals("Wrong object was returned", account, account.getInstance());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals("The email was not correct", "test@email.nl", account.getEmail());
    }

    @Test
    public void testGetPort() throws Exception {
        assertEquals("The port was not correct", 2048, account.getPort());
    }

    @Test
    public void testGetIp() throws Exception {
        assertEquals("The IP was not correct", "192.168.1.1", account.getIp());
    }

    @Test
    public void testGetUserName() throws Exception {
        assertEquals("The username was not correct", "username", account.getUserName());
    }

    @Test
    public void testSetIPAndPort() throws Exception {
        //Happy flow
        account.setIPAndPort("192.168.1.2", 2100);
        assertEquals("The new IP was not added", "192.168.1.2", account.getIp());
        assertEquals("The new Port was not added", 2100, account.getPort());

        //Try to set an negative port number
        try {
            account.setIPAndPort("192.168.1.1", -2);
            fail("An port has been set to -2 which should not be possible (0-65535)");
        }
        catch (IllegalArgumentException IAE) {
        }

        //Try to set an port number greater than the maximum
        try{
            account.setIPAndPort("192.168.1.1", 65536);
            fail("An port has been set to 65536 which should not be possible (0-65535)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an ip without dots
        try{
            account.setIPAndPort("19216811", 80);
            fail("An ip has been set without any seperation 19216811 (xxx.xxx.xxx)");
        }
        catch(IllegalArgumentException IAE) {
        }

        //try to set an too small IP address
        try {
            account.setIPAndPort("192.168.1", 80);
            fail("An ip has been set with only 3 parts 192.168.1 (xxx.xxx.xxx.xxx)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an too big IP address
        try {
            account.setIPAndPort("192.168.1.1.1", 80);
            fail("An ip has been set with 5 parts 192.168.1.1.1 (xxx.xxx.xxx.xxx)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an ip with an too high range
        try {
            account.setIPAndPort("192.300.1.1", 80);
            fail("An ip has been set with an value of a too high range 300 (0-255)");
        }
        catch(IllegalArgumentException IAE){
        }

        //try to set an ip with an negative range
        try {
            account.setIPAndPort("192.-100.1.1", 80);
            fail("An ip has been set with an negative range -100 (0-255)");
        }
        catch(IllegalArgumentException IAE){
        }
    }

    @Test
    public void testLogout() throws Exception {
        assertNull("Object was not null", account);
        account = Account.getInstance();
    }

}
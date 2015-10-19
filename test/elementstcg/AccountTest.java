package elementstcg;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Maarten on 28-9-2015.
 */
public class AccountTest extends TestCase {
    private Account account;

    @Before
    public void init()
    {
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");
    }

    @Test
    public void testLogin() throws Exception {
        assertEquals("Account was not logged in", true, Account.login("username", "password"));
        assertEquals("An invalid username was given", false, Account.login("INVALIDUSERNAME", "password"));
        assertEquals("An invalid password was given", false, Account.login("username", "INVALIDPASSWORD"));
        assertEquals("Logged in to an invalid account", false, Account.login("INVALIDUSERNAME", "INVALIDPASSWORD"));
    }

    @Test
    public void testRegister() throws Exception {
        assertEquals("An account could not be created", true, Account.register("username", "password", "email@test.nl"));
        assertEquals("An account already existed with the same data", true, Account.register("username", "password", "email@test.nl"));
        assertEquals("An account was created with an invalid email", false, Account.register("username", "password", "NOPE"));


        //ILLEGAL CHARACTERS USERNAME
        assertEquals("Account created with illegal character in username", false, Account.register(";test", "password", "email@test.nl"));
        assertEquals("Account created with illegal character in username", false, Account.register("@test", "password", "email@test.nl"));
        assertEquals("Account created with illegal character in username", false, Account.register("/test", "password", "email@test.nl"));
        assertEquals("Account created with illegal character in username", false, Account.register(".test", "password", "email@test.nl"));

        // ILLEGAL CHARACTERS PASSWORD
        assertEquals("An illegal character was allowed in a password.", false, Account.register("username", ";test", "email@test.nl"));
        assertEquals("An illegal character was allowed in a password.", false, Account.register("username", "\"test", "email@test.nl"));
        assertEquals("An illegal character was allowed in a password.", false, Account.register("username", "'test", "email@test.nl"));
    }

    @Test
    public void testEmptyConstructor()
    {
        assertEquals("Account was created with empty password.", false, Account.register("asdf", "", "info@example.nl"));
        assertEquals("Account was created with empty username", false, Account.register("", "ddd", "info@example.nl"));
    }

    @Test
    public void testGetInstance() {
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");
        assertEquals("Wrong object was returned", Account.getInstance(), account);
    }

    @Test
    public void testGetEmail() {
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");
        assertEquals("The email was not correct", "test@email.nl", account.getEmail());
    }

    @Test
    public void testGetPort() {
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");
        assertEquals("The port was not correct", 2048, account.getPort());
    }

    @Test
    public void testGetIp() {
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");
        assertEquals("The IP was not correct", "127.0.0.1", account.getIp());
    }

    @Test
    public void testGetUserName() {
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");
        assertEquals("The username was not correct", "username", account.getUserName());
    }

    @Test
    public void testSetIPAndPort() {
        //Happy flow
        Account.login("username", "password");
        account = Account.getInstance();
        account.setEmail("test@email.nl");

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
}
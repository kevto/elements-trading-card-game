package com.elementstcg.server.game;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by Maarten on 28-9-2015.
 */
public class AccountTest extends TestCase {

    Account acc;

    @Before
    public void setUp()
    {
        Account.register("testuser", "testpassword", "test@email.nl");
        acc = new Account("testuser2", "testpw2", "testemail@mail.com");

        //Account.getInstance().setIPAndPort("192.168.7.10", 2100);
    }

    /**
     * Tests the Register method.
     * @throws Exception
     */
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
     * Tests the getEmail method to see if it returns the right email.
     */
    @Test
    public void testGetEmail() {
        assertEquals("Wrong email returned.", "testemail@mail.com", acc.getEmail());
    }

    /**
     * Test to see if getUserName functions properly.
     */
    @Test
    public void testGetUserName() {
        assertEquals("Wrong username returned.", "testuser2", acc.getUserName());
    }

    @Test
    public void testSetAndGetElo(){
        int elo = 750;
        acc.setElo(elo);
        assertEquals("Wrong elo returned.", 750, acc.getElo());
    }

    @Test
    public void testGetPassword(){
        assertEquals("Wrong password returned.", "testpw2", acc.getPassword());
    }

    @Test
    public void testGetGold(){
        assertEquals("Wrong amount of gold returned.", 0, acc.getGold());
    }

    @Test
    public void testSetGold(){
        acc.setGold(3);
        assertEquals("Gold wasn't correctly added.", 3, acc.getGold());
    }

    @Test
    public void testSetEmail(){
        acc.setEmail("newemail@email.com");
        assertEquals("Wrong email returned, it wasn't changed.", "newemail@email.com", acc.getEmail());
    }

}
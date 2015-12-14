package com.elementstcg.client;

import com.elementstcg.client.Account;
import com.elementstcg.shared.trait.IClientHandler;
import com.elementstcg.shared.trait.IServerHandler;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Maarten on 28-9-2015.
 * Modified by Danny on 14-12-2015
 *
 *
 */
public class AccountTest extends TestCase {
    Account account;
    @Before
    public void setUp()
    {
        account = new Account("Maarten_schoolshooter");
        account.setElo(500);
    }

    @Test
    public void testGetName(){
        assertEquals("Should be equal to Maarten_schoolshooter", "Maarten_schoolshooter", account.getUserName());
    }

    public void testGetElo(){
        assertEquals("Elo should be 500", 500, account.getElo());
    }







}
package com.elementstcg.client;

/**
 * Created by Mick on 28-9-2015.
 * @Author Mick
 * @Author Rick
 */

import java.io.*;
import java.nio.file.Files;


public class Account implements Serializable {

    private static Account instance;
    private String username;
    private int elo;


    /**
     * Constructor of Account, creates an instance of Account.
     * ip can be empty, port can be -1.
     * username and password can't be empty.
     * @author Mick Wonnink
     * @param username
     */
    public Account(String username){
        this.username = username;
    }

    /**
     * Returns the current Account instance.
     * can return null.
     * @return
     */
    public static Account getInstance(){
        return instance;
    }

    public static Account setInstance(String username) {
        instance = new Account(username);
        return instance;
    }


    /**
     * Return the userName field.
     * can return null.
     * @return
     */
    public String getUserName(){
        return username;
    }


    /**
     * Gets the elo of the account.
     * @return an int that is the elo.
     */
    public int getElo() { return elo; }


    public void setElo(int elo){
        this.elo = elo;
    }
}

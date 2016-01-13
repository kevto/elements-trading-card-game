package com.elementstcg.server.game;

/**
 * Created by Mick on 28-9-2015.
 * @Author Mick
 * @Author Rick
 */

import com.avaje.ebean.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;

@Entity
@Table(name="player")
public class Account extends Model implements Serializable {

    @Id
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "elo")
    private int elo;

    @Column(name = "gold")
    private int gold;

    /**
     * Checks if the given username and password combination exist,
     * Returns true if they do.
     * @author Mick Wonnink
     * @param username
     * @param password
     * @return
     */
    @Deprecated
    public static boolean login(String username, String password)
    {
        //TODO Implement new login method for Account class.
        return false;
    }

    /**
     * Creates a new Account with the given username, password and email.
     * @author Mick Wonnink
     * @param username
     * @param password
     * @param email
     * @return
     */
    public static boolean register(String username, String password, String email)
    {
        //TODO Implement new register method for Account class.

        if (username == "" || password == "" || email == "")
        {
            return false;
        }

        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = p.matcher(email);

        if (!m.matches()) {
            return false;
        }

        return true;
    }

    /**
     * Constructor of Account, creates an instance of Account.
     * ip can be empty, port can be -1.
     * username and password can't be empty.
     * @author Mick Wonnink
     * @param username
     * @param password
     * @param ip
     * @param port
     */
    private Account(String username, String password, String ip, int port, int elo){

        if (!username.isEmpty() && !password.isEmpty()) {
            String pattern = "[$&+,:;=?@#|'<>.-^*()%!]";
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(username);

            if (m.find())
            {
                throw new IllegalArgumentException("Illegal character found in username.");
            }

            pattern = "[\",:;#|'<>.-^*()%!]";
            p = java.util.regex.Pattern.compile(pattern);
            m = p.matcher(password);

            if (m.find())
            {
                throw new IllegalArgumentException("Illegal character found in password.");
            }

            this.username = username;
            this.password = password;
            this.elo = elo;

        }
        else{
            throw new IllegalArgumentException("username/password/email can't be empty.");
        }
    }

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    /**
     * Return the email field.
     * can return null.
     * @return
     */
    public String getEmail(){
        return email;
    }

    /**
     * Return the email field.
     * can return null.
     * @return
     */
    public void setEmail(String emailString){
        email = emailString;
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
     * Return the password field.
     * can return null.
     * @return
     */
    public String getPassword(){
        return password;
    }

    /**
     * Gets the elo of the account.
     * @return an int that is the elo.
     */
    public int getElo() { return elo; }

    /**
     * Sets the elo of the player.
     * @param elo will be added on top of the existing elo.
     */
    public void setElo(int elo) {
        this.elo = elo;
    }

    /**
     * Sets the amount of gold of the player. gold will be added on top of the player gold.
     * @param gold
     */
    public void setGold(int gold) {
        this.gold += gold;
    }


    public static Finder<Long, Account> find = new Finder<>(Account.class);

}

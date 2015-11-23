package com.elementstcg.server.game;

/**
 * Created by Mick on 28-9-2015.
 * @Author Mick
 * @Author Rick
 */

import java.io.*;


public class Account implements Serializable {

    private static Account instance;
    private String username;
    private String password;
    private String ip;
    private int port;
    private String email;

    /**
     * Checks if the given username and password combination exist,
     * Returns true if they do.
     * @author Mick Wonnink
     * @param username
     * @param password
     * @return
     */
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

        instance = new Account(username, password, "12.0.0.1", 2048);
        instance.setEmail(email);

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
    private Account(String username, String password, String ip, int port){

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
            this.ip = ip;
            this.port = port;
        }
        else{
            throw new IllegalArgumentException("username/password/email can't be empty.");
        }
    }

    /**
     * Returns the current Account instance.
     * can return null.
     * @return
     */
    public static Account getInstance(){
        return instance;
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
     * Returns the port field.
     * can return -1.
     * @return
     */
    public int getPort(){
        return port;
    }

    /**
     * Return the ip field.
     * can return null.
     * @return
     */
    public String getIp(){
        return ip;
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
     * Sets the ip and port field.
     * port must be a number between 0 and 65535.
     * ip can't be empty.
     * @param ip
     * @param port
     */
    public void setIPAndPort(String ip, int port)
    {
        String testIp = ip.replace(".", "");
        int count = ip.length() - testIp.length();

        String[] ipArray = ip.split("\\.");
        boolean tooBig = false;

        for (String x : ipArray)
        {
            int y = Integer.parseInt(x);
            if (y > 256 || y < 0 || ipArray.length < 4 || y == 255)
            {
                tooBig = true;
            }
        }

        if (!ip.isEmpty() && count == 3 && tooBig == false) {
            this.ip = ip;
        }
        if (port >= 1024 && port <= 65535) {
            this.port = port;
        }
        else {
            throw new IllegalArgumentException("port must be > 1024, ip can't be empty");
        }
    }

    /**
     * Clears the Account instance.
     */
    public void logOut()
    {
        //TODO : further implementation
        instance = null;
    }




}

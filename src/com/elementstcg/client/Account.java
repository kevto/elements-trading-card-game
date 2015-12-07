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
    private String password;
    private String ip;
    private int port;
    private String email;
    private int elo;

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
        // TODO Enable below until we're done with registering an account

        Account savedAccount = null;

        try
        {
            File filePath = new File("savedaccount.ser");
            if (filePath.exists()) {
                FileInputStream fileIn = new FileInputStream(filePath);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                savedAccount = (Account) in.readObject();
                in.close();
                fileIn.close();
            }

        }catch(IOException i)
        {
            i.printStackTrace();

        }catch(ClassNotFoundException c)
        {
            System.out.println("Account class not found");
            c.printStackTrace();

        }
        if (savedAccount != null){
            instance = savedAccount;
            if (savedAccount.getUserName().equals(username) && savedAccount.getPassword().equals(password)) {
                return true;
            }

        }
        return false;
//        if(username.isEmpty() || password.isEmpty())
//            return false;
//
//        try {
//            instance = new Account(username, password, "127.0.0.1", 2048);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }
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
        //TODO Enable below until we're done with registering an account

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

        instance = new Account(username, password, "12.0.0.1", 2048, 0);
        instance.setEmail(email);

        //Serialization
        try {
            File filepath = new File("savedaccount.ser");
            if (filepath.exists()) {
                Files.delete(filepath.toPath());
            }
            FileOutputStream fileOut =
                    new FileOutputStream(filepath);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(instance);
            out.close();
            fileOut.close();
            System.out.printf("Serialized account is saved in savedaccount.ser");
            return true;
        } catch (IOException i) {
            i.printStackTrace();
        }
        return false;
    }

//        if(username.isEmpty() || password.isEmpty() || email.isEmpty())
//            return false;
//
//        String pattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
//        java.util.regex.Matcher m = p.matcher(email);
//
//        if (!m.matches())
//            return false;
//
//        try {
//            instance = new Account(username, password, "127.0.0.1", 2048);
//            instance.setEmail(email);
//            return true;
//        } catch (Exception ex) {
//            return false;
//        }


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
            this.ip = ip;
            this.port = port;

            this.elo = elo;
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
     * Gets the elo of the account.
     * @return an int that is the elo.
     */
    public int getElo() { return elo; }

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

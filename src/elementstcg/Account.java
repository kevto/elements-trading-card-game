package elementstcg;

/**
 * Created by Mick on 28-9-2015.
 */
public class Account {

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
    public boolean login(String username, String password){
        //TODO

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
    public boolean register(String username, String password, String email){
        //TODO : implement register()

        return false;
    }

    /**
     * Constructor of Account, creates an instance of Account.
     * @author Mick Wonnink
     * @param username
     * @param password
     * @param ip
     * @param port
     */
    private Account(String username, String password, String ip, int port){

        this.username = username;
        this.password = password;
        this.ip = ip;
        this.port = port;

    }

    /**
     * Returns the current Account instance.
     * @return
     */
    public Account getInstance(){
        return instance;
    }

    /**
     * Return the email field.
     * @return
     */
    public String getEmail(){
        return email;
    }

    /**
     * Returns the port field.
     * @return
     */
    public int getPort(){
        return port;
    }

    /**
     * Return the ip fied.
     * @return
     */
    public String getIp(){
        return ip;
    }

    /**
     * Return the userName field.
     * @return
     */
    public String getUserName(){
        return username;
    }

    /**
     * Sets the ip and port field.
     * @param ip
     * @param port
     */
    public void setIPAndPort(String ip, String port){
        //TODO
    }

    /**
     * Clears the Account instance.
     */
    public void logout(){
        //TODO
        instance = null;
    }




}

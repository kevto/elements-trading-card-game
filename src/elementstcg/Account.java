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
        //TODO : implement login()

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
            this.username = username;
            this.password = password;
        }
        else{
            throw new IllegalArgumentException("username and password can't be empty.");
        }
        this.ip = ip;
        this.port = port;

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
     * Sets the ip and port field.
     * port must be a number between 0 and 65535.
     * ip can't be empty.
     * @param ip
     * @param port
     */
    public void setIPAndPort(String ip, int port){
        if (!ip.isEmpty()) {
            this.ip = ip;
        }
        if (port <= 65535 && port >= 0) {
            this.port = port;
        }
        else if (ip.isEmpty()){
            throw new IllegalArgumentException("port must be a number between 0 and 65535, ip can't be empty");
        }
    }

    /**
     * Clears the Account instance.
     */
    public void logout(){
        //TODO : further implementation
        instance = null;
    }




}

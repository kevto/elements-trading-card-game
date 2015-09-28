package elementstcg;

/**
 * Created by Mick on 28-9-2015.
 */
public class Account {

    private static Account instance;
    private String userName;
    private String password;
    private String ip;
    private int port;
    private String email;

    public boolean login(String username, String password){
        //TODO

        return false;
    }

    public boolean register(String username, String password, String email){
        //TODO

        return false;
    }


    private Account(String username, String password, String ip, int port){

        userName = username;
        this.password = password;
        this.ip = ip;
        this.port = port;

    }

    public Account getInstance(){
        return instance;
    }

    public String getEmail(){
        return email;
    }

    public int getPort(){
        return port;
    }

    public String getIp(){
        return ip;
    }

    public String getUserName(){
        return userName;
    }

    public void setIPAndPort(String ip, String port){
        //TODO
    }

    public void logout(){
        //TODO
        instance = null;
    }




}

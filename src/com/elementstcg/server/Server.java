package com.elementstcg.server;

import com.elementstcg.server.handlers.ServerHandler;
import com.elementstcg.shared.trait.IServerHandler;
import org.avaje.agentloader.AgentLoader;

import java.net.Inet4Address;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The server initiating the RMI registry service and starting the monitor.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public class Server {

    private Registry registry;
    private IServerHandler handler;

    private String serverName = "server";

    /**
     * Public constructor.
     * @throws Exception if any exception occurs, shutdown the server immediately.
     */
    public Server() {
        this(8112);
    }

    /**
     * Public constructor.
     * @param port port between a reasonable number of 1024 and 65000
     * @throws Exception if any exception occurs, shutdown the server immediately.
     */
    public Server(int port) {
        try {
            System.out.println("STARTING TO INITIALIZE THE SERVER");
            if(port < 1024 || port > 65000) {
                throw new Exception("Port number is invalid. Must be between 1024 and 65000");
            }

            registry = LocateRegistry.createRegistry(port);
            handler = new ServerHandler();
            registry.bind(serverName, handler);

            // Load the agent into the running JVM process
//            if (!AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1;packages=com.elementstcg.server.game.**")) {
//                System.err.println("avaje-ebeanorm-agent not found in classpath - not dynamically loaded");
//            }

            System.out.println("SERVER IS UP AND RUNNING\n---------------");
            System.out.println("ELEMENTS TCG SERVER");
            System.out.println("SERVER RUNNING AT: " + Inet4Address.getLocalHost().getHostAddress() + ":" + port  + "  -  Server Name: " + serverName);
            System.out.println("VERSION: 1.0 Beta");
            System.out.println("HAVE FUN!");
            System.out.println("---------------");

            // Loop so that you can exit the server by hand.
            Scanner scanner = new Scanner(System.in);
            while(!scanner.nextLine().equals("exit")) {}


            // Shutting down the server
            System.out.println("INITIATING THE TIME BOMB!");
            System.out.println("SERVER WILL DIE IN 5 SECONDS!");

            // Adds a new timer to schedule it's death.
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("SHUTTING DOWN THE SERVER NOW!!!");
                    System.exit(1);
                }
            }, 5000);

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }


    /**
     * Initiating the server.
     * @param args port between a reasonable number of 1024 and 65000 or none.
     *             default port is 8112.
     */
    public static void main(String[] args) {
        try {
            Server server;

            if(args.length > 0 && args[0].length() > 0) {
                server = new Server(Integer.getInteger(args[0]));
            } else {
                server = new Server();
            }

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
    }
}

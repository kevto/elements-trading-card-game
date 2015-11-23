package com.elementstcg.server;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;

/**
 * Database class wrapper
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public class Database {

    private static EbeanServer instance;

    /**
     * Gets the instance of the ebean server. Set one if instance is null
     * @return EbeanServer
     */
    public static EbeanServer get() {
        if(instance == null)
            instance = Ebean.getServer("db");
        return instance;
    }
}

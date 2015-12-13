package com.elementstcg.server.handlers;

import com.elementstcg.server.game.Account;
import com.elementstcg.shared.trait.IClientHandler;

/**
 * Created by rvanduijnhoven on 30/11/2015.
 * This class will be used to keep track of the sessions of players which are connected to the server.
 */
public class Session {

    private String boardKey;
    private String key;
    private IClientHandler handler;
    private Account account;

    public Session(String key, IClientHandler handler, Account account)
    {
        this.key = key;
        this.handler = handler;
        this.account = account;
    }

    public IClientHandler getClient()
    {
        return handler;
    }

    public Account getAccount()
    {
        return account;
    }

    public String getBoardKey()
    {
        return boardKey;
    }

    public void setBoardKey(String boardKey)
    {
        this.boardKey = boardKey;
    }

}

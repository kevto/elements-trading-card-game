package com.elementstcg.shared.trait;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface used by the clients to communicate with the server using RMI.
 *
 * @author Kevin Berendsen
 * @since 2015-11-16
 */
public interface IServerHandler extends Remote {
    /**
     * Logs a user in on request. Only if the user exists and is not logged in yet.
     * @param username that was filled in on request.
     * @param password that was filled in on request. Password that should belong to the user.
     * @return IResponse for the caller.
     * @throws RemoteException
     */
    IResponse login(IClientHandler client, String username, String password) throws RemoteException;

    /**
     * Registers a user when the email is not in use yet nor is the username.
     * @param username at least contain 7 characters and only ASCII characters.
     * @param password should contain at least 7 characters and only ASCII characters.
     * @param email should be valid.
     * @return IResponse for the caller.
     * @throws RemoteException
     */
    IResponse register(String username, String password, String email) throws RemoteException;

    /**
     * De server controleert of de kaart die doorgegeven wordt mag en kan worden
     * geplaatst op de positie die de client aangeeft. Als dit succesvol is worden
     * bij beide clients de placeCard methode aangeroepen.
     * @param key kan niet null zijn en mag niet leeg zijn
     * @param selected kan niet kleiner dan 0 zijn
     * @param point kan niet  kleiner dan 0 zijn
     * @return IResponse voor de caller.
     * @throws RemoteException
     */
    IResponse placeCard(String key, int selected, int point) throws RemoteException;

    /**
     * De client geeft aan dat de beurt voorbij is.
     * De server geeft nu door aan de andere client dat het nu zijn beurt is.
     * @param key mag niet leeg of null zijn
     * @return IResponse voor de caller.
     * @throws RemoteException
     */
    IResponse nextTurn(String key) throws RemoteException;

    /**
     * Geeft aan dat de client graag twee kaarten wil wisselen van
     * plaats ( een in de hand en een op het veld) en controleert of dit toegestaan is.
     * Als dit succesvol is, roept de server bij beide clients een placeCard,
     * addCardToHand, removeCardFromHand en removeCard aan.
     * @param key mag niet leeg of null zijn
     * @param selected kan niet kleiner dan 0 zijn
     * @param point kan niet kleiner dan 0 zjn
     * @return IResponse voor de caller
     * @throws RemoteException
     */
    IResponse replaceCard(String key, int selected, int point) throws RemoteException;

    /**
     * De client geeft aan dat die een kaart wil aanvallen (enemyPoint) met zijn
     * eigen kaart (point). De server controleert of dit toegestaan is.
     * Als dit succesvol is, roept de server bij beide clients een (enemy)updateCardHp aan
     * @param key mag niet leeg of null zijn
     * @param point kan niet kleiner dan 0 zijn
     * @param enemyPoint kan niet kleiner dan 0 zijn
     * @return IResponse voor de caller
     * @throws RemoteException
     */
    IResponse attackCard(String key, int point, int enemyPoint) throws RemoteException;

    /**
     * De client geeft aan dat hij de tegenstander
     * (speler) direct wil aanvaleen met de kaart die op de positie point ligt.
     * @param key mag niet leeg of null zijn
     * @param point kan niet kleiner dan 0 zijn
     * @return IResponse voor de caller
     * @throws RemoteException
     */
    IResponse attackEnemy(String key, int point) throws RemoteException;

    /**
     * De client geeft aan dat die een potje wil speler.
     * De speler wordt nu toegevoegd aan de matching que
     * @param key mag niet leeg of null zijn
     * @return IResponse voor de caller
     * @throws RemoteException
     */
    IResponse findMatch(String key) throws RemoteException;

    /**
     * De client geeft aan dat die het huidige potje afsluit.
     * De server geeft door dat de andere speler gewonnen heeft en
     * sluit de huidige match waar deze speler bij hoort af.
     * @param key mag niet leeg of null zijn
     * @return IResponse voor de caller
     * @throws RemoteException
     */
    IResponse quitMatch(String key) throws RemoteException;

    IResponse isConnected() throws RemoteException;
}

package game.controller;

import game.dao.*;
import game.entity.*;

import java.util.*;

/**
* A GiftController responds to commands by InventoryMenu and execute these commands on the entity
*/
public class GiftController {
    private Player currentPlayer;
    private InventoryDAO inventoryDAO;
    private GiftDAO giftDAO;
    private PlayerDAO playerDAO;
    private FriendDAO friendDAO;

    /**
     * Creates a Gift object with the specified player
     * @param currentPlayer the currentPlayer who is player the game
     */
    public GiftController(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.inventoryDAO = InventoryDAO.getInstance(currentPlayer);
        this.giftDAO = GiftDAO.getInstance(currentPlayer);
        this.playerDAO = PlayerDAO.getInstance();
        this.friendDAO = FriendDAO.getInstance(currentPlayer);
    }

    /**
     * Gets the currentPlayer
     * @return the currentPlayer
     */
    public Player getPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if currentPlayer has reached the daily limit of gift sending
     * @return true if equal or more than dailyLimit, false if smaller than dailyLimit
     */
    public boolean reachedDailyLimit() {
        return giftDAO.reachedDailyLimit();
    }

    /**
     * Attempts to send gift to every player in the specified array.
     * Sending a gift may cause several errors, returning various error codes as follows
     * 0 - Success
     * 1 - Daily limit of 5 gifts reached
     * 2 - Cannot send gift to yourself
     * 3 - No player with that username exists
     * 4 - Player is not your friend
     * 5 - Already sent to player today
     * 6 - Insufficient crops left in the inventory
     * Error codes with smaller number takes precedence
     * @param usernames
     * @param crop
     * @return a HashMap where the key is each username the user tried to send to, and the value is the status code
     */
    public LinkedHashMap<String, Integer> sendGifts(String[] usernames, Crop crop) {
        //using a LinkedHashMap to retain ordering
        LinkedHashMap<String, Integer> status = new LinkedHashMap<>();

        for(String username : usernames){
            username = username.trim();

            //ignore if empty token
            if (username.length() == 0) {
                continue;
            }

            //ensure that we have not reached the daily limit
            if (reachedDailyLimit()) {
                status.put(username, 1);
                continue;
            }

            //ensure that player does not send to himself
            if(currentPlayer.getUsername().equals(username)){
                status.put(username, 2);
                continue;
            }

            //ensure that player exists
            Player receiver = playerDAO.getPlayer(username);
            if(receiver == null){
                status.put(username, 3);
                continue;
            }

            //check that they are friends
            if (!friendDAO.isFriend(username)) {
                status.put(username, 4);
                continue;
            }

            //ensure that we havent sent to this player before today
            if (giftDAO.hasSentToPlayerToday(username)) {
                status.put(username, 5);
                continue;
            }

            //check if there any more crops to send
            if (!inventoryDAO.decrement(crop, 1)) {
                status.put(username, 6);
                continue;
            }

            //finally, send the gift!
            giftDAO.sendGift(receiver);

            //add to the receiver's inventory
            inventoryDAO.loadPlayerData(receiver);
            inventoryDAO.increment(crop, 1);
            inventoryDAO.loadPlayerData(currentPlayer); //swap back to currentPlayer

            status.put(username, 0);
        }
        return status;
    }

}
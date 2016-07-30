package game.controller;

import java.util.*;
import game.dao.*;
import game.entity.*;

/**
* A FriendController responds to commands by FriendMenu and execute these commands on the entity
*/
public class FriendController {
	private FriendDAO friendDAO;
	private Player currentPlayer;
	private PlayerDAO playerDAO;

    /**
     * Creates a FriendController object with the specified player
     * @param currentPlayer the currentPlayer who is player the game
     */
	public FriendController(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
        friendDAO = FriendDAO.getInstance(currentPlayer);
        playerDAO = PlayerDAO.getInstance();
    }

    /**
     * Gets the currentPlayer
     * @return the currentPlayer
     */
    public Player getPlayer() {
        return currentPlayer;
    }

    /**
     * Gets incoming friend requests
     * @return incoming friend requests
     */
    public ArrayList<Request> getIncomingRequests() {
        return friendDAO.getIncomingRequests();
    }

    /**
     * Gets the player's friends
     * @return ArrayList of player's friends
     */
    public ArrayList<Player> getFriends() {
        return friendDAO.getFriends();
    }

	/**
	 * Attempts to send request to the specified player.
	 * Sending a request may cause several errors, returning various error codes as follows
	 * 0 - Success
	 * 1 - No player with that username exists
	 * 2 - Player is already a friend
	 * 3 - Player cannot send friend request to himself
	 * 4 - There is already a pending request between user and the player he is trying to friend
	 * @param username
	 * @return the error code
	 */
	public int sendRequest(String username){
		//create an instance of playerDAO and retrieve the player for request to be sent
		Player newFriend = playerDAO.getPlayer(username);
		if (newFriend == null) {
			return 1;
		}

		//check if username is already a friend
		if (friendDAO.isFriend(username)) {
			return 2;
		}

		//check that user is not trying to friend himself
		if (username.equals(currentPlayer.getUsername())) {
			return 3;
		}


		//check if request has been sent to player before
        if (friendDAO.requestExists(currentPlayer, newFriend)) {
            return 4;
        }

		//sends the request
		friendDAO.sendRequest(currentPlayer, newFriend);
		return 0;
	}

    /**
     * Rejecting friend request
     * @param request
     */
	public void rejectRequest(Request request) {
		friendDAO.rejectRequest(request);
	}

    /**
     * Accepting friend request
     * @param request
     */
	public void acceptRequest(Request request) {
		friendDAO.acceptRequest(request);
	}

    /**
     * Unfriend another player
     * @param oldFriend
     */
	public void unfriendPlayer(Player oldFriend) {
		friendDAO.unfriend(currentPlayer, oldFriend);
	}
}
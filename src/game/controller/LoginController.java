package game.controller;

import game.entity.*;
import game.dao.*;
/**
* A LoginController responds to commands by LoginMenu and execute these commands on the entity
*/
public class LoginController {
	private PlayerDAO playerDAO;

	public LoginController() {
		playerDAO = PlayerDAO.getInstance();
	}

	/**
	 * Returns the Player with the specified username and correct password
	 * @param username
	 * @param password
	 * @return the Player that has been successfully authenticated. If username or password is invalid, returns null.
	 */
	public Player login(String username, String password) {
		Player p = playerDAO.getPlayer(username);
		if (p != null && p.authenticate(password)) {
			return p;
		}
		return null;
	}

	/**
	 * Checks for availability of the specified username
	 * @param username
	 * @return true if the username has not been used, false otherwise
	 */
	public boolean checkUsernameAvailability(String username) {
		return playerDAO.getPlayer(username) == null;
	}

	/**
	 * Registers a new player, saves to the csv, and creates the required user directories
	 * @param name
	 * @param username
	 * @param password
	 */
	public void createNewPlayer(String name, String username, String password) {
		playerDAO.addPlayer(name, username, password);
	}

}
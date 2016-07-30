package game.controller;

import game.dao.*;
import game.entity.*;
import java.util.*;

/**
* A InventoryController responds to commands by InventoryMenu and execute these commands on the entity
*/
public class InventoryController {
	private Player currentPlayer;
	private InventoryDAO inventoryDAO;
    private PlayerDAO playerDAO;

    /**
     * Creates an InventoryController with the specified player
     * @param currentPlayer the currentPlayer who is player the game
     */
	public InventoryController(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.inventoryDAO = InventoryDAO.getInstance(currentPlayer);
        this.playerDAO = PlayerDAO.getInstance();
	}

    /**
     * Gets the currentPlayer
     * @return the currentPlayer
     */
    public Player getPlayer() {
    	return currentPlayer;
    }

    /**
     * Gets all the crops of the currentPlayer
     * @return a HashMap of crops that the currentPlayer has
     */
    public HashMap<Crop, Integer> getAllCrops(){
        return inventoryDAO.getInventory();
    }

    /**
     * Checks if currentPlayer has gold to buy seeds
      * @param crop
     * @param quantity
     * @return true if currentPlayer has enough gold to buy, false if not enough gold
     */
    public boolean buySeeds(Crop crop, int quantity){
        if(currentPlayer.decrementGold(crop.getCost() * quantity)) {
            inventoryDAO.increment(crop, quantity);
            playerDAO.save();
            return true;
        } else {
            return false;
        }
    }
}
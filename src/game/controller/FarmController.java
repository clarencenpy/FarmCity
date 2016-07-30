package game.controller;

import java.util.*;
import game.dao.*;
import game.entity.*;

/**
* A FarmController responds to commands by FarmMenu and execute these commands on the entity
*/
public class FarmController {
	private Player currentPlayer;
	private PlayerDAO playerDAO;
	private FarmDAO farmDAO;
	private InventoryDAO inventoryDAO;
	private RankDAO rankDAO;

    /**
     * Creates a FarmController object with the specified player
     * @param currentPlayer the currentPlayer who is playing the game
     */
	public FarmController(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
		this.playerDAO = PlayerDAO.getInstance();
		this.farmDAO = FarmDAO.getInstance(currentPlayer);
		this.inventoryDAO = InventoryDAO.getInstance(currentPlayer);
		this.rankDAO = RankDAO.getInstance();
	}

    /**
     * Gets the currentPlayer
     * @return the currentPlayer
     */
	public Player getPlayer() {
		return currentPlayer;
	}

    /**
     * Gets the plots of the currentPlayer
     */
	public ArrayList<Plot> getPlots() {
		return farmDAO.getPlots();
	}

    /**
     * Check if the plot is available
     * @param plotNumber
     * @return true if plot is available, false if plot is not available
     */
	public boolean checkPlotAvailability(int plotNumber) {
		Plot p = farmDAO.getPlot(plotNumber - 1);
		return p.isEmpty();
	}

    /**
     * Gets the uniqueCrops of the currentPlayer
     * @return the uniqueCrops
     */
	public ArrayList<Crop> getUniqueCrops() {
		return inventoryDAO.getUniqueCrops();
	}

	/**
	 * Plants the chosen crop at the specified plot number
	 * @param plotNo number of the plot to plant on
	 * @param crop crop to be planted
	 */
	public void plant(int plotNo, Crop crop) {
		Plot p = farmDAO.getPlot(plotNo - 1);
		p.plant(crop);
		inventoryDAO.decrement(crop, 1); //this will definitely return true, otherwise, the user cannot possibly select this crop for planting
		farmDAO.save();
	}

	/**
	 * Goes through all of the player's plots and harvests all crops that are ready for harvest.
	 * Increments player's gold and xp
	 * @return list of harvests
	 */
	public ArrayList<Harvest> harvest() {
		ArrayList<Plot> plots = farmDAO.getPlots();
		ArrayList<Harvest> harvests = gatherHarvests(plots);

		// adding gold and xp
		for (Harvest h : harvests) {
			h.calculateYield();
			currentPlayer.addGold(h.getTotalGold());
			currentPlayer.addXp(h.getTotalXp());
		}

		playerDAO.save();

		//return arraylist for printing
		return harvests;
	}

	/**
	 * Looks at all the given plots and aggregates the crops into Harvest objects
	 * @param plots
	 * @return
	 */
	public ArrayList<Harvest> gatherHarvests(ArrayList<Plot> plots) {
        HashMap<Crop, Harvest> harvests = new HashMap<>();

        for (Plot p : plots) {
            if (p.canHarvest()) {
                Crop c = p.getCrop();
                if (harvests.containsKey(c)) {
                    harvests.get(c).increment();
                } else {
                    harvests.put(c, new Harvest(c));
                }
                 p.clear();
            }
        }
		farmDAO.save();
		return new ArrayList<>(harvests.values()); //ArrayList constructor can take in a Collection
	}

	/**
	 * Checks to see if the player has enough xp to level up.
	 * If true, levels up the player and adds new plots to his farm, and returns the new Rank.
	 * Otherwise, return null.
	 * @return the new rank if level up, otherwise null.
	 */
	public Rank tryLevelUp() {
		ArrayList<Plot> plots = farmDAO.getPlots();
		Rank curRank = currentPlayer.getRank();
		Rank r = rankDAO.calculateRank(currentPlayer.getXp());
		if (r != curRank) {	//enough xp to level up
			currentPlayer.setRank(r);
			farmDAO.addPlots(r.getNumPlot() - plots.size());
			playerDAO.save();
			return r;
		}
		return null;
	}

	/**
	 * Clears the plot with the specified plot number.
	 * @param plotNo the plot number of the plot to be cleared
	 * @return true if cleared successfully, false if there is no wilted plant to clear
	 */
	public boolean clear(int plotNo) {
		Plot p = farmDAO.getPlot(plotNo - 1);
		if (p.isWilted()) {
			p.clear();
			farmDAO.save();
			currentPlayer.decrementGold(5); //still proceed even if insufficient gold! special power
			playerDAO.save();
			return true;
		}
		return false;
	}

}
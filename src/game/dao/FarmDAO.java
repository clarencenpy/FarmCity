package game.dao;

import java.util.*;
import java.io.*;
import game.entity.*;
import com.opencsv.*;
import utility.*;
/**
 * A FarmDAO reads and writes the farm.csv
 */
public class FarmDAO {
	private ArrayList<Plot> plots;
	private Player currentPlayer;
	private CropDAO cropDAO;
    private static FarmDAO instance;

	/**
	 * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once
	 */
	private FarmDAO() {
		cropDAO = CropDAO.getInstance();
	}

    /**
     *
     * @param currentPlayer
     * @return a singleton instance of the DAO and load it with the current player's data
     */
    public static FarmDAO getInstance(Player currentPlayer) {
        if (instance == null) {
            instance = new FarmDAO();
        }
        instance.loadPlayerData(currentPlayer);
        return instance;
    }

    /**
     * Loads the specified player's plots from the csv file and assigns it to instance attribute plots. Also sets the currentPlayer instance attribute to the specified player. This method also does a check to ensure that the player has the correct number of plots as defined by his rank.
	 * This method should be called on the singleton instance whenever a new player logs in
     * @param currentPlayer
     */
    private void loadPlayerData(Player currentPlayer) {
		this.currentPlayer = currentPlayer;
        plots = read();

		//ensure that player has right number of plots
		while(plots.size() < currentPlayer.getRank().getNumPlot()) {
			plots.add(new Plot());
		}
		save();
    }

	/**
	 *
	 * @return an ArrayList<Plot> of the plots of land that belong to the current player
	 */
	private ArrayList<Plot> read() {
		ArrayList<Plot> plots = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + currentPlayer.getUsername() + "/farm.csv"),',','"',1)) {

			List<String[]> contents = reader.readAll();
			for (String[] content : contents) {
				plots.add(new Plot(
					content[0].equals("") ? null : cropDAO.getCrop(content[0]),
					content[1].equals("") ? null : new Date(Long.parseLong(content[1]))
				));
			}		
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		return plots;
	}

	/**
	 * Saves the current state of ArrayList<Plot> into the currentPlayer's directory
	 */
	public void save() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(Env.dataDir + currentPlayer.getUsername() + "/farm.csv"), ',')) {
            String[] headers = new String[] {
                    "Crop",
                    "PlantedTime(ms)"
            };
            writer.writeNext(headers);
			for (Plot p : plots) {
				Crop c = p.getCrop();
				Date plantedTime = p.getPlantedTime();
				String[] data = new String[] {
                        c == null ? "" : c.getName(),
                        plantedTime == null ? "" : "" + plantedTime.getTime()
                };
				writer.writeNext(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Adds the specified num of plots to the player's farm
	 * @param num number of plots to add
	 */
	public void addPlots(int num) {
		for (int i=0; i<num; i++) {
			plots.add(new Plot());
		}
		save();
	}

	/**
	 * Gets the plot of currentPlayer
	 * @return an ArrayList<Plot> containg the currentPlayer's plots of land
	 */
	public ArrayList<Plot> getPlots() {
		return plots;
	}

	/**
	 * Gets the plot
	 * @param n the plot number, where n ranges from 1 - number of plots the player has
	 * @return plot
	 */
	public Plot getPlot(int n) {
		return plots.get(n);
	}
}

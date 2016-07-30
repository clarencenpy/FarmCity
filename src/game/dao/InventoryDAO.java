package game.dao;

import java.util.*;
import java.io.*;
import game.entity.*;
import com.opencsv.*;
import utility.*;
/**
 * A InventoryDAO reads and writes the inventory.csv
 */
public class InventoryDAO {
	private HashMap<Crop, Integer> inventory;
	private Player currentPlayer;
    private CropDAO cropDAO;
    private static InventoryDAO instance;

    /**
     * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once.
     */
	private InventoryDAO() {
        cropDAO = CropDAO.getInstance();
	}
    

    /**
     *
     * @param currentPlayer
     * @return a singleton instance of the DAO and load it with the current player's data
     */
    public static InventoryDAO getInstance(Player currentPlayer) {
        if (instance == null) {
            instance = new InventoryDAO();
        }
        instance.loadPlayerData(currentPlayer);
        return instance;
    }

    /**
     * Loads the specified player's inventory from the csv file and assigns it to instance attribute inventory.
     * Also sets the currentPlayer instance attribute to the specified player.
     * This method should be called on the singleton instance whenever a new player logs in
     * @param currentPlayer the logged in user
     */
    public void loadPlayerData(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        inventory = read();
    }

    /**
     *
     * @return a HashMap where the key = inventory owned by the player and the value = its quantity from inventory.csv
     */
    private HashMap<Crop, Integer> read() {
        HashMap<Crop, Integer> crops = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + currentPlayer.getUsername() + "/inventory.csv"),',','"',1)) {
            List<String[]> contents = reader.readAll();
            for (String[] content : contents) {
                if (Integer.parseInt(content[1]) != 0) {    //ensure that we do not add crops with 0 quantity
                    crops.put(cropDAO.getCrop(content[0]), Integer.parseInt(content[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return crops;
    }

    /**
     * Saves the current state of the inventory HashMap to inventory.csv
     */
    private void save() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Env.dataDir + currentPlayer.getUsername() + "/inventory.csv"), ',')) {
            String[] headers = new String[] {
                    "Crop",
                    "Quantity"
            };
            writer.writeNext(headers);

            Set<Crop> keys = inventory.keySet();
            for (Crop c : keys) {
                String[] data = new String[] {c.getName(), ""+ inventory.get(c)};
                writer.writeNext(data);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Gets all uniqueCrops
     * @return a list of unique crops from the player's inventory.
     */
    public ArrayList<Crop> getUniqueCrops() {
		Set<Crop> keys = inventory.keySet();
		return new ArrayList<>(keys);
	}

    /**
     * Check if crop can be decrement
     * @param crop
     * @param amt
     * @return true if there are sufficient crops to decrement, false if insufficient amount or the crop doesnt exist
     */
    public boolean decrement(Crop crop, int amt) {
        if (inventory.containsKey(crop) && inventory.get(crop) >= amt) {
            int count = inventory.get(crop) - amt;
            if (count == 0) {
                inventory.remove(crop);
            } else {
                inventory.replace(crop, count);
            }
            save();
            return true;
        }
		return false;
	}

    /**
     * Decrement crop
     * @param crop
     * @param amt
     */
    public void increment(Crop crop, int amt) {
        if (inventory.containsKey(crop)) { //player already has the crop, so update only the qty
            int count = inventory.get(crop) + amt;
            inventory.replace(crop, count);
        } else { //dont have, so add the crop to the hashmap
            inventory.put(crop, amt);
        }
        save();
	}

    /**
     * Gets the player's inventory
     * @return a HashMap<Crop, Integer> of player's inventory
     */
    public HashMap<Crop, Integer> getInventory(){
        return inventory;
    }

}
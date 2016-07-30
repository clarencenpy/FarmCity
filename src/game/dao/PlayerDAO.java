package game.dao;

import game.entity.*;
import java.util.*;
import java.io.*;
import com.opencsv.*;
import utility.*;
/**
 * A PlayerDAO reads and writes the player.csv
 */
public class PlayerDAO {
	private ArrayList<Player> players;
    private static PlayerDAO instance;

	/**
	 * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once.
	 * The constructor loads the data of all the players from player.csv and assigns it to instance attribute players.
	 */
	private PlayerDAO() {
		players = read();
	}

	/**
	 *
	 * @return a singleton instance of the DAO
	 */
	public static PlayerDAO getInstance() {
        if (instance == null) {
            instance = new PlayerDAO();
        }
        return instance;
    }

	/**
	 *
	 * @return an ArrayList<Player> of all the players' data from players.csv
	 */
	private ArrayList<Player> read() {
        ArrayList<Player> players = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + "players.csv"),',','"',1)) {
            List<String[]> contents = reader.readAll();
            for (String[] content : contents) {
                players.add(new Player(
                        content[0],
                        content[1],
                        content[2],
                        Integer.parseInt(content[3]),
                        Integer.parseInt(content[4])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return players;
    }

	/**
	 * Saves the current state of ArrayList<Player> into players.csv
	 */
	public void save() {
		try (CSVWriter writer = new CSVWriter(new FileWriter(Env.dataDir + "players.csv"), ',')) {
			String[] data = new String[] {"name", "username", "password", "gold", "xp"};
			writer.writeNext(data);
			for (Player p : players) {
				data = new String[] {p.getName(), p.getUsername(), p.getPassword(), ""+p.getGold(), ""+p.getXp()};
				writer.writeNext(data);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

    /**
     * Registers a new player to the game, and creates the required file directories
     * @param name
     * @param username
     * @param password
     */
    public void addPlayer(String name, String username, String password) {
		players.add(new Player(name, username, password));
		try {
			File f = new File(Env.dataDir + username);
			File farm = new File(Env.dataDir + username + "/farm.csv");
			File inventory = new File(Env.dataDir + username + "/inventory.csv");
			File gifts = new File(Env.dataDir + username + "/gifts.csv");
			if (!f.exists()) {
				f.mkdir();
			}
			if (!farm.exists()) {
				farm.createNewFile();
			}
			if (!inventory.exists()) {
				inventory.createNewFile();
			}
			if (!gifts.exists()) {
				gifts.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		save();
	}

	/**
	 * Gets the player with the specified username
	 * @param username
	 * @return the Player with the specified username
	 */
	public Player getPlayer(String username) {
		for (Player p : players) {
			if (p.getUsername().equals(username)) {
				return p;
			}
		}
		return null;
	}


}
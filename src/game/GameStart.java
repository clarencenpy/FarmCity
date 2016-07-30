package game;

import com.opencsv.*;
import java.util.*;
import utility.*;
import game.ui.*;
import java.io.*;

/**
 * Executable class that initialises the game
 * Ensures that all the required csv files are present, otherwise, creates new empty csvs.
 * Additionally, it ensures that all players (as recorded on players.csv) have their required directories and csv files.
 * Otherwise, new files are created.
 */
public class GameStart {
	public static void main(String[] args) {

		//initialising required csv files if not present
		try {
			File friendships = new File(Env.dataDir + "friendships.csv");
			File players = new File(Env.dataDir + "players.csv");
			if (!friendships.exists()) {
				friendships.createNewFile();
			}
			if (!players.exists()) {
				players.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//creating required player directories
		try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + "players.csv"),',','"',1)) {
			List<String[]> contents = reader.readAll();
			for (String[] content : contents) {
				String username = content[1];
				File dir = new File(Env.dataDir + username);
				File farm = new File(Env.dataDir + username + "/farm.csv");
				File inventory = new File(Env.dataDir + username + "/inventory.csv");
				File gifts = new File(Env.dataDir + username + "/gifts.csv");
				if (!dir.exists()) {
					dir.mkdirs();
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
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		//clearing screen for windows
		for (int i=0; i<100; i++) {
			Screen.clear();
		}

		LoginMenu loginMenu = new LoginMenu();
		loginMenu.readOption();
	}
}
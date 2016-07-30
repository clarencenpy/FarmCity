package game.dao;

import com.opencsv.*;
import game.entity.*;
import utility.*;

import java.io.*;
import java.util.*;

/**
 * A RankDAO reads and writes the rank.csv
 */
public class RankDAO {
    private ArrayList<Rank> rankList;
    private static RankDAO instance;

    /**
     * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once.
     * The constructor loads the rank data from the rank.csv and assigns it to instance attribute rankList.
     */
    private RankDAO() {
        rankList = read();
    }

    /**
     *
     * @return a singleton instance of the dao
     */
    public static RankDAO getInstance() {
        if (instance == null) {
            instance = new RankDAO();
        }
        return instance;
    }

    /**
     *
     * @return an ArrayList<Rank> of the ranks defined in rank.csv
     */
    public ArrayList<Rank> read() {
        ArrayList<Rank> rankList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + "rank.csv"),',','"',1)) {
            List<String[]> contents = reader.readAll();
            for (String[] content : contents) {
                rankList.add(new Rank(content[0],Integer.parseInt(content[1]),Integer.parseInt(content[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return rankList;
    }

    /**
     * Returns the rank corresponding to the player xp. Assumes that the ArrayList is ordered by ascending xp.
     * @param xp
     * @return
     */
    public Rank calculateRank(int xp) {
		Rank output = null;
		for (Rank r : rankList) {
			if (r.getXp() <= xp) {
				output = r;
			}
		}
		return output;
	}
}

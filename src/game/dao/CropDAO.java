package game.dao;

import com.opencsv.*;
import game.entity.*;
import utility.*;

import java.io.*;
import java.util.*;

/**
 * A CropDAO reads and writes the crop.csv
 */
public class CropDAO {
    private ArrayList<Crop> cropList;
    private static CropDAO instance;

    /**
     * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once.
     * The constructor loads the crop data defined in crop.csv and assigns it to instance attribute cropList.
     */
    private CropDAO() {
        cropList = read();
    }

    /**
     *
     * @return an ArrayList<Crop> of the ranks defined in crop.csv
     */
    public ArrayList<Crop> read() {
        ArrayList<Crop> cropList = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + "crop.csv"),',','"',1)) {
            List<String[]> contents = reader.readAll();
            for (String[] content : contents) {
                cropList.add(new Crop(
                        content[0],
                        Integer.parseInt(content[1]),
                        Integer.parseInt(content[2])/Integer.parseInt(Env.properties.getProperty("speedMultiplier")),
                        Integer.parseInt(content[3]),
                        Integer.parseInt(content[4]),
                        Integer.parseInt(content[5]),
                        Integer.parseInt(content[6])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return cropList;
    }

    /**
     *
     * @return a singleton instance of the dao
     */
    public static CropDAO getInstance() {
        if (instance == null) {
            instance = new CropDAO();
        }
        return instance;
    }

    /**
     * Gets the crop with the specified name
     * @param name
     * @return the crop with the specified name, or null if no such crop is present
     */
    public Crop getCrop(String name) {
        for (Crop c : cropList) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * Gets the list of crops of the currentPlayer
      * @return an ArrayList<Crop> containing the currentPlayer's crops
     */
    public ArrayList <Crop> getCropList(){
      return cropList;      
    }
    
    
}

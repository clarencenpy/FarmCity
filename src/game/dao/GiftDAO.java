package game.dao;

import java.util.*;
import java.io.*;
import game.entity.*;
import com.opencsv.*;
import utility.*;
/**
 * A GiftDAO reads and writes the gift.csv
 */
public class GiftDAO {
	private Player currentPlayer;
    private PlayerDAO playerDAO;
    private static GiftDAO instance;
    private ArrayList<Gift> gifts;

    /**
     * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once.
     */
    private GiftDAO(){
        playerDAO = PlayerDAO.getInstance();
    }

    /**
     *
     * @param currentPlayer
     * @return a singleton instance of the DAO and load it with the current player's data
     */
    public static GiftDAO getInstance(Player currentPlayer){
        if(instance == null){
            instance = new GiftDAO();
        }
        instance.loadPlayerData(currentPlayer);
        return instance;
    }

    /**
     * Loads the specified player's gifts from the csv file and assigns it to instance attribute gifts. Also sets the currentPlayer instance attribute to the specified player.
     * This method should be called on the singleton instance whenever a new player logs in
     * @param currentPlayer the logged in user
     */
    private void loadPlayerData(Player currentPlayer){
        this.currentPlayer = currentPlayer;
        gifts = read();
    }

    /**
     *
     * @return an ArrayList<Gift> from gifts.csv
     */
    private ArrayList<Gift> read(){
        ArrayList<Gift> gifts = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + currentPlayer.getUsername() + "/gifts.csv"), ',', '"', 1)) {
            List<String[]> contents = reader.readAll();
            for (String[] content : contents) {
                gifts.add(new Gift(
                        playerDAO.getPlayer(content[0]),
                        new Date(Long.parseLong(content[1]))
                ));
            }
        } catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
        return gifts;
    }

    /**
     * Saves the current state of ArrayList<Gift> into gifts.csv
     */
    private void save() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Env.dataDir + currentPlayer.getUsername() + "/gifts.csv"), ',')) {
            String[] headers = new String[] {
                    "Receiver",
                    "Send Date"
            };
            writer.writeNext(headers);

            for (Gift g : gifts) {
                String[] data = {
                        g.getReceiver().getUsername(),
                        ""+g.getSentDate().getTime()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Gets list of gifts that have been sent today (since 00:00)
     * @return an ArrayList<Gift> containing gifts that have been sent today (since 00:00)
     */
    private ArrayList<Gift> getGiftsSentToday() {
        ArrayList<Gift> output = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        now.setTime(new Date());
        for (Gift g : gifts) {
            GregorianCalendar sent = new GregorianCalendar();
            sent.setTime(g.getSentDate());

            if (sent.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) &&
                    sent.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
                output.add(g);
            }
        }
        return output;
    }

    /**
     * Check if the player has already sent a gift to the specified player today (since 00:00)
     * @param username
     * @return true if player sent, false if player did not send
     */
    public boolean hasSentToPlayerToday(String username) {
        ArrayList<Gift> giftsSentToday = getGiftsSentToday();
        for (Gift gift : giftsSentToday) {
            if(gift.getReceiver().getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player reached the daily limit of gift sending
     * @return true if reached, false if did not reached
     */
    public boolean reachedDailyLimit() {
        return getGiftsSentToday().size() >= 5;
    }

    /**
     * Send gift
      * @param receiver
     */
    public void sendGift(Player receiver){
        gifts.add(new Gift(receiver));
        save();
    }
}
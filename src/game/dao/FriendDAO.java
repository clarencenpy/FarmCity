package game.dao;

import com.opencsv.*;
import game.entity.*;
import utility.*;

import java.io.*;
import java.util.*;
/**
 * A FriendDAO reads and writes the friend.csv
 */
public class FriendDAO {
    private ArrayList<Request> allRequests;
    private ArrayList<Player> friends;
    private ArrayList<Request> incomingRequests;
    private PlayerDAO playerDAO;
    private Player currentPlayer;
    private static FriendDAO instance;

    /**
     * Singleton pattern uses a private constructor to ensure that this class can only be instantiated once.
     * The constructor loads the friendship data from the friendships.csv and assigns it to instance attribute allRequests.
     */
    private FriendDAO(){
        playerDAO = PlayerDAO.getInstance();
        allRequests = read();
    }

    /**
     *
     * @param currentPlayer
     * @return a singleton instance of the DAO and load it with the current player's data
     */
    public static FriendDAO getInstance(Player currentPlayer) {
        if (instance == null) {
            instance = new FriendDAO();
        }
        instance.loadPlayerData(currentPlayer);
        return instance;
    }

    /**
     * Filters player specific data from the global requests list
     * @param currentPlayer the logged in user
     */
    private void loadPlayerData(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        friends = extractFriends();
        incomingRequests = extractIncomingRequests();
    }

    /**
     *
     * @return an ArrayList<Request> from friendships.csv
     */
    private ArrayList<Request> read() {
        ArrayList<Request> allRequests = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(Env.dataDir + "friendships.csv"),',','"',1)) {
            List<String[]> contents = reader.readAll();
            for (String[] content : contents) {
                allRequests.add(new Request(
                        playerDAO.getPlayer(content[0]), //sender
                        playerDAO.getPlayer(content[1]), //receiver
                        RequestStatus.valueOf(content[2]) //status
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return allRequests;
    }

    /**
     * Saves the current state of ArrayList<Request> to a friendships.csv file
     */
    private void save() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(Env.dataDir + "friendships.csv"), ',')) {
            String[] headers = new String[] {
                    "Sender",
                    "Receiver",
                    "Status"
            };
            writer.writeNext(headers);
            for (Request r : allRequests) {
                String[] data = new String[] {
                        r.getSender().getUsername(),
                        r.getReceiver().getUsername(),
                        r.getStatus().toString()
                };
                writer.writeNext(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    /**
     * Returns the player's list of friends
     * @return an ArrayList<Player> containing only confirmed friends
     */

    private ArrayList<Player> extractFriends() {
        ArrayList<Player> friends = new ArrayList<>();
        for (Request r : allRequests) {
            if(r.getReceiver() == currentPlayer && r.getStatus() == RequestStatus.ACCEPTED) {
                friends.add(r.getSender());
            }

            if(r.getSender() == currentPlayer && r.getStatus() == RequestStatus.ACCEPTED) {
                friends.add(r.getReceiver());
            }
        }
        return friends;
    }

    /**
     * Gets all unresponded requests
     * @return an ArrayList<Request> of unresponded requests from other players
     */
    private ArrayList<Request> extractIncomingRequests() {
        ArrayList<Request> requests = new ArrayList<>();
        for(Request r : allRequests) {
            if (r.getReceiver() == currentPlayer && r.getStatus() == RequestStatus.PENDING) {
                requests.add(r);
            }
        }
        return requests;
    }

    /**
     * Send friend request
     * @param sender
     * @param receiver
     */
    public void sendRequest(Player sender, Player receiver) {
        allRequests.add(new Request(sender, receiver, RequestStatus.PENDING));
        //reload friendship data
        loadPlayerData(currentPlayer);
        save();
    }

    /**
     * Reject friend request
     * @param r request object to be rejected
     */
    public void rejectRequest(Request r) {
        r.setStatus(RequestStatus.REJECTED);
        //reload friendship data
        loadPlayerData(currentPlayer);
        save();
    }

    /**
     * Accept friend request
     * @param r request object to be accepted
     */
    public void acceptRequest(Request r) {
        r.setStatus(RequestStatus.ACCEPTED);
        //reload friendship data
        loadPlayerData(currentPlayer);
        save();
    }

    /**
     * Searches for the request between two players and removes it
     * @param currentPlayer
     * @param toUnfriend
     */
    public void unfriend(Player currentPlayer, Player toUnfriend) {
        for (int i=0; i<allRequests.size(); i++) {
            Request r = allRequests.get(i);
            //find the request for this relationship and remove it
            if (r.getSender().getUsername().equals(currentPlayer.getUsername()) && r.getReceiver().getUsername().equals(toUnfriend.getUsername()) ||
                    r.getReceiver().getUsername().equals(currentPlayer.getUsername()) && r.getSender().getUsername().equals(toUnfriend.getUsername())) {
                allRequests.remove(i);
                break;
            }
        }
        //reload friendship data
        loadPlayerData(currentPlayer);
        save();
    }

    /**
     * Checks if there is already a pending request between two players
     * @param currentPlayer
     * @param otherPlayer
     * @return true if pending request can be found, false otherwise
     */
    public boolean requestExists(Player currentPlayer, Player otherPlayer) {
        for (Request r: allRequests) {
            if (r.getSender() == currentPlayer && r.getReceiver() == otherPlayer && r.getStatus() == RequestStatus.PENDING) {
                return true;
            }
            if (r.getSender() == otherPlayer && r.getReceiver() == currentPlayer && r.getStatus() == RequestStatus.PENDING) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the player with the specified username is a friend
     * @param username
     * @return true if they are friends, false if they are not
     */
    public boolean isFriend(String username) {
        for (Player p : friends) {
            if (p.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the currentPlayer's friends
     * @return an ArrayList<Player> containing the currentPlayer's friends
     */
    public ArrayList<Player> getFriends() {
        return friends;
    }

    /**
     * Gets the currentPlayers's request
     * @return an ArrayList<Request> containing the currentPlayer's request
     */
    public ArrayList<Request> getIncomingRequests() {
        return incomingRequests;
    }

}

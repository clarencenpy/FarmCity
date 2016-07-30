package game.entity;

import game.dao.*;

/**
 * A Player represents a person with a name, username, password, gold, xp and rank
 */
public class Player{
    private String name;
    private String username;
    private String password;
    private int gold;
    private int xp;
    private Rank rank;

    /**
     * Creates a Player object with the specified name, username and password
     * @param name the player's name
     * @param username the player's username
     * @param password the player's password
     */
    public Player(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
        gold = 1000;
        xp = 0;
        rank = RankDAO.getInstance().calculateRank(xp);
    }

    /**
     * Creates a Player object with the specified name, username, password, gold and xp
     * @param name the player's name
     * @param username the player's username
     * @param password the player's password
     * @param gold the player's gold
     * @param xp the player's xp
     */
    public Player(String name, String username, String password, int gold, int xp) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.gold = gold;
        this.xp = xp;
        rank = RankDAO.getInstance().calculateRank(xp);
    }

    /**
     * Authenticate password
     * @param password check if the password is correct
     * @return true if password is correct, false if password is wrong
     */
    public boolean authenticate(String password) {
        return password.equals(this.password);
    }


    /**
     * Gets the name of this player
     * @return the name of this player
     */
    public String getName(){
        return name;
    }


    /**
     * Gets the username of this player
     * @return the username of this player
     */
    public String getUsername(){
        return username;
    }


    /**
     * Gets the password of this player
     * @return the password of this player
     */
    public String getPassword(){
        return password;
    }

    /**
     * Gets the amount of gold this player has
     * @return the amount of gold this player has
     */
    public int getGold(){
        return gold;
    }

    /**
     * Increase player's gold by the specified amount
     * @param gold amount of gold to be increase
     */
	public void addGold(int gold){
		this.gold += gold;
	}

    /**
     * Gets the xp of this player
     * @return the xp of this player
     */
    public int getXp(){
        return xp;
    }

    /**
     * Increase player's xp by the specified amount
     * @param xp amount of xp to be increase
     */
	public void addXp(int xp){
		this.xp += xp;
	}

    /**
     * Gets the rank of this player
     * @return the rank of this player
     */
    public Rank getRank(){
        return rank;
    }

    /**
     * Sets the rank of this player
     * @param rank the rank of this player
     */
    public void setRank(Rank rank){
        this.rank = rank;
    }

    /**
     * Decrement player's gold by specified amount
     * @param amount amount of gold to be decremented
     * @return true if successful, false if insufficient gold
     */
    public boolean decrementGold(int amount) {
        if (amount > gold) {
            return false;
        }
        gold -= amount;
        return true;
    }

}
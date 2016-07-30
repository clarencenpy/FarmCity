package game.entity;

/**
 * A Rank presents the rank of a player
 */
public class Rank{
    private String name;
    private int xp;
    private int numPlot;

    /**
     * Creates a Rank object with the specified name, xp and numPlot
     * @param name name of this rank
     * @param xp xp of this rank
     * @param numPlot numPlot of this rank
     */
    public Rank(String name, int xp, int numPlot){
        this.name = name;
        this.xp = xp;
        this.numPlot = numPlot;
    }

    /**
     * Gets the name of this rank
     * @return the name of this rank
     */
    public String getName() {
        return name;
    }


    /**
     * Gets the xp of this rank
     * @return the xp of this rank
     */
    public int getXp() {
        return xp;
    }


    /**
     * Gets the numPlot of this rank
     * @return the numPlot of this rank
     */
    public int getNumPlot() {
        return numPlot;
    }

}
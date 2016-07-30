package game.entity;

/**
 * A Crop represents a plant with a name, cost, time, xp, minYield, maxYield and salePrice
 */
public class Crop {

    private String name;
    private int cost;
    private int time;
    private int xp;
    private int minYield;
    private int maxYield;
    private int salePrice;

    /**
     *Creates a Crop object with the specified name, cost, time, xp, minYield, maxYield and salePrice
     * @param name the crop's name
     * @param cost the crop's cost
     * @param time the crop's harvest time
     * @param xp the crop's xp
     * @param minYield the crop's minYield
     * @param maxYield the crop's maxYield
     * @param salePrice the crop's salePrice
     */
    public Crop(String name, int cost, int time, int xp, int minYield, int maxYield, int salePrice){
        this.name = name;
        this.cost = cost;
        this.time = time;
        this.xp = xp;
        this.minYield = minYield;
        this.maxYield = maxYield;
        this.salePrice = salePrice;
     
    }

    /**
     * Gets the harvest time of this crop in minutes
     * @return the harvest time of this crop
     */
    public int getTime() {
        return time;
    }

    /**
     * Shows the time in ? hours, ? mins format
     * @return formatted string of the time
     */
    public String getFormattedTime() {
        int hours = 0;
        int t = time;

        if (t <= 60) {
            return t + " mins";
        }

        String output = "";
        while (t > 59) {
            t = t-60;
            hours++;
        }
        if (hours != 0) {
            output += hours + " hours";
        }
        if (t != 0) {
            output += t + " mins";
        }
        return output;
    }

    /**
     * Gets the xp of this crop
     * @return the xp of this crop
     */
    public int getXp() {
        return xp;
    }

    /**
     * Gets the name of this crop
     * @return the name of this crop
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the cost of this crop
     * @return the cost of this crop
     */
    public int getCost() {
        return cost;
    }

    /**
     * Gets the sale price of this crop
     * @return the salePrice of this crop
     */
    public int getSalePrice() {
        return salePrice;
    }

    /**
     *Gets a randomly generated yield value from minYield to maxYield inclusive
     * @return A randomly generated yield value from minYield to maxYield inclusive
     */
    public int getYield() {
		return (int)(Math.random() * (maxYield - minYield) + minYield);
	}
	
}


package game.entity;

/**
 * A Harvest represents the harvest of a specified crop, quantity and yield
 */
public class Harvest {
	private Crop crop;
	private int quantity;
	private int yield;
	
    /**
     * Creates a Harvest object with the specified crop
     * @param crop the crop to be harvest 
     */
	public Harvest(Crop crop){
		this.crop = crop;
		quantity = 1;
	}
    
    /**
     * Increment the quantity
     */
	public void increment() {
		quantity++;
	}
    
    /**
     * Calculate the yield 
     */
	public void calculateYield() {
		for (int i=0; i<quantity; i++) {
			yield += crop.getYield();
		}
	}
	
    /**
     * Gets the yield of the harvest
     * @return the yield of the harvest
     */
	public int getYield() {
		return yield;
	}
	
    /**
     * Gets the crop of the harvest
     * @return the crop of the harvest
     */
	public Crop getCrop() {
		return crop;
	}
	
    /**
     * Gets the total xp gained from the harvest
     * @return the totalXp gained
     */
	public int getTotalXp() {
		return yield*crop.getXp();
	}
	
    /**
     * Gets the total gold gained from the harvest
     * @return the totalGold gained
     */
	public int getTotalGold() {
		return yield*crop.getSalePrice();
	}
}
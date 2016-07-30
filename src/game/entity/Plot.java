package game.entity;

import java.util.*;

/**
 * A Plot represents a piece of land with a crop, plantedTime, harvestedTime, wiltTime
 */
public class Plot {
	private Crop crop;
    private Date plantedTime;
    private Date harvestTime;
    private Date wiltTime;

    /**
     *Creates a Plot object with the specified crop and planted time
     * @param crop crop to be planted on the plot
     * @param plantedTime date that the crop is planted
     */
    public Plot(Crop crop, Date plantedTime) {
    	this.crop = crop;
    	this.plantedTime = plantedTime;
        if (crop != null && plantedTime != null) {
            harvestTime = new Date(plantedTime.getTime() + crop.getTime()*60*1000);
            wiltTime = new Date(plantedTime.getTime() + crop.getTime()*60*1000*2);
        }
    }

    /**
     * Creates a Plot object with no specified parameter
     */
    public Plot() {
        //initialise to null
    }

    /**
     * Gets the crop of this plot
     * @return the crop of this plot
     */
    public Crop getCrop(){
    	return crop;
    }

    /**
     * Gets the harvest time of this plot
     * @return the harvestTime of this plot
     */
    public Date getHarvestTime(){
    	return harvestTime;
    }

    /**
     * Gets the wilt time of this plot
     * @return the wiltTime of this plot
     */
    public Date getWiltTime(){
    	return wiltTime;
    }

    /**
     * Gets the planted time of this plot
     * @return the plantedTime of this plot
     */
    public Date getPlantedTime(){
        return plantedTime;
    }


    /**
     * Checks if the crop can be planted
     * @param crop the crop to be plant
     * @return true if the crop can be plant, false if the the crop cannot be plant
     */
    public boolean plant(Crop crop){
    	if (this.crop == null) {
    		this.crop = crop;
            this.plantedTime = new Date();
            this.harvestTime = new Date(plantedTime.getTime() + crop.getTime()*60*1000);
            this.wiltTime = new Date(plantedTime.getTime() + crop.getTime()*60*1000*2);
    		return true;
    	} else {
    		return false;
    	}
    }

    /**
     * Checks if the plot can be harvested
     * @return true if the plot can be harvest, false if the plot cannot be harvest
     */
    public boolean canHarvest() {
        if(crop != null) {
            Date currentTime = new Date();
            return currentTime.after(harvestTime) && currentTime.before(wiltTime);
        } 
        return false;
	}

    /**
     * Checks if plot is empty
     * @return true if the plot is empty, false if the plot is not empty
     */
	public boolean isEmpty() {
        return crop == null;
    }

    /**
     * Checks if the crop has wilted
     * @return true if the crop is wilted, false if the crop is not wilted
     */
    public boolean isWilted() {
        if (crop != null) {
            Date currentTime = new Date();
            return currentTime.after(wiltTime);
        }
        return false;
    }

    /**
     * Clears the plot
     */
    public void clear() {
        crop = null;
        plantedTime = null;
        harvestTime = null;

        wiltTime = null;
    }
}
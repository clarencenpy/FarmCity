package game.ui;

import java.util.*;
import game.controller.*;
import game.entity.*;
import utility.*;

    /**
     * A FarmMenu represents the menu which let the player manages his farm
     */
public class FarmMenu {
	private FarmController farmCtrl;

    /**
     * Creates a FarmMenu object with the specified FarmController
     * @param farmCtrl the farmCtrl of this farmMenu
     */
	public FarmMenu(FarmController farmCtrl) {
		this.farmCtrl = farmCtrl;
	}

    /**
     * Displays the player's rank, gold and plots.
     */
	public void display() {
		System.out.println("== Farm City :: My Farm ==");
		Player p = farmCtrl.getPlayer();
		System.out.println("Welcome, " + p.getName() + "!");
		System.out.println("Rank: " + p.getRank().getName() + "\tGold: " + p.getGold());
		System.out.println();

		ArrayList<Plot> plots = farmCtrl.getPlots();
		System.out.println("You have " + plots.size() + " plots of land.");
		for (int i=0; i<plots.size(); i++) {
			String cropGrown;
			String cropStatus;
			Plot plot = plots.get(i);
			Crop c = plot.getCrop();
			if (c == null) {
				//empty plot
				cropGrown = "<empty>";
				cropStatus = "";
			} else {
				cropGrown = c.getName();
				//calculate percentage growth (elapsed time / harvest time * 100)
				Date now = new Date();
				Date plantedTime = plot.getPlantedTime();
				int percentGrowth = (int) ( (now.getTime() - plantedTime.getTime()) / (c.getTime()*60*1000.0) * 100 );
				cropStatus = renderStatusBar(percentGrowth);
			}
			System.out.println(i+1 + ". " + cropGrown + "\t" + cropStatus);
		}
	}

    /**
     * Takes in the player's input
     */
	public void readOption() {
		Scanner sc = new Scanner(System.in);
		String input;
		do {
			display();
			System.out.println();
			System.out.print("[M]ain | [P]lant | C[L]ear | [H]arvest > ");
			System.out.flush();
			input = sc.nextLine();
			input = input.toUpperCase();
			input = input.length()==0 ? "err" : input;	//so that empty strings wont throw indexoutofbounds
			int num;

			switch (input.charAt(0)) {
				case 'P':
					Screen.clear();
					try {
						num = Integer.parseInt(input.substring(1));
						int numPlot = farmCtrl.getPlayer().getRank().getNumPlot();
						if (num < 1 || num > numPlot) {
							Screen.yellowln("Choose a plot between 1 - " + numPlot);
							System.out.println();
							break;
						}
						processPlant(num);
					} catch (NumberFormatException e) {
						Screen.yellowln("Invalid input");
						System.out.println();
					}					
					break;
				case 'L': 
					Screen.clear();
					try {
						num = Integer.parseInt(input.substring(1));
						int numPlot = farmCtrl.getPlayer().getRank().getNumPlot();
						if (num < 1 || num > numPlot) {
							Screen.yellowln("Choose a plot between 1 - " + numPlot);
							System.out.println();
							break;
						}
						processClear(num);
					} catch (NumberFormatException e) {
						Screen.yellowln("Invalid input");
						System.out.println();
					}
					break;
				case 'H':
					Screen.clear();
					if (input.length() > 1) {
						Screen.yellowln("Invalid input");
						System.out.println();
						break;
					}
					processHarvest();

					break;
                case 'M':
                    Screen.clear();
                    if (input.length() > 1) {
                        Screen.yellowln("Invalid input");
						System.out.println();
					}
                    break;
				default:
					Screen.clear();
					Screen.yellowln("Invalid input");
					System.out.println();
					break;
			}
		} while (!input.equals("M"));
	}

    /**
     * Processes planting of crop on the specified plot
     * @param plotNo the plot that this player chose
     */
	public void processPlant(int plotNo) {
		//check whether plot is empty first
		if (!farmCtrl.checkPlotAvailability(plotNo)) {
			Screen.yellowln("Plot is already in use. Please harvest or clear first before planting.");
			System.out.println();
			return;
		}

		//display list of crops available for planting
		ArrayList<Crop> crops = farmCtrl.getUniqueCrops();
		if (crops.size() == 0) {
			Screen.yellowln("Oops. You do not have any crops in your inventory.");
			System.out.println();
			return;
		}
		System.out.println("Select the crop:");
		for (int i=0; i<crops.size(); i++) {
			System.out.println(i+1 + ". " + crops.get(i).getName());
		}
		System.out.println();

		Scanner sc = new Scanner(System.in);
		int choice = 0;

		boolean valid = false;
		while (!valid) {
            System.out.print("[M]ain | Select choice > ");
            System.out.flush();
            String input = sc.nextLine().trim().toUpperCase();

            if(input.equals("M")){
                Screen.clear();
                return;
            } else {
                try {
                    choice = Integer.parseInt(input);
                    if (choice < 1 || choice > crops.size()) {
                        Screen.yellowln("Invalid choice, please enter a number from 1 - " + crops.size());
                        System.out.println();
                    } else {
                        valid = true;
                    }
                } catch (NumberFormatException e) {
                    Screen.yellowln("Invalid input");
                    System.out.println();
                }
            }
		}

		Crop crop = crops.get(choice - 1);
		farmCtrl.plant(plotNo, crop);
		Screen.clear();
		Screen.greenln("Success! " + crop.getName() + " planted on Plot " + plotNo);
		System.out.println();
	}

    /**
     * Processes the clearing of the specified plot
     * @param plotNo the plot to be clear
     */
	public void processClear(int plotNo) {	
		if (farmCtrl.clear(plotNo)) {
			Screen.greenln("Plot " + plotNo + " is cleared");
			System.out.println();
		} else {
			Screen.yellowln("Plot " + plotNo + " does not have a wilted plant to clear");
			System.out.println();
		}
	}

    /**
     * Processes the harvesting of all plots
     */
	public void processHarvest() {
		ArrayList<Harvest> harvests = farmCtrl.harvest();
		if (harvests.size() > 0) {
			for (Harvest h : harvests) {
				Screen.greenln("You have harvested " + h.getYield() + " units of " + h.getCrop().getName() + " for " + h.getTotalXp() + " XP and " + h.getTotalGold() + " gold.");
				System.out.println();
			}
			tryLevelUp(); //try to level up, since xp may have increased
		} else {
			Screen.yellowln("No crops are available for harvest");
			System.out.println();
		}


	}

    /**
     * Displays new rank when player levels up
     */
	public void tryLevelUp() {
		Rank r = farmCtrl.tryLevelUp();
		if (r != null) {
			Screen.greenln("< LEVEL UP > You are now a " + r.getName() + " with " + r.getNumPlot() + " plots of land. Keep farming!");
			System.out.println();
		}
	}

    /**
     * Displays the growth status of the crop
     * @param percent the percentage growth of the crop
     * @return the growth status of the player's crop
     */
	private String renderStatusBar(int percent) {
		String output = "[";
		if (percent >= 200) {
			return output + "  wilted  ]";
		}
		if (percent > 100) {
			percent = 100;
		}
		int n = percent/10;
		for (int i=0; i<n; i++) {
			output += "#";
		}
		for (int i=0; i<10-n; i++) {
			output += "-";
		}
		output += "] " + percent + "%";
		return output;
	}
}
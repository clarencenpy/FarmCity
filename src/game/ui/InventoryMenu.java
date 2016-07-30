package game.ui;

import java.util.*;
import game.controller.*;
import game.entity.*;
import utility.*;
import game.dao.*;
/**
 * A InventoryMenu represents the menu which let the player manages his inventory
 */
public class InventoryMenu {
	private InventoryController inventoryCtrl;
    private GiftController giftCtrl;

    /**
     * Creates a InventoryMenu object with the specified InventoryController and GiftController
     * @param inventoryCtrl the inventoryCtrl of this InventoryMenu
     */
	public InventoryMenu(InventoryController inventoryCtrl, GiftController giftCtrl) {
		this.inventoryCtrl = inventoryCtrl;
        this.giftCtrl = giftCtrl;
	}

    /**
     * Displays the player's inventory of seeds
     */
    public void display(){
        System.out.println("== Farm City :: My Inventory ==");
        Player p = inventoryCtrl.getPlayer();
        System.out.println("Welcome, " + p.getName() + "!");
        System.out.println("Rank: " + p.getRank().getName() + "\tGold: " + p.getGold());
        System.out.println();
        
        int counter = 1;    
        System.out.println("My Seeds: ");
        HashMap<Crop, Integer> cropList = inventoryCtrl.getAllCrops();
        if (cropList.size() == 0) {
            System.out.println("You have no seeds. Enter <B> to buy some!");
        }
        Set<Crop> crops = cropList.keySet();
        for(Crop c : crops){
            System.out.println(counter + ". " + cropList.get(c) + " bag(s) of " + c.getName());
            counter++;
        }
    }

    /**
     * Processes the purchasing of seeds
     */
    public void processBuySeeds(){
        Scanner sc = new Scanner(System.in);       
        System.out.println("== Farm City :: Store ==");
        Player p = inventoryCtrl.getPlayer();
        System.out.println("Welcome, " + p.getName() + "!");
        System.out.println("Rank: " + p.getRank().getName() + "\tGold: " + p.getGold());
        System.out.println();
        
        //Show every single crop that is available
        System.out.println("Seeds Available: ");
        ArrayList <Crop> cropList = CropDAO.getInstance().getCropList();
        for (int i = 0; i < cropList.size(); i++){
            Crop c = cropList.get(i);
            System.out.println((i + 1) + ". \t" +
                    c.getName() + " costs: " + c.getCost() + " gold\n\t" +
                    "Harvests in: " + c.getFormattedTime() + "\n\t" +
                    "XP Gained: " + c.getXp());
            System.out.println();
        }
        System.out.println();

        int cropChoice = 0;
        int quantity = 0;

        //prompt for choice
        boolean validChoice = false;
        while (!validChoice) {
            System.out.print("[M]ain | Select choice > ");
            System.out.flush();
            String input = sc.nextLine().trim().toUpperCase();

            if(input.equals("M")){
                Screen.clear();
                return;
            } else {
                try {
                    cropChoice = Integer.parseInt(input);
                    if (cropChoice < 1 || cropChoice > cropList.size()) {
                        Screen.yellowln("Invalid choice, please enter a number from 1 - " + cropList.size());
                        System.out.println();
                    } else {
                        validChoice = true;
                    }
                } catch (NumberFormatException e) {
                    Screen.yellowln("Invalid input");
                    System.out.println();
                }
            }
        }

        //prompt for quantity, ensure positive number
        boolean validQuantity = false;

        while (!validQuantity) {
            System.out.print("Enter quantity > ");
            System.out.flush();
            String input = sc.nextLine().trim().toUpperCase();

            try {
                quantity = Integer.parseInt(input);
                if (quantity > 0) {
                    validQuantity = true;
                    Screen.clear();
                } else {
                    Screen.yellowln("Invalid quantity. Please enter a positive number");
                    System.out.println();
                }
            } catch (NumberFormatException e) {
                Screen.yellowln("Invalid input");
                System.out.println();
            }
        }

        Crop crop = cropList.get(cropChoice - 1);
        if(inventoryCtrl.buySeeds(crop, quantity)){
            Screen.greenln(quantity + " bags of " + crop.getName() + " seeds purchased for "
                + (crop.getCost() * quantity) + " gold. ");
            System.out.println();    
        } else {
            Screen.yellowln("Insufficient gold");
            System.out.println();
        }

    }

    /**
     * Processes the sending of gifts
     */
    public void processSendGift(){

        if(giftCtrl.reachedDailyLimit()) {
            Screen.clear();
            Screen.yellowln("You have reached your maximum of sending 5 gifts today.");
            System.out.println();
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("== Farm City :: Send a Gift ==");
        Player p = giftCtrl.getPlayer();
        System.out.println("Welcome, " + p.getName() + "!");
        System.out.println("Rank: " + p.getRank().getName() + "\tGold: " + p.getGold());
        System.out.println();

        System.out.println("Gifts Available: ");
        System.out.println();

        ArrayList <Crop> cropList = CropDAO.getInstance().getCropList();

        //display all available crops
        for (int i = 0; i < cropList.size(); i++){
            Crop c = cropList.get(i);
            System.out.println((i + 1) + ". 1 bag of " + c.getName() + " Seeds");
        }
        System.out.println();

        int choice = 0;
        //prompt for choice
        boolean validChoice = false;

        while (!validChoice) {
            System.out.print("[M]ain | Select choice > ");
            System.out.flush();
            String input = sc.nextLine().trim().toUpperCase();

            if(input.equals("M")){
                Screen.clear();
                return;
            } else {
                try {
                    choice = Integer.parseInt(input);
                    if (choice < 1 || choice > cropList.size()) {
                        Screen.yellowln("Invalid choice, please enter a number from 1 - " + cropList.size());
                        System.out.println();
                    } else {
                        validChoice = true;
                    }
                } catch (NumberFormatException e) {
                    Screen.yellowln("Invalid input");
                    System.out.println();
                }
            }
        }
        Crop crop = cropList.get(choice - 1);

        //prompts for username until the user enters something
        System.out.print("Send to > ");
        System.out.flush();
        String usernamesStr = sc.nextLine();

        while (usernamesStr.trim().length() == 0) {
            Screen.yellowln("Please enter the usernames of players you wish to send gifts to.");
            System.out.println();
            System.out.print("Send to > ");
            System.out.flush();
            usernamesStr = sc.nextLine();
        }

        String[] usernames = usernamesStr.split(",");
        if (usernames.length == 0) {
            Screen.clear();
            Screen.yellowln("Error! Look, you gotta enter <username>, <username>, ...");
            System.out.println();
            return;
        }

        Screen.clear();

        LinkedHashMap<String, Integer> status = giftCtrl.sendGifts(usernames, crop);

        Iterator<String> iter = status.keySet().iterator();
        while (iter.hasNext()) {
            String username = iter.next(); //using same variable as above, but its fine
            int statusCode = status.get(username);
            switch (statusCode) {
                case 0:
                    Screen.greenln("Gift sent to " + username + ".");
                    break;
                case 1:
                    Screen.yellowln("Sending to " + username + " failed: You have already sent to 5 friends today.");
                    break;
                case 2:
                    Screen.yellowln("Sending to " + username + " failed: You can't send a gift to yourself.");
                    break;
                case 3:
                    Screen.yellowln("Sending to " + username + " failed: Player does not exist.");
                    break;
                case 4:
                    Screen.yellowln("Sending to " + username + " failed: Player is not your friend.");
                    break;
                case 5:
                    Screen.yellowln("Sending to " + username + " failed: Already sent to player today");
                    break;
                case 6:
                    Screen.yellowln("Sending to " + username + " failed: Insufficient " + crop.getName() + " in inventory.");
                    break;
            }
        }
        System.out.println();

    }

    /**
     * Takes in this player's input
     */
    public void readOption(){
        Scanner sc = new Scanner(System.in);
        String input;
        do {
            display();
            System.out.println();
            System.out.print("[M]ain | [B]uy | [G]ift | Select choice > ");
            System.out.flush();
            input = sc.nextLine().trim().toUpperCase();

            switch(input){
                case "B":
                    Screen.clear();
                    processBuySeeds();
                    break;
                case "G":
                    Screen.clear();
                    processSendGift();
                    break;
                case "M":
                    Screen.clear();
                    break;
                default:
                    Screen.clear();
                    Screen.yellowln("Invalid input");
                    System.out.println();
                    break;
            }
        } while (!input.equals("M"));
    }
}
package game.ui;

import game.controller.*;
import game.dao.*;
import game.entity.*;
import utility.*;

import java.util.*;

    /**
     * A MainMenu allows this player to choose friendMenu or farmMenu or inventoryMenu
     */
public class MainMenu {
	private Player currentPlayer;

    private FriendMenu friendMenu;
	private FarmMenu farmMenu;
	private InventoryMenu inventoryMenu;

    /**
     * Initialises all the other controller and menu classes, after confirming the logged in player.
     * @param currentPlayer logged in player
     */
    public MainMenu(Player currentPlayer) {

        this.currentPlayer = currentPlayer;

        displayNotifications();

		//loading all the controllers based on the current player
        FriendController friendCtrl = new FriendController(currentPlayer);
        FarmController farmCtrl = new FarmController(currentPlayer);
        InventoryController inventoryCtrl = new InventoryController(currentPlayer);
        GiftController giftCtrl = new GiftController(currentPlayer);

		//initialising all the ui classes
		friendMenu = new FriendMenu(friendCtrl);
		farmMenu = new FarmMenu(farmCtrl);
		inventoryMenu = new InventoryMenu(inventoryCtrl, giftCtrl);

		//show opening animation
		Animator.opening();
	}

    /**
     * Displaying choices for this player
     */
	public void display() {
		System.out.println("== Farm City :: Main Menu ==");
		System.out.println("Welcome, " + currentPlayer.getName() + "!");
		System.out.println();
		System.out.println("1. My Friends");
		System.out.println("2. My Farm");
		System.out.println("3. My Inventory");
		System.out.println("4. Logout");
		System.out.println();
	}

    /**
     * Displaying friend request notification
     */
    public void displayNotifications() {
        FriendDAO friendDAO = FriendDAO.getInstance(currentPlayer);
        ArrayList<Request> incomingRequests = friendDAO.getIncomingRequests();
		if (incomingRequests.size() != 0) {
			Screen.cyanln("Notifications:");
		}
		for (Request r : incomingRequests) {
            Screen.cyanln(r.getSender().getName() + " has sent you a friend request.");
        }
		System.out.println();
	}

	public void processLogout() {
		Animator.loadingMessage("Logging you out");
	}

    /**
     * Taking in this player's input
     */
	public void readOption() {
        Scanner sc = new Scanner(System.in);
		String input;
		do {
			display();
            System.out.print("Enter your choice > ");
            System.out.flush();
            input = sc.nextLine().trim();

			switch (input) {
				case "1":
					Screen.clear();
					friendMenu.readOption();
					break;
				case "2":
					Screen.clear();
					farmMenu.readOption();
					break;
				case "3":
					Screen.clear();
					inventoryMenu.readOption();
					break;
				case "4":
					Screen.clear();
					processLogout();
					return;
				default:
                    Screen.clear();
					Screen.yellowln("Invalid input");
					break;
			}
		} while (!input.equals("4"));
	}
}
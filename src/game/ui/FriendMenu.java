package game.ui;

import java.util.*;
import game.controller.*;
import game.entity.*;
import utility.*;

    /**
     * A FriendMenu represents the menu which let this player manages his friends
     */
public class FriendMenu {
	private FriendController friendCtrl;

    /**
     * Creates a FarmMenu object with the specified FriendController
     * @param friendCtrl the friendCtrl of this farmMenu
     */
	public FriendMenu(FriendController friendCtrl) {
		this.friendCtrl = friendCtrl;
	}

    /**
     * Displays the player's list of friends and friend requests
     */
    public void display() {
        System.out.println("== Farm City :: My Friends ==");
        Player p = friendCtrl.getPlayer();
        System.out.println("Welcome, " + p.getName() + "!");
        System.out.println();

		int count = 1;

        ArrayList<Player> friends = friendCtrl.getFriends();
        System.out.println("My Friends:");
        for (Player friend : friends) {
            System.out.println(count++ + ". " + friend.getUsername());
        }
		if (friends.size() == 0) {
			System.out.println("You have no friends.");
		}
        System.out.println();

        ArrayList<Request> requests = friendCtrl.getIncomingRequests();
        System.out.println("My Requests:");
		if (requests.size() == 0) {
			System.out.println("You have no requests.");
		}
        for (Request r : requests) {
            System.out.println(count++ + ". " + r.getSender().getUsername());
        }
		System.out.println();
		
    }

    /**
     * Takes in the player's input
     */
    public void readOption() {
        Scanner sc = new Scanner(System.in);
		String input;
		do {
			display();
			ArrayList<Player> friends = friendCtrl.getFriends();
			ArrayList<Request> requests = friendCtrl.getIncomingRequests();
			
			System.out.print("[M]ain | [U]nfriend | re[Q]uest | [A]ccept | [R]eject > ");
			System.out.flush();
			input = sc.nextLine().toUpperCase();
            input = input.length() == 0 ? "err" : input;	//so that empty strings wont throw indexoutofbounds

			switch (input.charAt(0)){

				case 'A': //accept
					Screen.clear();
					try {
						int option = Integer.parseInt(input.substring(1));
						if (option <= friends.size() || option > (friends.size() + requests.size()) ){
							Screen.yellowln("Invalid option");
							System.out.println();
						} else {
							Request toAccept = requests.get(option - 1 - friends.size());
							friendCtrl.acceptRequest(toAccept);
							Screen.clear();
							Screen.greenln(toAccept.getSender().getUsername() + " is now your friend. Awesome!");
							System.out.println();
						}
					} catch (NumberFormatException e) {
						Screen.yellowln("Invalid input");
						System.out.println();
					}
					break;

				case 'R': //reject
                    Screen.clear();
                    try {
						int option = Integer.parseInt(input.substring(1));
						if (option <= friends.size() || option > friends.size() + requests.size() ){
							Screen.yellowln("Invalid option");
							System.out.println();
						} else {
							Request toReject = requests.get(option - 1 - friends.size());
							friendCtrl.rejectRequest(toReject);
							Screen.clear();
							Screen.greenln("You rejected request from " + toReject.getSender().getUsername() + ". Awwh.");
							System.out.println();
						}
					} catch (NumberFormatException e) {
						Screen.yellowln("Invalid input");
						System.out.println();
					}
					break;

				case 'Q': //send request
					System.out.print("Enter your friend's username > ");
					System.out.flush();
					String username = sc.nextLine();

					Screen.clear();
					int statusCode = friendCtrl.sendRequest(username);
					switch (statusCode) {
						case 0:
							Screen.greenln("A friend request is sent to " + username + ".");
							break;
						case 1:
							Screen.yellowln("Player does not exist!");
							break;
						case 2:
							Screen.yellowln(username + " is already a friend.");
							break;
						case 3:
							Screen.yellowln("You cannot friend yourself. (we're sorry if you're that lonely)");
							break;
						case 4:
							Screen.yellowln("Hang on.. There is already a request pending between you and " + username + ".");
							break;
					}
					System.out.println();

					break;

				case 'U': //unfriend
                    Screen.clear();
                    try {
						int option = Integer.parseInt(input.substring(1));
						if (option < 1 || option > friends.size()) {
							Screen.yellowln ("Invalid option");
							System.out.println();
						} else {
							Player toUnfriend = friends.get(option-1);
							friendCtrl.unfriendPlayer(toUnfriend);
							Screen.clear();
							Screen.greenln(toUnfriend.getUsername() + " is no longer your friend. Good Riddance!");
							System.out.println();
						}
					} catch (NumberFormatException e) {
						Screen.yellowln("Invalid input");
						System.out.println();
					}
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
			}
		} while (!input.equals("M"));
    }










}
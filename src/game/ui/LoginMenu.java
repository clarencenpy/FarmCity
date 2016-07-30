package game.ui;

import game.controller.*;
import game.entity.*;
import java.util.*;
import utility.*;

    /**
     * A LoginMenu represents the menu which let the player login
     */
public class LoginMenu {
	private LoginController loginCtrl;

    /**
     * Creates a new LoginMenu object with no parameters
     */
	public LoginMenu() {
		loginCtrl = new LoginController();
	}

    /**
     * Display options for this user
     */
	public void display() {
		System.out.println("== Farm City :: Welcome ==");
		System.out.println("1. Register");
		System.out.println("2. Login");
		System.out.println("3. Exit");
		System.out.println();
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
					processRegister();
					break;
				case "2":
					Screen.clear();
					processLogin();
					break;
				case "3":
                    Screen.clear();
					break;
				default:
					Screen.clear();
					Screen.yellowln("Invalid input");
					System.out.println();
					break;
			}
		} while (!input.equals("3"));
	}

    /**
     * Allowing this player to login
     */
	public void processLogin() {
		System.out.println("== Farm City :: Login ==");
		Scanner sc = new Scanner(System.in);
		System.out.print("Username > ");
		System.out.flush();
		String username = sc.nextLine();
        System.out.print("Password > ");
        System.out.flush();
        String password = sc.nextLine();

		//Masking password
//		Console c = System.console();
//		char[] pw = c.readPassword("Password > ");
//		String password = new String(pw);

		Player loggedInPlayer = loginCtrl.login(username, password);
		if (loggedInPlayer != null) {
			Screen.clear();
			MainMenu mainMenu = new MainMenu(loggedInPlayer);
			mainMenu.readOption();
		} else {
			Screen.clear();
			Screen.redln("Username or password is incorrect");
			System.out.println();
		}
	}

    /**
     * Allowing a new player to register
     */
	public void processRegister() {
		System.out.println("== Farm City :: Registration ==");
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter your username > ");
		System.out.flush();
		String username = sc.nextLine();

		//validate username
		while (!username.matches("[A-Za-z0-9]+")) {
			Screen.yellowln("Username must contain only alphanumeric characters. Try Again.");
			System.out.println();
			System.out.print("Enter your username > ");
			System.out.flush();
			username = sc.nextLine();
		}

		Animator.loadingMessage("Checking username");
		if (!loginCtrl.checkUsernameAvailability(username)) {
			Screen.clear();
			Screen.yellowln("Opps. Too slow, someone has already taken that username!");
			System.out.println();
			//terminate here and return to menu
			//might cause user frustration if we keep prompting until they find an unused username, without the choice to terminate
			return;
		}

		System.out.print("Enter your full name > ");
		System.out.flush();
		String name = sc.nextLine();

		//validate name
		while (name.length() == 0) {
			Screen.yellowln("Are you sure you wanna be nameless? Try again.");
			System.out.println();
			System.out.print("Enter your name > ");
			System.out.flush();
			name = sc.nextLine();
		}

        String password;
		boolean passwordMatches = false;
		do {
            System.out.print("Enter your password > ");
            System.out.flush();
            password = sc.nextLine();

			//validate password
			while (!password.matches("[\\S]+")) {
				Screen.yellowln("Password cannot be empty or contain spaces. Try Again.");
				System.out.println();
				System.out.print("Enter your password > ");
				System.out.flush();
				password = sc.nextLine();
			}

            System.out.print("Confirm password > ");
            System.out.flush();
            String confirmPassword = sc.nextLine();
			if (password.equals(confirmPassword)) {
				passwordMatches = true;
			} else {
				Screen.yellowln("Passwords do not match");
				System.out.println();
			}
		} while (!passwordMatches);

		loginCtrl.createNewPlayer(name, username, password);
		Screen.clear();
		Screen.greenln("Hi " + name + "! Your account is successfully created.");
		Screen.greenln("Please log in...");
		System.out.println();
	}
}
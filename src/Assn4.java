

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * The main class for Assignment 4.  
 * This class has no purpose except to contain the Main method
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class Assn4 {

	/**
	 * The main function for the Assignment 4 game.
	 * @param args The arguements which are passed to the program - ignored
	 */
	public static void main(String[] args) {
		// Cosmetic adjustments
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			// Non-essential - just continue after writing the error to the console
			JOptionPane.showMessageDialog(null, 
					"The game was unable to set the Cross Platform Look And Feel.\n"
					+ "The game may appear differently than it was intended",
					"Game Appearance Warning",
					JOptionPane.WARNING_MESSAGE);
			e.printStackTrace();
		}
		
		// Try to load the sprites
		try {
			SpriteManager.init();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, 
					"The game was unable to load its graphical assets. \n"
					+ "Make certain that you are running it in the same folder as the 'assets' folder.",
					"Asset Loading Error",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		
		// Create the main menu
		MainMenu mainMenu = new MainMenu();
		mainMenu.setVisible(true);
	}

}

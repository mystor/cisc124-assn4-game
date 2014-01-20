

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This class represents a GameFrame.  It is the window in which the game is held
 * The holds the various components which are essential for the game to operate.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class GameFrame extends JFrame {
	
	// Starting score values
	private static final int STARTING_LIFE = 10000;
	private static final int STARTING_SCORE = 0;

	// The spacing between the components in the score display
	private static final int SCORE_HORIZ_SPACING = 10;
	
	// The gamepanel
	private GamePanel gamePanel;
	
	// The main menu
	private MainMenu mainMenu;
	
	// The labels which display the current life and score
	private JLabel lifeDisplay;
	private JLabel scoreDisplay;

	// The score manager - manages the player's score
	private ScoreManager scoreManager;

	public GameFrame(MainMenu mainMenu, String nexusName, double spawnRate) {
		super("Defend " + nexusName + "!");
		// Make this frame dispose upon closing
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		// Save the main menu to return to it once game is over
		this.mainMenu = mainMenu;
		
		// Create the score manager
		scoreManager = new ScoreManager(STARTING_LIFE, STARTING_SCORE);
		
		// The panel which contains the play-pause button, etc.
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setBackground(Color.DARK_GRAY);
		
		final JButton playPauseBtn = new JButton("Pause");
		playPauseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// The pause button was clicked
				if (gamePanel.isRunning())
					gamePanel.pause();
				else
					gamePanel.play();
				
				// Change the text on the button
				playPauseBtn.setText(gamePanel.isRunning() ? "Pause" : "Play");
			}
		});
		headerPanel.add(playPauseBtn, BorderLayout.WEST);
		
		// The stop/quit button
		JButton quitButton = new JButton("Stop");
		quitButton.setBackground(Color.RED);
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Temporarially pause the game while the prompt is being displayed
				boolean wasRunning = gamePanel.isRunning();
				
				if (wasRunning)
					gamePanel.pause();
				
				// The quit button was clicked - confirm
				int selection = JOptionPane.showConfirmDialog(null, 
						"Are you sure you want to Stop?", 
						"Stop COnfirmation", 
						JOptionPane.YES_NO_OPTION);
				
				if (selection == JOptionPane.YES_OPTION) {
					// Dispose of the frame and return to the main menu
					GameFrame.this.setVisible(false);
					GameFrame.this.dispose();
				} else if (wasRunning)
					gamePanel.play();
			}
		});
		headerPanel.add(quitButton, BorderLayout.EAST);
		
		// Create the panel for scores
		JPanel scorePanel = new JPanel();
		scorePanel.setBackground(Color.DARK_GRAY);
		
		// Set the layout
		GridLayout scorePanelLayout = new GridLayout(0,4);
		scorePanelLayout.setHgap(SCORE_HORIZ_SPACING);
		scorePanel.setLayout(scorePanelLayout);
		
		// The life score
		JLabel lifeLabel = new JLabel(nexusName + " life:");
		lifeLabel.setForeground(Color.WHITE);
		lifeLabel.setHorizontalAlignment(JLabel.RIGHT);
		scorePanel.add(lifeLabel);
		
		lifeDisplay = new JLabel("10000");
		lifeDisplay.setForeground(Color.GREEN);
		lifeDisplay.setHorizontalAlignment(JLabel.LEFT);
		scorePanel.add(lifeDisplay);
		
		// The Player Score
		JLabel scoreLabel = new JLabel("Score:");
		scoreLabel.setForeground(Color.WHITE);
		scoreLabel.setHorizontalAlignment(JLabel.RIGHT);
		scorePanel.add(scoreLabel);
		
		scoreDisplay = new JLabel("0");
		scoreDisplay.setForeground(Color.CYAN);
		scoreDisplay.setHorizontalAlignment(JLabel.LEFT);
		scorePanel.add(scoreDisplay);
		
		headerPanel.add(scorePanel, BorderLayout.CENTER);
		
		add(headerPanel, BorderLayout.NORTH);
		
		// The central game panel
		gamePanel = new GamePanel(spawnRate, scoreManager);
		add(gamePanel, BorderLayout.CENTER);
		
		// Pack the frame
		pack();
		setLocationRelativeTo(null); // Center in screen
	}
	
	/**
	 * Gets the GameFrame's scoreManager
	 * @return The GameFrame's scoreManager
	 */
	public ScoreManager getScoreManager() {
		return scoreManager;
	}
	
	/**
	 * Destroys the window and returns the user to the main menu
	 */
	@Override
	public void dispose() {
		super.dispose();
		
		gamePanel.pause(); // Make sure that the timer stops running
		
		mainMenu.setVisible(true);
	}
	
	/**
	 * The ScoreManager class maintains the current score of the game.
	 * The ScoreManager also checks for win conditions
	 * 
	 * @author Michael Layzell
	 *
	 */
	public class ScoreManager {
		private double health;
		private double score;

		/**
		 * Create a ScoreManager with initial values for health and score
		 * @param initialHealth
		 * @param initialScore
		 */
		public ScoreManager(int initialHealth, int initialScore) {
			health = initialHealth;
			score = initialScore;
		}
		
		/**
		 * Does damage to the nexus
		 * @param damage The damage which has been dealt to the nexus
		 */
		public void doDamage(double damage) {
			health -= damage;
			updateDisplay();
		}
		
		/**
		 * Adds score to the player's score
		 * @param points The number of points to add to the player's score
		 */
		public void gainScore(double points) {
			score += points;
			updateDisplay();
		}
		
		/**
		 * Updates the values displayed in at the top of the game display.
		 * Also checks for win conditions.
		 */
		private void updateDisplay() {
			if (health < 0)
				health = 0;
			
			lifeDisplay.setText(Long.toString(Math.round(health)));
			scoreDisplay.setText(Long.toString(Math.round(score)));
			
			// Color the life display based on current life total
			if (health < 2000)
				lifeDisplay.setForeground(Color.RED);
			else if (health < 5000)
				lifeDisplay.setForeground(Color.ORANGE);
			else if (health < 7000)
				lifeDisplay.setForeground(Color.YELLOW);
			else
				lifeDisplay.setForeground(Color.GREEN);
			
			if (health <= 0) {
				// The player has died
				gamePanel.pause();
				
				// Notify player that game is over
				JOptionPane.showMessageDialog(null, 
						"The game is over. \n"
						+ "Your nexus has been destroyed. \n"
						+ "You managed to collect " + Math.round(score) + " points in the defense of your nexus.",
						"Game Over", JOptionPane.INFORMATION_MESSAGE);
				
				// Destroy the game window and return to the main menu!
				GameFrame.this.dispose();
			}
		}
	}
}

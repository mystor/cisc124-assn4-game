

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This class represents the main menu of the game.
 * It provides access into the different game modes as well as allows the user to quit.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class MainMenu extends JFrame {
	
	private static final int INSANE_SPAWN_RATE= 250;
	private static final int HARD_SPAWN_RATE = 500;
	private static final int MEDIUM_SPAWN_RATE = 1000;
	private static final int EASY_SPAWN_RATE = 2000;

	private static final int DEFAULT_VERTICAL_PADDING = 20;

	// The background color
	private static final Color bgColor = new Color(155, 62, 151);
	
	// The name of the nexus
	private JTextField nexusName;
	
	public MainMenu() {
		super("Nexus Defender - Main Menu");
	
		// Configure the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBackground(bgColor);
		
		// Create the image header for the MainMenu
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new FlowLayout());
		headerPanel.setBackground(Color.BLACK);
		
		JLabel nexusIcon = new JLabel("");
		nexusIcon.setIcon(new ImageIcon(SpriteManager.getSprite(SpriteManager.NEXUS_PORTRAIT)));
		headerPanel.add(nexusIcon);
		
		add(headerPanel, BorderLayout.NORTH);
		
		// Create the panel which will contain the central elements and absorb any vertical stretch
		JPanel stretchPanel = new JPanel();
		stretchPanel.setLayout(new BorderLayout());
		
		// Create the panel containing the controls
		JPanel buttonsPanel = new JPanel();
		GridLayout buttonsLayout = new GridLayout(0,1);
		buttonsLayout.setVgap(5);
		buttonsPanel.setLayout(buttonsLayout);
		buttonsPanel.setBackground(Color.BLACK);
		stretchPanel.add(buttonsPanel, BorderLayout.NORTH);
		
		// Create the text field which contains the nexus' name
		nexusName = new JTextField("Nexus");
		nexusName.setBackground(Color.DARK_GRAY);
		nexusName.setForeground(Color.WHITE);
		nexusName.setBorder(null);
		nexusName.setHorizontalAlignment(JTextField.CENTER);
		buttonsPanel.add(nexusName);
		
		// Create the play game label
		JLabel play = new JLabel("Play Game!");
		play.setFont(new Font(Font.MONOSPACED, Font.BOLD, 18));
		play.setHorizontalAlignment(JLabel.CENTER);
		play.setForeground(Color.WHITE);
		buttonsPanel.add(play);
		
		// Create the play game button
		JButton playE = new JButton("EASY (" + EASY_SPAWN_RATE + " ms)");
		playE.addActionListener(new PlayGameListener(EASY_SPAWN_RATE));
		playE.setBackground(Color.GREEN);
		buttonsPanel.add(playE);
		
		JButton playM = new JButton("MEDIUM (" + MEDIUM_SPAWN_RATE + " ms)");
		playM.addActionListener(new PlayGameListener(MEDIUM_SPAWN_RATE));
		playM.setBackground(Color.CYAN);
		buttonsPanel.add(playM);
		
		JButton playH = new JButton("HARD (" + HARD_SPAWN_RATE + " ms)");
		playH.addActionListener(new PlayGameListener(HARD_SPAWN_RATE));
		playH.setBackground(Color.ORANGE);
		buttonsPanel.add(playH);
		
		JButton playI = new JButton("INSANE (" + INSANE_SPAWN_RATE + " ms)");
		playI.addActionListener(new PlayGameListener(INSANE_SPAWN_RATE));
		playI.setBackground(Color.RED);
		buttonsPanel.add(playI);
		
		// Create the padding below the play game buttons
		JPanel basePadding = new JPanel();
		basePadding.setBackground(Color.BLACK);
		basePadding.setPreferredSize(new Dimension(DEFAULT_VERTICAL_PADDING, DEFAULT_VERTICAL_PADDING));
		stretchPanel.add(basePadding, BorderLayout.CENTER);
		
		add(stretchPanel, BorderLayout.CENTER);
		
		// Create the quit button
		JButton quit = new JButton("Quit Game");
		quit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Quit the program
				System.exit(0);
			}
		});
		quit.setBackground(Color.LIGHT_GRAY);
		add(quit, BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(null); // Center in screen
	}
	
	/**
	 * A listener to be attached to a play game button.
	 * Causes the game to be started with a specified difficulty
	 * 
	 * @author Michael Layzell
	 *
	 */
	private class PlayGameListener implements ActionListener {
		
		private double spawnRate;
		
		/**
		 * Create a PlayGameListener
		 * @param spawnRate The spawnRate which the game will be run at when the button is pressed.
		 */
		public PlayGameListener(double spawnRate) {
			this.spawnRate = spawnRate;
		}
		
		/*
		 * (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			// Hide this window
			setVisible(false);
			
			// Create the game's window and display it
			new GameFrame(MainMenu.this, nexusName.getText(), spawnRate)
					.setVisible(true);
		}
		
	}
}

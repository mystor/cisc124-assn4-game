

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This class represents the central panel in the game in which the game's elements are drawn.
 * The GamePanel manages almost all aspects of the game, including input, collisions, movement, and scoring.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class GamePanel extends JPanel {
	
	// The rate at which the timer fires
	private static final int TIMER_RATE = 20;
	
	// The maximum number of times which the spawner will attempt to spawn a ship before giving up
	private static final int MAX_SPAWN_TRIALS = 10;
	
	// The damage and points which each ship are worth
	private static final double PER_MILI_DAMAGE = 0.05;
	private static final double PER_SHIP_POINTS = 50;
	
	// A Random object used for random calculations
	private static final Random random = new Random();
	
	// The background color
	private static final Color bgColor = new Color(59, 24, 80);

	// The timer which ticks every 10ms
	private Timer timer;
	
	// The list of ships
	private List<Ship> ships = new ArrayList<Ship>();
	
	// Time when the previous update occured
	private long lastTime;
	
	// The spawn rate
	private double spawnRate = 1000;
	private long spawnTime;
	
	// The score manager
	private GameFrame.ScoreManager scoreManager;
	
	/**
	 * Creates a GamePanel - the component of a GameFrame which contains the gameplay
	 * @param scoreManager The parent GameFrame's scoreManager
	 */
	public GamePanel(double spawnRate, GameFrame.ScoreManager sManager) {
		super();
		setPreferredSize(new Dimension(800, 600)); // Default size for the frame
		setBackground(bgColor);
		
		// Save the spawn rate and score manager
		this.spawnRate = spawnRate;
		scoreManager = sManager;
		
		// Start with 4 drones spawned (in the first few ticks)
		spawnTime = Math.round(4 * spawnRate);
		
		// Reset the effect manager
		EffectManager.resetEffectManager();
		
		// Create the timer which will be run every 10ms.
		timer = new Timer(TIMER_RATE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// This is run every approx. 10 ms.
				// Calls the tick(long delta) method of the enclosing class
				// Then, commands the screen to refresh
				long delta = System.currentTimeMillis() - lastTime;
				lastTime = System.currentTimeMillis();
				
				tick(delta);
				repaint();
			}
		});
		
		// Set the lastTime variable
		lastTime = System.currentTimeMillis();
		
		// Create the mouse listener
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				// Ignore click if the game is paused
				if (!isRunning())
					return;
				
				// Attempt to destroy the ship
				Point clickPos = e.getPoint();
				Ship theShip = null;
				for (Ship ship : ships) {
					if (ship.pointOnShip(clickPos.x, clickPos.y)) {
						theShip = ship;
						break;
					}
				}
				
				if (theShip != null) {
					// The player is clicking on theShip
					if (theShip.isMoving()) {
						// Reward the player with points
						int pointsGain = (int) (PER_SHIP_POINTS * (1 + theShip.getNumLinks()));
						scoreManager.gainScore(pointsGain);
						
						// Destroy it if it is moving (not stationary)
						theShip.dispose();
						
						// Display the points display
						EffectManager.getEffectManager().addEffect(
								new PointsDisplay(theShip.getX(), theShip.getY(), 
										Integer.toString(pointsGain), Color.CYAN));
						
						// Remove the ship from the list
						ships.remove(theShip);
					}
				}
			}
		});
		
		
		play(); // Start the game running
	}
	
	/**
	 * @return Whether or not the game is currently running
	 */
	public boolean isRunning() {
		return timer.isRunning();
	}
	
	/**
	 * Pauses the game - no effect if game is already paused
	 */
	public void pause() {
		timer.stop();
	}
	
	/**
	 * Resumes the game - no effect if the game is already running
	 */
	public void play() {
		timer.start();
		lastTime = System.currentTimeMillis();  // Don't include time when paused in delta calculations
	}
	
	/**
	 * Spawn a ship at a random point on the screen
	 */
	private void spawnShip() {
		for (int trial = 0; trial < MAX_SPAWN_TRIALS; trial++) {
			int x;
			int y;
			try {
				x = random.nextInt(getWidth() - Ship.BASE_HALF_DIM) + Ship.BASE_HALF_DIM;
				y = random.nextInt(getHeight() - Ship.BASE_HALF_DIM) + Ship.BASE_HALF_DIM;
			} catch (IllegalArgumentException e) {
				return;
			}
			
			int dir = random.nextInt(4); // Random int in [0, 4) = [0, 3]
			int type = random.nextInt(2); // Random int in [0, 2) = [0, 1]
			
			// Create the ship
			Ship ship = new Ship(x, y, dir, type, this);
			
			boolean badShip = false;
			
			for (Ship other : ships) {
				if (Ship.areColliding(ship, other)) {
					badShip = true;
					break; // The ship is colliding with another ship, better try again!
				}
			}
			
			if (badShip)
				continue;
			
			// Not colliding with any of the ships, return
			ships.add(ship);
			return;
		}
		
		System.out.println("WARNING: MAX_SPAWN_TRIALS Exceeded!");
	}

	/**
	 * Called approximately every 10ms by the timer 'timer'
	 * 
	 * @param delta The time in millis since the last update
	 */
	public void tick(long delta) {
		// Increment spawntime
		spawnTime += delta;
		
		if (spawnTime >= spawnRate) {
			// Spawn a ship
			spawnShip();
			spawnTime -= spawnRate;
		}
		
		// Animate the effects
		EffectManager.getEffectManager().tick(delta);
		
		// Check for collisions between the ships
		for (int i = 0; i < ships.size(); i++) {
			Ship a = ships.get(i);
			
			// Only try ships with indexes greater than i
			for (int j = i + 1; j < ships.size(); j++) {
				Ship b = ships.get(j);
				
				if (Ship.areColliding(a, b)) {
					// The ships are colliding, link them and bounce them off of eachother
					a.bounce();
					b.bounce();
					
					// Move the ships apart from eachother
					a.tick(3 * 20 * Ship.HALF_DIM_SCALING);
					b.tick(3 * 20 * Ship.HALF_DIM_SCALING);
					
					// Finally, link the two together
					a.link(b);
				}
			}
		}
		
		// Update all of the ships
		for (Ship ship : ships) {
			ship.tick(delta);
		}
		
		// Do damage
		double damage = 0;
		for (Ship ship : ships) {
			// Damage scales exponentially
			damage += Math.pow(2, ship.getNumLinks()) * PER_MILI_DAMAGE;
		}
		scoreManager.doDamage(damage);
	}
	
	/**
	 * Paints the gamePanel
	 * @param g The graphics context
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Graphics2D g2 = (Graphics2D) g;
		
		// Draw the background image
		g2.drawImage(SpriteManager.getSprite(SpriteManager.NEXUS_BG), (getWidth() - 800)/2, (getHeight() - 600)/2, 800, 600, null);
		
		// Draw the links
		for (Ship ship : ships) {
			ship.paint_links(g2);
		}
		
		// Draw the effects
		EffectManager.getEffectManager().paintEffects(g2);
		
		// Draw the ships themselves
		for (Ship ship : ships) {
			ship.paint(g2);
		}
	}
}

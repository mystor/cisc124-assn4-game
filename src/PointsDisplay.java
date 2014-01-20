import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;



/**
 * The PointsDisplay class represents a points display which appears when ships are distroyed
 * It floats upwards and disappears after a second.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class PointsDisplay implements EffectManager.Effect {

	// The speed at which the points float upwards
	private static final double FLOAT_SPEED = -0.04;
	
	// The total life of the points display
	private static final int TOTAL_LIFE = 1000;
	
	// The display features of the points display
	private String text;
	private Color color;
	
	// The x and y coordinates of the points display
	private double x;
	private double y;
	
	// The lifespan of the points display
	private long lifespan = 0;
	private boolean done = false;
	
	/**
	 * Creates a points display which will float upwards on the display and then disappear
	 * @param x The starting x-coordinate of the points display
	 * @param y The starting y-coordinate of the points display
	 * @param text The text of the points display
	 * @param color The color of the points display
	 */
	public PointsDisplay(double x, double y, String text, Color color) {
		this.text = text;
		this.color = color;
		
		this.x = x;
		this.y = y;
	}
	
	/*
	 * (non-Javadoc)
	 * @see EffectManager.Effect#tick(long)
	 */
	@Override
	public void tick(long delta) {
		y += FLOAT_SPEED * delta;
		lifespan += delta;
		
		if (lifespan > TOTAL_LIFE)
			done = true;
	}

	/*
	 * (non-Javadoc)
	 * @see EffectManager.Effect#paint(java.awt.Graphics2D)
	 */
	@Override
	public void paint(Graphics2D g) {
		// Set the font correctly
		g.setColor(color);
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
		FontMetrics fontMetrics = g.getFontMetrics();
		
		// Get the width of the string
		int strWidth = fontMetrics.stringWidth(text);
		
		// Draw the string
		g.drawString(text, (int) x - strWidth / 2, (int) y);
	}

	/*
	 * (non-Javadoc)
	 * @see EffectManager.Effect#done()
	 */
	@Override
	public boolean done() {
		return done;
	}
}



import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * This class represents an explosion.  It is displayed on the screen when a ship is destroyed.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class Explosion implements EffectManager.Effect {
	
	// The time in miliseconds which the explosion takes
	private static final int EXPLOSION_TIME = 750;

	// The number of frames in the x and y directions
	private static final int EXPLOSION_XFRAMES = 4;
	private static final int EXPLOSION_YFRAMES = 4;

	// Width and Height of the frame
	private static final int WIDTH = 64;
	private static final int HEIGHT = 64;

	// The amount of time, in miliseconds, which the explosion has been alive
	private long lifespan = 0;
	
	// The position of the explosion
	private int x, y;
	
	// Whether or not the explosion is done
	private boolean isDone = false;
	
	// The current image to draw
	private BufferedImage visual;
	
	/**
	 * Creates an explosion at the defined point
	 * @param x The X coordinate of the explosion
	 * @param y The Y coordinate of the explosion
	 */
	public Explosion(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * @see EffectManager.Effect#tick(long)
	 */
	@Override
	public void tick(long delta) {
		lifespan += delta;
		
		// Get the sprite sheet
		BufferedImage spriteSheet = SpriteManager.getSprite(SpriteManager.EXPLOSION);
		
		int frame = (int) lifespan / (EXPLOSION_TIME / (EXPLOSION_XFRAMES * EXPLOSION_YFRAMES));
		
		int frameX = frame % EXPLOSION_XFRAMES;
		int frameY = frame / EXPLOSION_XFRAMES;
		
		if (frameY >= EXPLOSION_YFRAMES) {
			// The explosion is done
			isDone = true;
			return;
		}
				
		visual = spriteSheet.getSubimage(frameX * WIDTH, frameY * HEIGHT, WIDTH, HEIGHT);
	}

	/*
	 * (non-Javadoc)
	 * @see EffectManager.Effect#paint(java.awt.Graphics2D)
	 */
	@Override
	public void paint(Graphics2D g) {
		// Draw the current visual image
		g.drawImage(visual, x - WIDTH/2, y - HEIGHT/2, WIDTH, HEIGHT, null);
	}

	/*
	 * (non-Javadoc)
	 * @see EffectManager.Effect#done()
	 */
	@Override
	public boolean done() {
		return isDone;
	}
	
}

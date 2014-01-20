

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class loads and provides access to the various sprites which are used throughout the game.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class SpriteManager {
	
	// These are the integer constants which define the images
	public static final int DRONE_NORMAL = 0;
	public static final int DRONE_FAST = 1;
	public static final int DRONE_NORMAL_STILL = 2;
	public static final int DRONE_FAST_STILL = 3;
	public static final int NEXUS_BG = 4;
	public static final int NEXUS_PORTRAIT = 5;
	public static final int EXPLOSION = 6;
	
	// Storage location for the loaded sprites
	private static BufferedImage[] sprites;
	
	/**
	 * Loads all of the sprites into memory and saves them
	 * @throws IOException 
	 */
	public static void init() throws IOException {
		sprites = new BufferedImage[7];
		
		sprites[DRONE_NORMAL] = ImageIO.read(new File("assets/drone_normal.png"));
		sprites[DRONE_FAST] = ImageIO.read(new File("assets/drone_fast.png"));
		sprites[DRONE_NORMAL_STILL] = ImageIO.read(new File("assets/drone_normal_still.png"));
		sprites[DRONE_FAST_STILL] = ImageIO.read(new File("assets/drone_fast_still.png")); 
		sprites[NEXUS_BG] = ImageIO.read(new File("assets/nexus_background.png"));
		sprites[NEXUS_PORTRAIT] = ImageIO.read(new File("assets/nexus_portrait.png"));
		sprites[EXPLOSION] = ImageIO.read(new File("assets/explosion.png"));
	}
	
	/**
	 * Gets the sprite represented by id
	 * @param id The sprite to get
	 * @return The BufferedImage for the Sprite
	 */
	public static BufferedImage getSprite(int id) {
		if (sprites == null)
			throw new Error("Sprites not yet loaded");
		
		return sprites[id];
	}

}

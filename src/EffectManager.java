

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * This class manages effect objects for the game. 
 * It ensures that effects are updated and cleaned up when they are completed.
 * 
 * @author Michael Layzell
 * Made for CISC 124, Winter 2013
 */
public class EffectManager {

	private List<Effect> effects = new ArrayList<Effect>();
	
	private static EffectManager eManager;
	
	/**
	 * Gets the static EffectManager
	 * @return The static EffectManager, or a new EffectManager if there is currently no static EffectManager
	 */
	public static EffectManager getEffectManager() {
		// Creates the static eManager if it isn't set yet
		if (eManager == null) 
			eManager = new EffectManager();
		
		// Returns the eManager
		return eManager;
	}
	
	/**
	 * Resets the static EffectManager
	 * ensures that effects do not carry over between game sessions
	 */
	public static void resetEffectManager() {
		eManager = null;
	}
	
	/**
	 * Adds the effect e to the screen
	 * @param e The effect to add
	 */
	public void addEffect(Effect e) {
		effects.add(e);
	}
	
	/**
	 * Updates all of the effects managed by the EffectManager
	 * @param delta The time since the last tick
	 */
	public void tick(long delta) {
		for (Effect e : effects)
			e.tick(delta);
		
		// Check to see if any of the effects are done
		Iterator<Effect> iterator = effects.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().done())
				iterator.remove();
		}
	}
	
	/**
	 * Draws all of the effects managaed by the EffectManager to the screen
	 * @param g The graphics context to draw to
	 */
	public void paintEffects(Graphics2D g) {
		for (Effect e : effects)
			e.paint(g);
	}

	/**
	 * The interface for an effect to use.
	 * Effects will be updated and drawn by an EffectManager.
	 * Effects will be destroyed once done() returns true.
	 * 
	 * @author Michael Layzell
	 *
	 */
	public static interface Effect {
		
		/**
		 * Updates the effect
		 * @param delta The time since the last tick
		 */
		public void tick(long delta);
		
		/**
		 * Draws the effect to the screen
		 * @param g The graphics context to draw to.
		 */
		public void paint(Graphics2D g);
		
		/**
		 * Checks whether the effect is complete
		 * @return Whether the effect is complete
		 */
		public boolean done();
		
	}
}

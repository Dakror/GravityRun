package de.dakror.gravityrun.ui;

import java.awt.Graphics2D;

/**
 * Base interface for anything to be drawn
 * 
 * @author Maximilian Stark | Dakror
 */
public interface Drawable {
	public void draw(Graphics2D g);
	
	/**
	 * Update pseodo-thread. Just to split logic from drawing
	 * 
	 * @param deltaTime the time in seconds since the last frame
	 */
	public void update(float deltaTime);
}

package de.dakror.gravityrun.layer;

import de.dakror.gravityrun.ui.Drawable;

/**
 * @author Maximilian Stark | Dakror
 */
public interface Layer extends Drawable {
	/**
	 * Called when this layer is being added
	 */
	public void enter();
	
	/**
	 * Called when this layer is being removed
	 */
	public void exit();
}

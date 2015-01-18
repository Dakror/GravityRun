package de.dakror.gravityrun.layer;

import java.awt.Graphics2D;
import java.util.ArrayList;

import de.dakror.gravityrun.ui.Drawable;

/**
 * Global manager for all layers.
 * 
 * @author Maximilian Stark | Dakror
 */
public class LayerManager implements Drawable {
	/**
	 * Global and only instance
	 */
	public static final LayerManager instance = new LayerManager();
	
	ArrayList<Layer> layers = new ArrayList<>();
	
	private LayerManager() {}
	
	/**
	 * Adds a new layer at the end of the list = not drawn
	 */
	public void addLayer(Layer l) {
		l.enter();
		layers.add(l);
	}
	
	/**
	 * Adds a new layer at the beginning of the list = drawn
	 */
	public void putLayer(Layer l) {
		l.enter();
		if (layers.size() == 0) layers.add(l);
		else layers.add(0, l);
	}
	
	public void removeLayer(Layer l) {
		l.exit();
		layers.remove(l);
	}
	
	public void removeLayer(int index) {
		removeLayer(layers.get(index));
	}
	
	/**
	 * Removes first layer from the list
	 */
	public void popLayer() {
		if (layers.size() > 0) removeLayer(0);
	}
	
	public Layer first() {
		if (layers.size() == 0) return null;
		return layers.get(0);
	}
	
	@Override
	public void draw(Graphics2D g) {
		layers.forEach(l -> l.draw(g)); // sexy Java 8 for loop
	}
	
	@Override
	public void update(float deltaTime) {
		layers.forEach(l -> l.update(deltaTime));
	}
}

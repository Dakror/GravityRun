package de.dakror.gravityrun.game;

import java.awt.Graphics2D;

import de.dakror.gravityrun.GravityRun;
import de.dakror.gravityrun.game.tile.Block;
import de.dakror.gravityrun.layer.Layer;

/**
 * Central class for the actual game.
 * 
 * @author Maximilian Stark | Dakror
 */
public class Game implements Layer {
	Block block;
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(block.getBatch(), (int) block.getX(), (int) block.getY(), null);
	}
	
	@Override
	public void update(float deltaTime) {}
	
	@Override
	public void enter() {
		block.load(GravityRun.getImage("flat.png"));
	}
	
	@Override
	public void exit() {}
}

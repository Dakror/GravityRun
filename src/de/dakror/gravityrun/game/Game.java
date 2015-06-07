/*******************************************************************************
 * Copyright 2015 Maximilian Stark | Dakror <mail@dakror.de>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/


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
		block = new Block();
		block.load(GravityRun.getImage("tiles/flat.png"));
	}
	
	@Override
	public void exit() {}
}

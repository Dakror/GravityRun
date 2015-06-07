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


package de.dakror.gravityrun;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import de.dakror.gravityrun.game.Game;
import de.dakror.gravityrun.layer.LayerManager;
import de.dakror.gravityrun.ui.Drawable;

/**
 * @author Maximilian Stark | Dakror
 */
public class GravityRun extends JFrame implements Drawable {
	private static final long serialVersionUID = 1L;
	public static GravityRun instance;
	
	static HashMap<String, BufferedImage> imageCache = new HashMap<>();
	
	public GravityRun() {
		super("GravityRun");
		instance = this;
		
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
		setBackground(Color.black);
		
		createBufferStrategy(2);
		
		LayerManager.instance.addLayer(new Game());
		mainLoop();
	}
	
	public void mainLoop() {
		Graphics2D g = null;
		long last = System.currentTimeMillis();
		
		while (true) {
			BufferStrategy bs = getBufferStrategy();
			g = (Graphics2D) bs.getDrawGraphics();
			g.translate(getInsets().left, getInsets().top);
			
			update((System.currentTimeMillis() - last) / 1_000f);
			
			g.clearRect(0, 0, innerWidth(), innerHeight());
			draw(g);
			
			g.dispose();
			
			if (!bs.contentsLost()) bs.show();
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		LayerManager.instance.draw(g);
	}
	
	@Override
	public void update(float deltaTime) {
		LayerManager.instance.update(deltaTime);
	}
	
	public int innerWidth() {
		return getWidth() - (getInsets().left + getInsets().right);
	}
	
	public int innerHeight() {
		return getHeight() - (getInsets().top + getInsets().bottom);
	}
	
	public BufferedImage loadImage(String p) {
		try {
			BufferedImage i = ImageIO.read(GravityRun.class.getResource((p.startsWith("/") ? "" : "/assets/") + p));
			
			return i;
		} catch (Exception e) {
			return null;
		}
	}
	
	// -- statics -- //
	
	public static int width() {
		return instance.innerWidth();
	}
	
	public static int height() {
		return instance.innerHeight();
	}
	
	public static BufferedImage getImage(String p) {
		if (imageCache.containsKey(p)) return imageCache.get(p);
		BufferedImage img = instance.loadImage(p);
		imageCache.put(p, img);
		return img;
	}
	
	public static void main(String[] args) {
		new GravityRun();
	}
}

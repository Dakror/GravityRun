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
 

package de.dakror.gravityrun.util;

import java.awt.Color;
import java.util.ArrayList;

/**
 * The default palette for the game to index colors, rather store RGB values every time.
 * 
 * @author Maximilian Stark | Dakror
 */
public class Palette {
	/**
	 * In which steps should the colors be rasterized in?<br>
	 * total result of colors = STEP^3<br>
	 * STEP = 8 => 32768 Colors => fits into a short
	 */
	public static final int STEP = 8;
	
	/**
	 * This color serves as fake-transparency. That way we don't have to mess with alpha values anywhere.<br>
	 * Fully transparent or opaque.
	 */
	public static final Color TRANSPARENT = new Color(255, 0, 255);
	public static final Color TRANSPARENT_RGBA = new Color(0, 0, 0, 0);
	
	/**
	 * Global only instance
	 */
	public static final Palette instance = new Palette();
	
	ArrayList<Color> colors;
	
	protected Palette() {
		colors = new ArrayList<Color>();
		
		for (int i = 0; i <= 256; i += STEP)
			for (int j = 0; j <= 256; j += STEP)
				for (int k = 0; k <= 256; k += STEP)
					colors.add(new Color(Math.max(0, i - 1), Math.max(0, j - 1), Math.max(0, k - 1))); // colors go from 0 - 255, so meh :/
	}
	
	public int size() {
		return colors.size();
	}
	
	public int indexOf(Color c) {
		return colors.indexOf(c);
	}
	
	public boolean contains(Color c) {
		return colors.contains(c);
	}
	
	public int indexOf(int rgb) {
		return colors.indexOf(new Color(rgb));
	}
	
	public Color get(int index) {
		return colors.get(index);
	}
	
	public Color getWithAlpha(int index) {
		if (get(index).equals(TRANSPARENT)) return TRANSPARENT_RGBA;
		else return get(index);
	}
	
	public int getRGB(int index) {
		return get(index).getRGB();
	}
}

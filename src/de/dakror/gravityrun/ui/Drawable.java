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

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

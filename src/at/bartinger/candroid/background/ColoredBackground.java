/**
 *  Copyright 2010 Bartl Dominic

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package at.bartinger.candroid.background;

import android.graphics.Canvas;

/**
 * Draws a color in the Background
 * @author dominic
 *
 */

public class ColoredBackground extends Background{
	
	private int color;
	
	/**
	 * @param color The color witch should get drawn
	 */
	public ColoredBackground(int color) {
			this.color = color;
	}
	
	/**
	 * Changes the color witch should get drawn
	 * @param color
	 */
	public void changeColor(int color){
		this.color = color;		
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawColor(color);
		super.draw(canvas);
	}

}

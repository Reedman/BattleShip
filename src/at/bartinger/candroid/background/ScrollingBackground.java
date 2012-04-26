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


import android.graphics.Bitmap;
import android.graphics.Canvas;
import at.bartinger.candroid.texture.Texture;

public class ScrollingBackground extends Background{
	
	private Bitmap mBackground;
	private double offsetX;
	private double offsetY;

	private double autooffsetX;
	private double autooffsetY;
	
	public ScrollingBackground(Texture tex) {
		mBackground = tex.tex;
		height = tex.tex.getHeight();
		width = tex.tex.getWidth();
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBackground, (int)x, (int)y, null);
		super.draw(canvas);
	}
	
	@Override
	public void update() {
		x+=offsetX;
		y+=offsetY;
		
		x+=autooffsetX;
		y+=autooffsetY;
		
		offsetX=0;
		offsetY=0;
	}
	
	public void setOffset(double offsetX, double offsetY){
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	
	public void setSpeed(double autooffsetX, double autooffsetY){
		this.autooffsetX = autooffsetX;
		this.autooffsetY = autooffsetY;
	}
	
	
}

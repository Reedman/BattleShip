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

package at.bartinger.candroid.renderer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import at.bartinger.candroid.background.Background;
import at.bartinger.candroid.background.ColoredBackground;
import at.bartinger.candroid.texture.Texture;


public class CellRenderer implements Renderer {

	private Bitmap cells;
	private Background background;
	public float x;
	public float y;
	private float scrollX;
	private float scrollY;
	
	public int mapHeight;
	public int mapWidth;
	
	
	public CellRenderer() {
		background = new ColoredBackground(Color.BLACK);
	}
	
	public void setMap(Texture map){
		cells = map.tex;
		mapHeight = cells.getHeight();
		mapWidth = cells.getWidth();
	}

	public void drawFrame(Canvas canvas) {
		update();
		background.update();
		background.draw(canvas);
		canvas.drawBitmap(cells,x,y,null);
	}
	
	public void setBackground(Background bg){
		background = bg;
	}
	
	public void setMapOffset(float ox, float oy){
		scrollX = ox;
		scrollY = oy;
	}
	
	public void setMapPosition(float px, float py){
		x = px;
		y = py;
	}
	
	private void update(){
		x+=scrollX;
		y+=scrollY;
		scrollX = 0;
		scrollY = 0;
	}

}

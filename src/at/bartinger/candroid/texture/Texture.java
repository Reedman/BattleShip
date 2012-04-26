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

package at.bartinger.candroid.texture;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

public class Texture {
	
	public Bitmap tex = null;
	public String path;
	public boolean isLoaded = false;
	
	public Texture(String path) {
		this.path = path;
	}
	
	public Texture(Bitmap bmp){
		tex = bmp;
	}
	
	public void recycle(){
		tex.recycle();
		tex = null;
	}
	
	public Texture[] cut(int tileColumns, int tileRows){
		Texture[] textures = new Texture[tileColumns*tileRows];
		int cx = 0;
		int cy = 0;
		int counter = 0;
		int stepX = tex.getWidth()/tileColumns;
		int stepY = tex.getHeight()/tileRows;
		for(int i = 1; i <= tileRows; i++){
			for(int j = 0; j < tileColumns; j++){
				if(cx+stepX <= tex.getWidth()){
					textures[counter] = new Texture(Bitmap.createBitmap(tex, cx, cy, stepX, stepY));
					cx+=stepX;
					counter++;
				}
			}
			cx=0;
			if(cy+stepY <= tex.getHeight()){
				cy+=stepY; 
			}
			textures[textures.length-1] = new Texture(Bitmap.createBitmap(textures[textures.length-1].tex.getWidth(),textures[textures.length-1].tex.getHeight(),Config.RGB_565));
		}
		return textures;
	}

}

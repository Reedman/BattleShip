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
import android.graphics.Matrix;
import at.bartinger.candroid.texture.Texture;

public class FixedBackground extends Background{
	
	private Bitmap mBackground;
	
	public FixedBackground(Texture tex,int fixWidth,int fixHeight) {
		mBackground = tex.tex;
		
		getSize();
		
		Matrix mMatrix = new Matrix();
		float sw = (float)fixWidth/width;
		float sh = (float)fixHeight/height;
		mMatrix.postScale(sw, sh);
		mBackground = Bitmap.createBitmap(mBackground, 0, 0, width, height,mMatrix,false);
		
		getSize();
	}
	
	public void Scale(int fixWidth,int fixHeight){
		Matrix mMatrix = new Matrix();
		float sw = (float)fixWidth/width;
		float sh = (float)fixHeight/height;
		mMatrix.postScale(sw, sh);
		mBackground = Bitmap.createBitmap(mBackground, 0, 0, width, height,mMatrix,false);
		getSize();
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(mBackground, (int)x, (int)y, null);
		super.draw(canvas);
	}
	
	private void getSize(){
		height = mBackground.getHeight();
		width = mBackground.getWidth();
	}
	
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}

	public void recycle() {
		mBackground.recycle();
		mBackground = null;
	}

}

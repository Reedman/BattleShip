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

package at.bartinger.candroid.ingame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import at.bartinger.candroid.renderable.Renderable;

public class Text extends Renderable{
	
	private String text = "";
	private Paint paint = new Paint();
	
	public Text(Context context, int x, int y, String text, int color, int textSize, boolean bold, String fontPath) {
		this.text = text;
		this.x = x;
		this.y = y;
		
		paint.setTextSize(textSize);
		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(bold);
		paint.setTypeface(Typeface.createFromAsset(context.getAssets(),fontPath));
	}
	
	public Text(Context context, int x, int y, String text, int color, int textSize, boolean bold) {
		this.text = text;
		this.x = x;
		this.y = y;
		
		paint.setTextSize(textSize);
		paint.setColor(color);
		paint.setAntiAlias(true);
		paint.setFakeBoldText(bold);
	}
	
	public void setText(String text){
		this.text = text;
	}
	
	public String getText(){
		return text;
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawText(text, (float)x, (float)y, paint);
		super.draw(canvas);
	}

}

package at.bartinger.candroid.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import at.bartinger.candroid.renderable.Renderable;

public class Rectangle extends Renderable{
	
	private Paint mPaint = new Paint();
	
	public Rectangle(int x, int y, int width, int height, int color) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawRect((float)x,(float)y,width,height,mPaint);
	}

}

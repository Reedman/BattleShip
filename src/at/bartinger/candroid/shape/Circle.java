package at.bartinger.candroid.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import at.bartinger.candroid.renderable.Renderable;

public class Circle extends Renderable{
	
	public int radius = 0;
	private Paint mPaint = new Paint();
	
	public Circle(int x, int y, int radius, int color) {
		this.x = x;
		this.y = y;
		this.width = 2*radius;
		this.height = 2*radius;
		this.radius = radius;
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawCircle((float)x, (float)y, radius, mPaint);
	}
	
	public boolean collision(Circle c){
		double difX = c.x - this.x;
		double difY = c.y - this.y;
		double distance = Math.sqrt(difX*difX + difY*difY);
		return distance < (c.radius + this.radius);
	}
	
	public boolean collision(int x, int y){
		double difX = x - this.x;
		double difY = y - this.y;
		double distance = Math.sqrt(difX*difX + difY*difY);
		return distance < (this.radius);
	}

}

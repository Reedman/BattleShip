package at.bartinger.candroid.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import at.bartinger.candroid.renderable.Renderable;

public class Line extends Renderable{

	private Paint mPaint = new Paint();
	public double x2;
	public double y2;
	
	public Line(int x1, int y1, int x2, int y2, int color) {
		this.x = x1;
		this.y = y1;
		this.x2 = x2;
		this.y2 = y2;
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}
	public Line(Point p1, Point p2, int color){
		this.x = p1.x;
		this.y = p1.y;
		this.x2 = p2.x;
		this.y2 = p2.y;
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}
	
	public void setStrokeWidth(float w){
		mPaint.setStrokeWidth(w);
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawLine((float)x, (float)y, (float)x2, (float)y2, mPaint);
	}
	
}

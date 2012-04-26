package at.bartinger.candroid.shape;

import android.graphics.Canvas;
import android.graphics.Paint;
import at.bartinger.candroid.renderable.Renderable;

public class Polygon extends Renderable{

	private Paint mPaint = new Paint();
	public Line[] lines;

	public Polygon(Line [] lines, int color) {
		this.lines = lines;
		mPaint.setColor(color);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}

	public void setStrokeWidth(float w){
		mPaint.setStrokeWidth(w);
	}

	@Override
	public void draw(Canvas canvas) {
		for(int i = 0; i < lines.length; i++){
			lines[i].draw(canvas);
		}
	}

}

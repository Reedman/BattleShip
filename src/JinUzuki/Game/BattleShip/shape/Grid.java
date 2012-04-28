/**
 * 
 */
package JinUzuki.Game.BattleShip.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import at.bartinger.candroid.renderable.Renderable;

/**
 * @author v-alajin
 * the Grid frame 10*10
 */
public class Grid extends Renderable {
	
	int VerSize = 10; 
	int HrzSize = 10;
	int blkSize = 0;
	
	private Paint mPaint = new Paint();

	/**
	 * 
	 */
	public Grid(int bSize,int vSize,int hSize) {
		VerSize = vSize ;
		HrzSize = hSize ;
		blkSize = bSize ;
		
		mPaint.setColor(Color.WHITE);
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(2);
	}
	
	@Override
	public void draw(Canvas canvas) {
		for(int i=0; i<= VerSize; i++)
		{
			canvas.drawLine(0, blkSize*i,blkSize*HrzSize,blkSize*i, mPaint);
			canvas.drawLine(blkSize*i,0,blkSize*i,blkSize*VerSize, mPaint);
		}
	}

}

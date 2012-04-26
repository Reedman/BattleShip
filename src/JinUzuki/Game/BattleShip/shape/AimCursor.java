/**
 * 
 */
package JinUzuki.Game.BattleShip.shape;

import android.graphics.Canvas;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import JinUzuki.Game.BattleShip.Data.Position;

/**
 * @author v-alajin
 *
 */
public class AimCursor extends Cursor {

	public int posX;
	public int posY;
	
	public int centerX;
	public int centerY;
	
	public int targetX;
	public int targetY;
	
	public int rectSize;
	public int movingStep;
	
	public Position curPos;
	public int cellSize;
	
	public AimCursor(int x, int y, int cellsize) {
		super(x, y);

		centerX = x;
		centerY = y;
		cellSize = cellsize;
		rectSize = (int) (cellsize*0.4);
		
		curPos = new Position(x / cellsize,y / cellsize);
		
		movingStep = 12;
	}
	
	public void SetCenter(int x, int y) {
		centerX = x;
		centerY = y;
	}
	
	public void SetTarget(int x, int y) {
		targetX = x;
		targetY = y;
		
		velocityX = (targetX - centerX)/movingStep;
		velocityY = (targetY - centerY)/movingStep;
	}
	
	
	public Position Snaping(int x, int y, int cellsize){
		int nX = x / cellsize;
		int nY = y / cellsize;
		
		if(nX < 0) nX = 0;
		if(nX > 9) nX = 9;
		if(nY < 0) nY = 0;
		if(nY > 9) nY = 9;
		
		targetX = nX * cellsize + cellsize / 2;
		targetY = nY * cellsize + cellsize / 2;
		
		curPos.x = nX;
		curPos.y = nY;
		
		return new Position(nX,nY);
	}

	@Override
	public void draw(Canvas canvas) {
		if(this.visiable){
			canvas.drawLine(centerX, 0 , centerX , canvas.getHeight(), mPaint);
			canvas.drawLine(0 , centerY , canvas.getWidth() , centerY, mPaint);
			Rect r = new Rect(centerX - rectSize, centerY - rectSize, centerX + rectSize, centerY + rectSize);
			mPaint.setStyle(Style.STROKE);
			canvas.drawRect(r, mPaint);
		}
	}
	
	@Override
	public void update(){
		
		if(centerX == targetX && centerY == targetY){
			return;
		}
		
		boolean arriveX = false;
		boolean arriveY = false;
		if(Math.abs(centerX - targetX) <= Math.abs(velocityX)){
			centerX = targetX;
			//centerY = targetY;
			
			curPos.x = centerX / cellSize;
			
			arriveX = true;
			//curPos.y = centerY / cellSize;
			
			//Log.e("AimCursor","onAimCursorMoved");
			

		}
		else{
			centerX+=velocityX;
			//centerY+=velocityY;
		}
		
		if(Math.abs(centerY - targetY) <= Math.abs(velocityY)){
			centerY = targetY;
			
			curPos.y = centerY / cellSize;
			
			arriveY = true;
		}
		else{
			//centerX+=velocityX;
			centerY+=velocityY;
		}
		
		if(arriveX && arriveY){
			if(_onCursorMove != null)
				_onCursorMove.onCursorMoved(curPos);
		}
	}
}

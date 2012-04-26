package JinUzuki.Game.BattleShip.shape;

import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Interface.CursorMoveListener;
import android.graphics.Paint;
import at.bartinger.candroid.renderable.Renderable;

public class Cursor extends Renderable {
	
	protected boolean visiable = true;
	protected Paint mPaint = new Paint();
	protected CursorMoveListener _onCursorMove = null;
	
	public Cursor(int x, int y) {
		this.x = x;
		this.y = y;
		mPaint.setAntiAlias(true);
		mPaint.setColor(android.graphics.Color.BLACK);
		mPaint.setStrokeWidth(2);
	}
	
	public void Color(int color) {
		mPaint.setColor(color);
	}
	
	public void SetVisibility(boolean vis){
		this.visiable = vis;
	}
	
	public void setCursorMoveListener(CursorMoveListener onMove){
		this._onCursorMove = onMove;
	}
	
	public Position Snaping(int cellSize,int dx,int dy){
		int snapX = 0;
		int snapY = 0;

		int markX = 0,markY = 0;
		int grandX = 10000;
		int grandY = 10000;
		
		for(int i=0; i<10 ; i++){
			int disX = Math.abs(dx - cellSize*i);
			int disY = Math.abs(dy - cellSize*i);
			if(disX < grandX && disX >= 0){
				grandX = disX;
				markX = i;
			}
			if(disY < grandY && disY >= 0){
				grandY = disY;
				markY = i;
			}			
		}

		if(dx < 0) snapX = 0;
		if(dy < 0) snapY = 0;
		
		snapX = cellSize * markX;
		snapY = cellSize * markY;

		this.x = snapX;
		this.y = snapY;
		
		return new Position(markX,markY);
	}
	

}



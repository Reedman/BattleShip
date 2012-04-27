/**
 * 
 */
package JinUzuki.Game.BattleShip.shape;

import java.util.ArrayList;

import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.shipdir;
import JinUzuki.Game.BattleShip.Data.stage;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import at.bartinger.candroid.renderable.Renderable;
import at.bartinger.candroid.renderable.Sprite;
import at.bartinger.candroid.shape.Line;
import at.bartinger.candroid.texture.Texture;

/**
 * @author v-alajin
 *
 */
public class Board extends Renderable {

	//chess board size
	private int VerSize = 10; 
	private int HrzSize = 10;
	private int blkSize = 0;
	private boolean visible = true;
	private stage stg ;
	
	CellArray cArr[][] = new CellArray[10][10];
	
	private Paint mPaint = new Paint();
	private Bitmap seagull = null;
	
	/**
	 * 
	 */
	public Board(Texture tex,int bSize,int vSize,int hSize,stage st ) {
		VerSize = vSize ;
		HrzSize = hSize ;
		blkSize = bSize ;
		seagull = tex.tex;
		stg     = st;
		
		Matrix mMatrix = new Matrix();
		float sw = (float)blkSize/seagull.getWidth();
		float sh = (float)blkSize/seagull.getHeight();
		mMatrix.postScale(sw, sh);
		seagull = Bitmap.createBitmap(seagull, 0, 0, seagull.getWidth(), seagull.getHeight(),mMatrix,false);
		
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				cArr[i][j] = new CellArray(true,"");
			}
		}
	}
	
	/**
	 * 
	 */
	public void recycle(){
		mPaint = null;
		seagull.recycle();
		seagull = null;
		cArr = null;
	}
	
	/**
	 * @param ships
	 */
	public void ApplyShip(ArrayList<Ship> ships){
		
		ClearStatus();
		
		for(Ship ship:ships){
			if(ship.direct == shipdir.Horizon){
				for(int i= ship.pos.x ; i< ship.pos.x + ship.size; i++){
					cArr[i][ship.pos.y].shipID = ship.id;
					//cArr[i][ship.pos.y].status = 0;
				}
			}
			else if(ship.direct == shipdir.Vertical){
				for(int i= ship.pos.y ; i< ship.pos.y + ship.size; i++){
					cArr[ship.pos.x][i].shipID = ship.id;
					//cArr[ship.pos.x][i].status = 0;
				}
			}
		}
	}
	
	public void ClearStatus(){
		for(int i=0;i<10;i++){
			for(int j=0;j<10;j++){
				cArr[i][j].mist = true;
				cArr[i][j].shipID = "";
			}
		}
	}
	
	
	/**
	 * @param tgt
	 * @return //0-untouched; 1-touched; 2-null;
	 */
	public boolean CheckStatus(Position tgt){
		return cArr[tgt.x][tgt.y].mist;
	}
	
	public void SetStatus(Position tgt,boolean sta){
		cArr[tgt.x][tgt.y].mist = sta;
	}
	
	public String CheckShipID(Position tgt){
		return cArr[tgt.x][tgt.y].shipID;
	}
	
	public boolean HasShip(Position tgt){
		if(cArr[tgt.x][tgt.y].shipID != "")	return true;
		else return false;
	}
	
	public void SetVisibility(boolean vis){
		this.visible = vis;
	}
	
	public boolean checkPosibility(int size,int x,int y){
		//search vertical
		int vtlSize=1;
		for(int j=y;j<9;j++){
			if(vtlSize >= size) return true;
			if(cArr[x][j+1].mist == true) vtlSize++;
		}
		for(int j=y;j>1;j--){
			if(vtlSize >= size) return true;
			if(cArr[x][j-1].mist == true) vtlSize++;
		}
		
		//search horizonal
		int hrzSize=1;
		for(int i=x;i<9;i++){
			if(hrzSize >= size) return true;
			if(cArr[i+1][y].mist == true) hrzSize++;
		}
		for(int i=x;i>1;i--){
			if(hrzSize >= size) return true;
			if(cArr[i-1][y].mist == true) hrzSize++;
		}
		
		return false;
	}

	@Override
	public void draw(Canvas canvas) {
		//if(this.visible == true){
		if(stg == stage.enemy){
			for(int i=0;i<10;i++){
				for(int j=0;j<10;j++){
					if(cArr[i][j].mist == true)
					{
						int sgX = blkSize*i + (blkSize - seagull.getWidth())/2;
						int sgY = blkSize*j + (blkSize - seagull.getHeight())/2;
						canvas.drawBitmap(seagull, sgX, sgY, null);
					}
				}
			}
		}
		else
		{
			for(int i=0;i<10;i++){
				for(int j=0;j<10;j++){
					if(cArr[i][j].mist == false && cArr[i][j].shipID == "")
					{
						int sgX = blkSize*i + (blkSize - seagull.getWidth())/2;
						int sgY = blkSize*j + (blkSize - seagull.getHeight())/2;
						canvas.drawBitmap(seagull, sgX, sgY, null);
					}
				}
			}
		}
		
		//}
	}
}



class CellArray{
	
	public boolean mist = true;//0-untouched;1-touched;2-null;
	public String shipID = "";
	
	public CellArray(boolean mst,String id){
		this.mist = mst;
		this.shipID = id;
	}
}

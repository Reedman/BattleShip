/**
 * 
 */
package JinUzuki.Game.BattleShip;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

/**
 * @author v-alajin
 *
 */
public class CacheScreen {

	public Bitmap cacheImage;
	private int strip = 10;
	private int stripHeight;
	private int stripWidth;
	private int dx;
	private int dy;
	private int stepLength;
	private ArrayList<Bitmap> stripArray = null;
	
	private Matrix mMatrix = new Matrix();
	private Paint mPaint = new Paint();
	
	/**
	 * 
	 */
	public CacheScreen(int width) {
		// TODO Auto-generated constructor stub
		//cacheImage = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
		stripArray = new ArrayList<Bitmap>();
		stripWidth =  width;
		stripHeight = width/strip;
		stepLength = 26; 
		dx = 0;
		dy = 0;
	}
	
	public void setCacheBitmap(Bitmap bitmap){
		if(null == bitmap){ throw new IllegalArgumentException("bitmap is null.");}
		cacheImage = bitmap;
		dx = 0;
		createStripArray();
	}
	
	public Bitmap getStripBitmap(int StripIdx){
		return stripArray.get(StripIdx);
	}
	
	public void createStripArray(){
		for(int i=0; i< strip ; i++){
			Bitmap stripBitmap = Bitmap.createBitmap(cacheImage, 0, i*stripHeight, stripWidth, stripHeight);
			stripArray.add(stripBitmap);
		}
	}
	
	
	
	/**
	 * @return true - finish update; false - on update
	 */
	public boolean update(){
		if(dx < stripWidth){ 
			dx+=26;
			return false;
		}
		else{
			return true;
		}
	}
	
	
	public void draw(Canvas cvs){
		if(null == cvs){ throw new IllegalArgumentException("canvas is null.");}
		
		cvs.drawColor(Color.BLACK);
		
		for(int i=0; i< strip ; i++){
			dy = i*stripHeight;
			if(i%2 == 1)
				mMatrix.setTranslate(dx, dy);
			else if(i%2 == 0)
				mMatrix.setTranslate(-dx, dy);
			cvs.drawBitmap(stripArray.get(i), mMatrix, mPaint);
		}
	}

}

package JinUzuki.Game.BattleShip;

import java.util.ArrayList;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import JinUzuki.Game.BattleShip.AI.BaseAI;
import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.gameSetting;
import JinUzuki.Game.BattleShip.Data.shipdir;
import JinUzuki.Game.BattleShip.Data.stage;
import JinUzuki.Game.BattleShip.Data.turnStep;
import JinUzuki.Game.BattleShip.Interface.TurnEndListener;
import JinUzuki.Game.BattleShip.Interface.TurnStartListener;
import JinUzuki.Game.BattleShip.shape.AimCursor;
import JinUzuki.Game.BattleShip.shape.Board;
import JinUzuki.Game.BattleShip.shape.Grid;
import JinUzuki.Game.BattleShip.shape.Ship;
import JinUzuki.Game.BattleShip.Utility.Utility;
import at.bartinger.candroid.background.Background;
import at.bartinger.candroid.background.FixedBackground;
import at.bartinger.candroid.background.MultibleBackground;
import at.bartinger.candroid.renderable.Sprite;
import at.bartinger.candroid.renderable.TileAnimation;
import at.bartinger.candroid.renderer.Renderer;
import at.bartinger.candroid.renderer.SurfaceRenderer;
import at.bartinger.candroid.sound.SFXManager;
import at.bartinger.candroid.texture.Texture;
import at.bartinger.candroid.texture.TextureAtlas;
import at.bartinger.candroid.texture.TextureManager;

public class StageView extends SurfaceView implements SurfaceHolder.Callback {

	public StageThread mCanvasThread;
	
	protected SurfaceRenderer homeRenderer;
	protected SurfaceRenderer anotherRenderer;
	
	//Display
	public DisplayMetrics displayMetrics = null;
	public int SCREEN_WIDTH = 0;
	public int SCREEN_HEIGHT = 0;
	public float SCREEN_XDPI = 0;
	public float SCREEN_YDPI = 0;
	public int STAGE_WIDTH = 0;
	public int STAGE_HEIGHT = 0;
	public int cellSize = 0;
	public int blockCount = 10;
	
	//Touchscreen
	public boolean 	doTouch = false;
	public float 	touchPressure = 0;
	
	//context
	protected 	Context ctx 	 = null;
	
	protected 	Vibrator 		vibrator	= null;
	protected 	TextureAtlas 	atlas 		= null;	


	
	//sound
	public	  SFXManager sfx;
	protected boolean    bPlaySound = true;
	protected boolean    bVibrate   = true;
	
	public StageView(Context context,String name) {
		super(context);
			
		this.ctx = context;
		
		//sfx!
		sfx = new SFXManager(context);
		sfx.addSound("hit", "sounds/explosion.mp3");
		sfx.addSound("miss", "sounds/splash.mp3");
		
		vibrator = (Vibrator) ctx.getSystemService(Service.VIBRATOR_SERVICE);  
		
        //this.setZOrderOnTop(true);
        SurfaceHolder holder = getHolder();
        //holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);
        
        displayMetrics = context.getResources().getDisplayMetrics();
		SCREEN_HEIGHT = displayMetrics.heightPixels;
		SCREEN_WIDTH = displayMetrics.widthPixels;
		SCREEN_XDPI = displayMetrics.xdpi;
		SCREEN_YDPI = displayMetrics.ydpi;
		cellSize = SCREEN_WIDTH/blockCount;
		STAGE_WIDTH = SCREEN_WIDTH;
		STAGE_HEIGHT = SCREEN_WIDTH;

		homeRenderer = new SurfaceRenderer();
		anotherRenderer = new SurfaceRenderer();
		
		atlas = new TextureAtlas();
		
		//InitDrawable(instance);
		
		
        setFocusable(true); // make sure we get key events
	}
	
	public StageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	

	public StageView(Context context, AttributeSet attrs) {
		super(context, attrs);

        // register our interest in hearing about changes to our surface

	}
	
	@Override
	public void onDraw(Canvas canvas) {
		STAGE_WIDTH = canvas.getWidth();
		STAGE_HEIGHT = canvas.getHeight();
		//Log.d("StageSize:",Integer.toString(STAGE_WIDTH) + "," + Integer.toString(STAGE_HEIGHT));
	}
	
	public void setGameSetting(gameSetting gs){
		this.bPlaySound = gs.bPlaySound;
		this.bVibrate   = gs.bVibrate;
	}
	
	public boolean Attack(Position target){
		/*
		if(!this.chessBroad.HasShip(target)){
			//play water splash
			this.chessBroad.SetStatus(target, false);
			this.splashAni.x = aim.targetX - splashAni.width/2;
			this.splashAni.y = aim.targetY - splashAni.height/2;
			splashAni.start();
			if(bPlaySound) sfx.play("miss");//sfx.play("miss");
			
			return false;
		}
		else// if(this.chessBroad.HasShip(target) && this.chessBroad.CheckStatus(target)!=1  )
		{
			//ai.clearMark();
			if(ai!= null) ai.markHit(target.x, target.y);
			
			this.chessBroad.SetStatus(target, false);
			Ship ship = (Ship)spriteRenderer.getRenderable(this.chessBroad.CheckShipID(target));
			if(ship.Damage() && this.ai != null){
				ArrayList<Position> nPos = ship.getBuffer();
				for(Position pos:nPos){
					if(!chessBroad.HasShip(pos))
						chessBroad.SetStatus(pos, false); 
				}
				ai.removeCandidate(ship);
			}
			
			//play bomb
			this.explosionAni.x = aim.targetX - explosionAni.width/2;
			this.explosionAni.y = aim.targetY - explosionAni.height/2;
			explosionAni.start();
			if(bPlaySound) sfx.play("hit");
			
			//add fire Sprite
			Sprite fireSprite = new Sprite(txFire,aim.targetX,aim.targetY);
			
			float sw = (float) (((float) cellSize) / (float)fireSprite.width*0.8);
			float sh = (float) (((float) cellSize) / (float)fireSprite.height*0.8);
			
			fireSprite.scale(sw, sh);
			
			fireSprite.x = aim.targetX - fireSprite.width/2;
			fireSprite.y = aim.targetY - fireSprite.height/2;
			this.spriteRenderer.addRenderableAt(fireSprite, this.spriteRenderer.getRenderableSize()-3);
			
			if(bVibrate)
				vibrator.vibrate(100);
			
			return true;
		}
		*/
		return true;
	}
	
	public void setRendererAndStart(Renderer renderer,String name) {
		if(mCanvasThread != null){
			mCanvasThread.start();
		}
		else{
			
		}
	}
	

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int pressure = (int) (event.getPressure()*100);

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			doTouch = true;
			onTouchDown(x,y,pressure);
			return true;
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			onTouchMove(x,y,pressure);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			doTouch = false;
			onTouchUp(x,y,pressure);
			return super.onTouchEvent(event);
		}
		
		//return false;
		return super.onTouchEvent(event);
	}
	

	
	/**
	 * onTouchDown get called when the user touches the screen.
	 * @param touchX The x coordinate of the finger
	 * @param touchY The y coordinate of the finger
	 * @param pressure The size of the finger
	 */
	public void onTouchDown(int touchX, int touchY, int pressure){}

	/**
	 * onTouchMove get called when the user moves the finger on the screen.
	 * @param touchX The x coordinate of the finger
	 * @param touchY The y coordinate of the finger
	 * @param pressure The size of the finger
	 */
	public void onTouchMove(int touchX, int touchY, int pressure){}

	/**
	 * onTouchUp get called when the user releases the screen with the finger.
	 * @param touchX The x coordinate of the finger
	 * @param touchY The y coordinate of the finger
	 * @param pressure The size of the finger
	 */
	public void onTouchUp(int touchX, int touchY, int pressure){}
	
	public void pause(){
		mCanvasThread.pause();
	}
	
	public void resume(){
		mCanvasThread.restart();
	}
	
	/**
	 * Here you update, calculate and move everything you need
	 * deltaTime is the time between the last update and now
	 * @param deltaTime
	 */
	public void onUpdate(){}
	
	/**
	 * This get called when the screen rotates
	 * @param width The new width of the screen
	 * @param height The new height of the screen
	 */
	public void onWindowResize(int width, int height){}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//mCanvasThread.onWindowFocusChanged(hasFocus);
	}
	
	/**
	 * Set a Thread to be run on the rendering thread.
	 * @param r The runnable to be run on the rendering thread.
	 */
	public void setSecondRunnable(Runnable r) {
		mCanvasThread.setSecondRunnable(r);
	}

	/** Clears the runnable event, if any, from the rendering thread. */
	public void clearSecondRunnable() {
		mCanvasThread.clearSecondRunnable();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mCanvasThread.requestExitAndWait();
	}


	public void stopDrawing() {
		mCanvasThread.requestExitAndWait();
	}
	
	/**
	 * This is called when the View is starts with drawing
	 * Override this for your purposes!
	 * @param canvas draw with this Canvas
	 */
	public void onStartDrawing(Canvas canvas){}

	/**
	 * This is called when the View is finished with drawing
	 * Override this for your purposes!
	 * @param canvas draw with this Canvas
	 */
	public void onStopDrawing(Canvas canvas){}
	
	
	public void stopGame(){}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		//setRendererAndStart(spriteRenderer,instance);
		
		if(mCanvasThread != null){
			//Log.d("mCanvasThread start:",this.instance);
			
			if(!mCanvasThread.isAlive()){
				mCanvasThread.start();
			}
			mCanvasThread.surfaceCreated();
		}
		else{
			//Log.d("mCanvasThread is null:",this.instance);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {

        //Log.d("StageView", "surfaceDestroyed");  
        
		
		//mCanvasThread.requestExitAndWait();
		mCanvasThread.surfaceDestroyed();
		
		//mHolder.removeCallback(this);

	}
	
	
	public void onDestroy(){
	}

}

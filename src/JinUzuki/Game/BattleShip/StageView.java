package JinUzuki.Game.BattleShip;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import JinUzuki.Game.BattleShip.AI.BaseAI;
import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.gameSetting;
import JinUzuki.Game.BattleShip.Data.shipdir;
import JinUzuki.Game.BattleShip.Data.stage;
import JinUzuki.Game.BattleShip.Data.turnStep;
import JinUzuki.Game.BattleShip.Interface.AnimationPlayedListener;
import JinUzuki.Game.BattleShip.Interface.CursorMoveListener;
import JinUzuki.Game.BattleShip.Interface.TurnEndListener;
import JinUzuki.Game.BattleShip.Interface.TurnStartListener;
import JinUzuki.Game.BattleShip.shape.AimCursor;
import JinUzuki.Game.BattleShip.shape.Board;
import JinUzuki.Game.BattleShip.shape.Grid;
import JinUzuki.Game.BattleShip.shape.Ship;
import JinUzuki.Game.BattleShip.Utility.Utility;
import at.bartinger.candroid.CandroidThread;
import at.bartinger.candroid.background.Background;
import at.bartinger.candroid.background.ColoredBackground;
import at.bartinger.candroid.background.FixedBackground;
import at.bartinger.candroid.background.MultibleBackground;
import at.bartinger.candroid.ingame.Text;
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
	
	protected SurfaceRenderer spriteRenderer;
	protected Texture blueBackground;
	protected Background[] bgs = new Background[1];
	protected MultibleBackground mbg;
	protected boolean moveCursor = false;
	
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
	
	//renderer
	protected 	TileAnimation 	explosionAni = null;
	protected   TileAnimation	splashAni	= null;
	protected 	Board 			chessBroad	= null;
	protected 	Ship			bsSprite 	= null;
	protected	Ship 			RjSprite 	= null;
	protected	Ship 			klSprite 	= null;
	protected	Ship 			dsSprite 	= null;
	protected	Ship 			ds2Sprite 	= null;
	protected   ArrayList<Ship> fleet		= null;
	protected 	Texture 		txFire 		= null;
	protected 	AimCursor 		aim			= null;
	
	protected 	Vibrator 		vibrator	= null;
	protected 	TextureAtlas 	atlas 		= null;	

	//status
	protected  	Position  		curPos 		= null;
	public 		String 			action 		= "";
	public 		turnStep		step 		= null;//prepare,ready,turn start,turn over
	protected	String			instance		= "";
	
	//Listener
	protected TurnStartListener _onTurnStart;
	protected TurnEndListener _onTurnEnd;
	
	protected BaseAI ai      = null;
	
	//sound
	public	  SFXManager sfx;
	protected boolean    bPlaySound = true;
	protected boolean    bVibrate   = true;
	
	public StageView(Context context,String name) {
		super(context);
			
		this.ctx = context;
		this.instance = name;
		
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

		curPos = new Position(3,3);
		spriteRenderer = new SurfaceRenderer();
		atlas = new TextureAtlas();
		
		InitDrawable(instance);
		setRendererAndStart(spriteRenderer,name);
		
        setFocusable(true); // make sure we get key events
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
	
	public void restart(String name){
		if(null != spriteRenderer){
			setRendererAndStart(spriteRenderer,name);
		}
	}
	
	public boolean Attack(Position target){
				
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
		
	}
	
	public void startTurn(){
		step = turnStep.turnStart;
		this.aim.SetVisibility(true);
		
		
		step = turnStep.turnReady;
	}
	
	public void endTurn(){
		//sleep(1000);
		
		this.aim.SetVisibility(false);
		
		if(this._onTurnEnd != null){
			this._onTurnEnd.onTurnEnd();
		}
		
		step = turnStep.turnOver;
	}
	
	public void sleep(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void randomShips(){
		//Utility.randomShip(fleet);
		Utility.prepareFleet(fleet);
	}
	                          
	
	public void InitDrawable(String name){
		
		Grid grid = new Grid(cellSize,blockCount,blockCount);
		spriteRenderer.addRenderable(grid);
		
	    Texture explosionTex = new Texture("graphics/explosion.png");
		atlas.addTexture(explosionTex);
		TextureManager.load(ctx, atlas);
		explosionAni = new TileAnimation(explosionTex, 2, 2, 5, 5, 20);
		explosionAni.stop();
		
		Texture splashTex = new Texture("graphics/watersplash.png");
		atlas.addTexture(splashTex);
		TextureManager.load(ctx, atlas);
		splashAni = new TileAnimation(splashTex,2,2,3,3,40);
		splashAni.stop();
		
		int shipSize = 4;
		Texture txBS = new Texture("graphics/battleship.png"); 
		atlas.addTexture(txBS);
		
		TextureManager.load(ctx, atlas);
		bsSprite = new Ship(txBS,cellSize*3,0,shipSize,cellSize,shipdir.Horizon,ctx.getString(R.string.bshipID));
			
		float sw = ((float) cellSize*shipSize) / (float)bsSprite.width;
		float sh = ((float) cellSize) / (float)bsSprite.height;
		
		bsSprite.scale(sw, sh);
		spriteRenderer.addRenderable(bsSprite);
		
		shipSize = 5;
		Texture txRj = new Texture("graphics/ryujo.png");
		atlas.addTexture(txRj);
		TextureManager.load(ctx, atlas);
		RjSprite = new Ship(txRj,0,0,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.cshipID));

		sw = ((float) cellSize) / (float)RjSprite.width;
		sh = ((float) cellSize*shipSize) / (float)RjSprite.height;

		RjSprite.scale(sw, sh);
		spriteRenderer.addRenderable(RjSprite);
		
		shipSize = 2;
		Texture txSub = new Texture("graphics/kilo.png");
		atlas.addTexture(txSub);
		TextureManager.load(ctx, atlas);
		
		klSprite = new Ship(txSub,cellSize*3,cellSize*3,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.mshipID));
		
		sw = ((float) cellSize) / (float)klSprite.width;
		sh = ((float) cellSize*shipSize) / (float)klSprite.height;
		
		klSprite.scale(sw, sh);
		spriteRenderer.addRenderable(klSprite);
		
		shipSize = 3;
		Texture txDst = new Texture("graphics/destroyer1.png");
		atlas.addTexture(txDst);
		TextureManager.load(ctx, atlas);
		
		dsSprite = new Ship(txDst,cellSize*6,cellSize*3,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.dshipID));
		
		sw = ((float) cellSize) / (float)dsSprite.width;
		sh = ((float) cellSize*shipSize) / (float)dsSprite.height;
		
		dsSprite.scale(sw, sh);
		spriteRenderer.addRenderable(dsSprite);
		
		shipSize = 3;
		Texture txDst2 = new Texture("graphics/destroyer2.png");
		atlas.addTexture(txDst2);
		TextureManager.load(ctx, atlas);
		
		ds2Sprite = new Ship(txDst2,cellSize*7,cellSize*3,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.sshipID));
		
		sw = ((float) cellSize) / (float)ds2Sprite.width;
		sh = ((float) cellSize*shipSize) / (float)ds2Sprite.height;
		
		ds2Sprite.scale(sw, sh);
		spriteRenderer.addRenderable(ds2Sprite);
		
		txFire = new Texture("graphics/fire.png");
		atlas.addTexture(txFire);
		TextureManager.load(ctx, atlas);
		

		
		if(name == "home"){
			Texture txSG = new Texture("graphics/seagull.png");
			atlas.addTexture(txSG);
			TextureManager.load(ctx, atlas);
			chessBroad = new Board(txSG,cellSize,blockCount,blockCount,stage.home);
		}
		else{
			Texture txSG = new Texture("graphics/cloud3.png");
			atlas.addTexture(txSG);
			TextureManager.load(ctx, atlas);
			chessBroad = new Board(txSG,cellSize,blockCount,blockCount,stage.enemy);
		}
		spriteRenderer.addRenderable(chessBroad);
		
		fleet = new ArrayList<Ship>();
		fleet.add(bsSprite);
		fleet.add(RjSprite);
		fleet.add(klSprite);
		fleet.add(dsSprite);
		fleet.add(ds2Sprite);
		
		spriteRenderer.addRenderable(this.explosionAni);
		spriteRenderer.addRenderable(this.splashAni);
		
		aim = new AimCursor(4*cellSize - cellSize/2,4*cellSize - cellSize/2,cellSize);
		spriteRenderer.addRenderable(aim);
		
		
	}

	public StageView(Context context, AttributeSet attrs) {
		super(context, attrs);

        // register our interest in hearing about changes to our surface

	}

	public StageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
    final Handler viewHandler = new Handler();

    final Runnable mAttackStart = new Runnable() {
    	
        public void run() {
        	Attack(aim.curPos);
        }
    };
    
    final Runnable mEndTurn = new Runnable() {
    	
        public void run() {
        	endTurn();
        }
    };
    
    final Runnable mAttackEnd = new Runnable(){
    	public void run() {
    		//all ship destoryed , end game
    		
    		if(Utility.allDestoryed(fleet)){
    			//mCanvasThread.requestExitAndWait();
    			//((Activity)ctx).finish();
    			
		        //Intent intent = new Intent();  
		        //intent.setClass(ctx,GameOverActivity.class); 
		        
		        
		        //Bundle bundle = new Bundle();


    			if(instance == "home"){
    				((BattleShipActivity)ctx).dialog(false);
    				//intent.putExtra("vic", false);
    				//intent.putExtra("result",  "lose");
    		        //bundle.putString("result", "victory");
    			}
    			else{
    				((BattleShipActivity)ctx).dialog(true);
    				//dialog(true);
    				//intent.putExtra("vic", true);
    				//intent.putExtra("result",  "victory");
    				//bundle.putString("result", "lose");
    			}
		        //intent.putExtras(bundle);  
		        //ctx.startActivity(intent);  
		        
		       
		        //ctx.startActivity(intent);
    			//ctx.startActivity(new Intent(ctx, GameOverActivity.class));
    			//android.os.Process.killProcess(android.os.Process.myPid());
		        
		        //stopGame();
    			
    			
    			
    			
    			//((Activity)ctx).finish();
    			//System.exit(0);
    			
    			//finish();
    			//Log.e("endturn", "all ship destoryed , end game");
    		}
    	}
    };
    

    
    
    
    final Runnable mStartTurn = new Runnable() {
    	
        public void run() {
        	startTurn();
        }
    };
	
	public void setRendererAndStart(Renderer renderer,String name) {
		mCanvasThread = new StageThread(this, renderer, name);
		mCanvasThread.start();
	}
	
	public void setOnTurnStartListener(TurnStartListener _onTurnStart){
		this._onTurnStart = _onTurnStart;
	}
	
	public void setOnTurnEndListener(TurnEndListener _onTurnEnd){
		this._onTurnEnd = _onTurnEnd;
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
	
	public void stopGame(){
		mCanvasThread.requestExitAndWait();
		onDestroy();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		if(mCanvasThread != null){
			mCanvasThread.surfaceCreated();
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
		
		sfx.release();
		sfx = null;
		
		for(Ship ship:fleet){
			ship.recycle();
		}
		fleet = null;
		  
		FixedBackground bg = (FixedBackground)bgs[0];
		bg.recycle();
		bg = null;
		
		atlas.recycle();
		atlas = null;

		explosionAni.recycle();
		explosionAni = null;
		splashAni.recycle();
		splashAni = null;
		chessBroad.recycle();
		chessBroad = null;
		
		curPos = null;
		ctx = null;
		
	}

}

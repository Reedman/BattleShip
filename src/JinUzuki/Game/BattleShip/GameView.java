package JinUzuki.Game.BattleShip;

/**
 *@author v-alajin
 */
import java.util.ArrayList;

import JinUzuki.Game.BattleShip.AI.BaseAI;
import JinUzuki.Game.BattleShip.AI.GenericAI;
import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.shipdir;
import JinUzuki.Game.BattleShip.Data.stage;
import JinUzuki.Game.BattleShip.Data.turnStep;
import JinUzuki.Game.BattleShip.Interface.AnimationPlayedListener;
import JinUzuki.Game.BattleShip.Interface.CursorMoveListener;
import JinUzuki.Game.BattleShip.Interface.TurnEndListener;
import JinUzuki.Game.BattleShip.Interface.TurnStartListener;
import JinUzuki.Game.BattleShip.Utility.Utility;
import JinUzuki.Game.BattleShip.shape.AimCursor;
import JinUzuki.Game.BattleShip.shape.Board;
import JinUzuki.Game.BattleShip.shape.Grid;
import JinUzuki.Game.BattleShip.shape.Ship;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.widget.Toast;
import at.bartinger.candroid.background.Background;
import at.bartinger.candroid.background.FixedBackground;
import at.bartinger.candroid.background.MultibleBackground;
import at.bartinger.candroid.renderable.TileAnimation;
import at.bartinger.candroid.texture.Texture;
import at.bartinger.candroid.texture.TextureManager;

public class GameView extends StageView {
	
	//renderer
	protected 	TileAnimation 	explosionAni = null;
	protected   TileAnimation	splashAni	= null;
	protected 	Board 			homeBroad	= null;
	protected   Board			anotherBroad = null;
//	protected 	Ship			bsSprite 	= null;
//	protected	Ship 			RjSprite 	= null;
//	protected	Ship 			klSprite 	= null;
//	protected	Ship 			dsSprite 	= null;
//	protected	Ship 			ds2Sprite 	= null;
	protected   ArrayList<Ship> homefleet		= null;
	protected   ArrayList<Ship> anotherfleet	= null;
	protected 	Texture 		txFire 		= null;
	protected 	AimCursor 		homeAim			= null;
	protected 	AimCursor 		anotherAim		= null;
	
	//Listener
	protected TurnStartListener _onTurnStart;
	protected TurnEndListener _onTurnEnd;
	
	protected BaseAI ai      = null;
	
	//status
	protected  	Position  		curPos 		= null;
	public 		String 			action 		= "";
	public 		turnStep		step 		= null;//prepare,ready,turn start,turn over
	protected	String			instance		= "";
	
	public		Ship	selSprite = null;
	
	protected Texture blueBackground;
	protected Background[] bgs = new Background[1];
	protected MultibleBackground mbg;
	protected boolean moveCursor = false;

	
	public GameView(Context context) {
		super(context,"home");
		
		curPos = new Position(3,3);
		
		InitRenderer();
		
		mCanvasThread = new StageThread(this, homeRenderer,anotherRenderer, "haleruya");
		
		step = turnStep.inPrepare;
		ai = new GenericAI();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void InitRenderer(){
		Grid grid = new Grid(cellSize,blockCount,blockCount);
		homeRenderer.addRenderable(grid);
		
		Grid antGrid = new Grid(cellSize,blockCount,blockCount);
		anotherRenderer.addRenderable(antGrid);
		
		
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
		Ship bsSprite = new Ship(txBS,cellSize*3,0,shipSize,cellSize,shipdir.Horizon,ctx.getString(R.string.bshipID));
			
		float sw = ((float) cellSize*shipSize) / (float)bsSprite.width;
		float sh = ((float) cellSize) / (float)bsSprite.height;
		
		bsSprite.scale(sw, sh);
		homeRenderer.addRenderable(bsSprite);
		
		shipSize = 5;
		Texture txRj = new Texture("graphics/ryujo.png");
		atlas.addTexture(txRj);
		TextureManager.load(ctx, atlas);
		Ship RjSprite = new Ship(txRj,0,0,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.cshipID));

		sw = ((float) cellSize) / (float)RjSprite.width;
		sh = ((float) cellSize*shipSize) / (float)RjSprite.height;

		RjSprite.scale(sw, sh);
		homeRenderer.addRenderable(RjSprite);
		
		
		
		shipSize = 2;
		Texture txSub = new Texture("graphics/kilo.png");
		atlas.addTexture(txSub);
		TextureManager.load(ctx, atlas);
		
		Ship klSprite = new Ship(txSub,cellSize*3,cellSize*3,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.mshipID));
		
		sw = ((float) cellSize) / (float)klSprite.width;
		sh = ((float) cellSize*shipSize) / (float)klSprite.height;
		
		klSprite.scale(sw, sh);
		homeRenderer.addRenderable(klSprite);
		
		shipSize = 3;
		Texture txDst = new Texture("graphics/destroyer1.png");
		atlas.addTexture(txDst);
		TextureManager.load(ctx, atlas);
		
		Ship dsSprite = new Ship(txDst,cellSize*6,cellSize*3,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.dshipID));
		
		sw = ((float) cellSize) / (float)dsSprite.width;
		sh = ((float) cellSize*shipSize) / (float)dsSprite.height;
		
		dsSprite.scale(sw, sh);
		homeRenderer.addRenderable(dsSprite);
		
		shipSize = 3;
		Texture txDst2 = new Texture("graphics/destroyer2.png");
		atlas.addTexture(txDst2);
		TextureManager.load(ctx, atlas);
		
		Ship ds2Sprite = new Ship(txDst2,cellSize*7,cellSize*3,shipSize,cellSize,shipdir.Vertical,ctx.getString(R.string.sshipID));
		
		sw = ((float) cellSize) / (float)ds2Sprite.width;
		sh = ((float) cellSize*shipSize) / (float)ds2Sprite.height;
		
		ds2Sprite.scale(sw, sh);
		homeRenderer.addRenderable(ds2Sprite);
		
		txFire = new Texture("graphics/fire.png");
		atlas.addTexture(txFire);
		TextureManager.load(ctx, atlas);
		

		Texture txSG = new Texture("graphics/seagull.png");
		atlas.addTexture(txSG);
		TextureManager.load(ctx, atlas);
		homeBroad = new Board(txSG,cellSize,blockCount,blockCount,stage.home);
		homeRenderer.addRenderable(homeBroad);
		
		Texture txCld = new Texture("graphics/cloud3.png");
		atlas.addTexture(txCld);
		TextureManager.load(ctx, atlas);
		anotherBroad = new Board(txCld,cellSize,blockCount,blockCount,stage.enemy);
		anotherRenderer.addRenderable(anotherBroad);
		
		homefleet = new ArrayList<Ship>();
		homefleet.add(bsSprite);
		homefleet.add(RjSprite);
		homefleet.add(klSprite);
		homefleet.add(dsSprite);
		homefleet.add(ds2Sprite);
		
		homeRenderer.addRenderable(this.explosionAni);
		homeRenderer.addRenderable(this.splashAni);
		
		anotherRenderer.addRenderable(this.explosionAni);
		anotherRenderer.addRenderable(this.splashAni);
		
		homeAim = new AimCursor(4*cellSize - cellSize/2,4*cellSize - cellSize/2,cellSize);
		homeRenderer.addRenderable(homeAim);
		
		Texture bgTex = new Texture("graphics/bg_stage.png");
		atlas.addTexture(bgTex);
		TextureManager.load(ctx, atlas);
		
		bgs[0] = new FixedBackground(bgTex,STAGE_WIDTH,STAGE_HEIGHT);
		
	    mbg = new MultibleBackground(bgs);
	    homeRenderer.setBackground(mbg);
	    anotherRenderer.setBackground(mbg);
		
		this.explosionAni.setOnAnimePlayedListener(new AnimationPlayedListener(){

			@Override
			public void onAnimationPlayed() {
				//Log.e("HomeStageView","onexplosionPlayed");
				explosionAni.stop();
				startTurn();
			}
		});
		
		this.splashAni.setOnAnimePlayedListener(new AnimationPlayedListener(){

			@Override
			public void onAnimationPlayed() {
				//Log.e("HomeStageView","onsplashAniPlayed");
				splashAni.stop();
				viewHandler.post(mAttackEnd);
				viewHandler.post(mEndTurn);
			}
		});

		homeAim.setCursorMoveListener(new CursorMoveListener(){

			@Override
			public void onCursorMoved(Position tgt) {
				//Log.e("HomeStageView","Attack!");
				viewHandler.post(mAttackStart);
			}
			
		});
		                                               
		homeAim.SetVisibility(false);
		homeBroad.SetVisibility(false);
		anotherBroad.SetVisibility(true);
		
		Utility.prepareFleet(homefleet);
		homeBroad.ApplyShip(homefleet);
		
		//Utility.prepareFleet(homefleet);
		//homeBroad.ApplyShip(homefleet);
	}
	
	public void movingCursor(Position tgt){
		int x = tgt.x * cellSize + cellSize / 2;
		int y = tgt.y * cellSize + cellSize / 2;
		
		homeAim.SetTarget(x, y);

		curPos.x = x;
		curPos.y = y;
		
		moveCursor = true;
	}
	
	public void switchStage(){
		mCanvasThread.switchRenderer();
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
		Utility.prepareFleet(homefleet);
	}
	
    final Handler viewHandler = new Handler();

    final Runnable mAttackStart = new Runnable() {
    	
        public void run() {
        	Attack(homeAim.curPos);
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
    		
    		if(Utility.allDestoryed(homefleet)){
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
	
	@Override
	public void onTouchDown(int touchX, int touchY, int pressure) {
		
		if(step == turnStep.inPrepare){
			selSprite = (Ship)homeRenderer.searchRenderable(touchX, touchY, Ship.class);
			if(selSprite != null){
				selSprite.setAnchor((int)(touchX - selSprite.x),(int)(touchY - selSprite.y));
				homeRenderer.floatRenderable(selSprite);
			}
		}
		
		super.onTouchDown(touchX, touchY, pressure);
	}
	
	@Override	
	public void onTouchMove(int touchX, int touchY, int pressure){
		
		if(step == turnStep.inPrepare){
			if(selSprite != null){
				selSprite.x = touchX - selSprite.anchorX;
				selSprite.y = touchY - selSprite.anchorY;
				//Log.d("k", "onTouch");
			}
		}
		
		super.onTouchMove(touchX, touchY, pressure);
	}
	
	@Override	
	public void onTouchUp(int touchX, int touchY, int pressure){
		
		if(step == turnStep.inPrepare){
			if(selSprite != null){
				//snapShip(touchX - (int)selSprite.anchorX,touchY - (int)selSprite.anchorY,selSprite);
				selSprite.Snaping(touchX - (int)selSprite.anchorX, touchY - (int)selSprite.anchorY);
				selSprite.setAnchor(0,0);
				//selSprite = null;
			}
		}
		
		super.onTouchUp(touchX, touchY, pressure);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		
		Canvas myCav = holder.lockCanvas();
		STAGE_WIDTH = myCav.getWidth();
		STAGE_HEIGHT = myCav.getHeight();
		//Log.d("StageSize:",Integer.toString(STAGE_WIDTH) + "," + Integer.toString(STAGE_HEIGHT));
		holder.unlockCanvasAndPost(myCav);
		
		FixedBackground bg = (FixedBackground)bgs[0]; //= new FixedBackground(bgTex,STAGE_WIDTH,STAGE_HEIGHT);
		bg.Scale(STAGE_WIDTH, STAGE_HEIGHT);
	}
	
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		super.surfaceDestroyed(holder);
		
		//ai.recycle();
		
	}
	
	public void rotateShip(){
		if(selSprite !=  null)
			selSprite.rotate();
	}
	
	public boolean StartMission(){
		//no cross and touch
		if(!Utility.fleetReady(homefleet))	{
			Toast.makeText(this.ctx, "ship can't be crossed or touched each other", Toast.LENGTH_SHORT).show();
			return false;
		}
		else{
			homeBroad.ApplyShip(homefleet);
			this.endTurn();
		}
		
		return true;
	}
	
	public void startTurn(){

		step = turnStep.turnStart;
		this.homeAim.SetVisibility(true);
		step = turnStep.turnReady;
		//Log.d("turn", "homeTurnStart");
		
		ai.updateBattleField(this.homeBroad,this.homefleet);
		Position tgt = ai.askForTarget();
		
		this.movingCursor(tgt);
		//this.Attack(tgt);
		
		if(this._onTurnStart != null){
			this._onTurnStart.onTurnStart();
		}
	}
	
	public void endTurn(){
		//sleep(1000);
		
		this.homeAim.SetVisibility(false);
		
		if(this._onTurnEnd != null){
			this._onTurnEnd.onTurnEnd();
		}
		
		step = turnStep.turnOver;
	}
	
	public void setOnTurnStartListener(TurnStartListener _onTurnStart){
		this._onTurnStart = _onTurnStart;
	}
	
	public void setOnTurnEndListener(TurnEndListener _onTurnEnd){
		this._onTurnEnd = _onTurnEnd;
	}
	
	@Override
	public void stopGame(){
		mCanvasThread.requestExitAndWait();
		onDestroy();
	}
	
	@Override
	public void onUpdate(){
		
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		
		for(Ship ship:homefleet){
			ship.recycle();
		}
		homefleet = null;
		
		explosionAni.recycle();
		explosionAni = null;
		splashAni.recycle();
		splashAni = null;
		homeBroad.recycle();
		homeBroad = null;
		anotherBroad.recycle();
		anotherBroad = null;
		
		curPos = null;
		
		sfx.release();
		sfx = null;
		
		atlas.recycle();
		atlas = null;
		
		ctx = null;
		
	}
	
	

}

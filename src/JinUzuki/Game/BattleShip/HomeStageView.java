package JinUzuki.Game.BattleShip;

/**
 *@author v-alajin
 */
import JinUzuki.Game.BattleShip.AI.BaseAI;
import JinUzuki.Game.BattleShip.AI.GenericAI;
import JinUzuki.Game.BattleShip.AI.MonkeyAI;
import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.shipdir;
import JinUzuki.Game.BattleShip.Data.turnStep;
import JinUzuki.Game.BattleShip.Interface.AnimationPlayedListener;
import JinUzuki.Game.BattleShip.Interface.CursorMoveListener;
import JinUzuki.Game.BattleShip.Utility.Utility;
import JinUzuki.Game.BattleShip.shape.AimCursor;
import JinUzuki.Game.BattleShip.shape.Ship;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;
import at.bartinger.candroid.background.ColoredBackground;
import at.bartinger.candroid.background.FixedBackground;
import at.bartinger.candroid.background.MultibleBackground;
import at.bartinger.candroid.renderable.Sprite;
import at.bartinger.candroid.texture.Texture;
import at.bartinger.candroid.texture.TextureAtlas;
import at.bartinger.candroid.texture.TextureManager;

public class HomeStageView extends StageView {
	
	public		Ship	selSprite = null;

	
	public HomeStageView(Context context) {
		super(context,"home");
		
		InitRenderer();
		
		step = turnStep.inPrepare;
		ai = new GenericAI();
	}

	public HomeStageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public HomeStageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	public void InitRenderer(){
		//super.InitDrawable();
		
		Texture bgTex = new Texture("graphics/bg_stage.png");
		atlas.addTexture(bgTex);
		TextureManager.load(ctx, atlas);
		
		bgs[0] = new FixedBackground(bgTex,STAGE_WIDTH,STAGE_HEIGHT);
		
	    mbg = new MultibleBackground(bgs);
	    spriteRenderer.setBackground(mbg);
		
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

		aim.setCursorMoveListener(new CursorMoveListener(){

			@Override
			public void onCursorMoved(Position tgt) {
				//Log.e("HomeStageView","Attack!");
				viewHandler.post(mAttackStart);
			}
			
		});
		
		aim.SetVisibility(false);
		chessBroad.SetVisibility(false);
		
		Utility.prepareFleet(fleet);
		chessBroad.ApplyShip(fleet);
	}
	
	public void movingCursor(Position tgt){
		int x = tgt.x * cellSize + cellSize / 2;
		int y = tgt.y * cellSize + cellSize / 2;
		
		aim.SetTarget(x, y);

		curPos.x = x;
		curPos.y = y;
		
		moveCursor = true;
	}
	
	@Override
	public void onTouchDown(int touchX, int touchY, int pressure) {
		
		if(step == turnStep.inPrepare){
			selSprite = (Ship)spriteRenderer.searchRenderable(touchX, touchY, Ship.class);
			if(selSprite != null){
				selSprite.setAnchor((int)(touchX - selSprite.x),(int)(touchY - selSprite.y));
				spriteRenderer.floatRenderable(selSprite);
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
		if(!Utility.fleetReady(fleet))	{
			Toast.makeText(this.ctx, "ship can't be crossed or touched each other", Toast.LENGTH_SHORT).show();
			return false;
		}
		else{
			chessBroad.ApplyShip(fleet);
			this.endTurn();
		}
		
		return true;
	}
	
	@Override
	public void startTurn(){
		super.startTurn();
		
		//Log.d("turn", "homeTurnStart");
		
		ai.updateBattleField(this.chessBroad,this.fleet);
		Position tgt = ai.askForTarget();
		
		this.movingCursor(tgt);
		//this.Attack(tgt);
		
		if(this._onTurnStart != null){
			this._onTurnStart.onTurnStart();
		}
	}
	
	@Override
	public void endTurn(){
		
		//Log.d("turn", "homeTurnEnd");
		super.endTurn();
	}
	
	
	@Override
	public void onUpdate(){
		
	}
	
	

}

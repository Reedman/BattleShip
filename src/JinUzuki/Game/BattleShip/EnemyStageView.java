/**
 * 
 */
package JinUzuki.Game.BattleShip;

import JinUzuki.Game.BattleShip.Data.Position;
import JinUzuki.Game.BattleShip.Data.turnStep;
import JinUzuki.Game.BattleShip.Interface.AnimationPlayedListener;
import JinUzuki.Game.BattleShip.Utility.Utility;
import JinUzuki.Game.BattleShip.shape.AimCursor;
import JinUzuki.Game.BattleShip.shape.Ship;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import at.bartinger.candroid.background.ColoredBackground;
import at.bartinger.candroid.background.FixedBackground;
import at.bartinger.candroid.background.MultibleBackground;
import at.bartinger.candroid.renderable.Sprite;
import at.bartinger.candroid.texture.Texture;
import at.bartinger.candroid.texture.TextureManager;

/**
 * @author v-alajin
 *
 */
public class EnemyStageView extends StageView {
	
	/**
	 * @param context
	 */
	public EnemyStageView(Context context) {
		super(context,"Enemy");
		
		new Thread(){
			public void run(){
				InitRenderer();
				//mCanvasThread = new StageThread(EnemyStageView.this, hRenderer, "Enemy");
				mCanvasThread.start();
				mCanvasThread.surfaceCreated();
			}
		}.start();
		
		

		//mCanvasThread = new StageThread(this, spriteRenderer, "Enemy");
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public EnemyStageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public EnemyStageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	

	public void InitRenderer(){
		//super.InitDrawable();
		
		//bgs[0] = new ColoredBackground(Color.rgb(0, 90, 243));
       	//mbg = new MultibleBackground(bgs);
       	//spriteRenderer.setBackground(mbg);
		
		/*
		Texture bgTex = new Texture("graphics/bg_stage.png");
		atlas.addTexture(bgTex);
		TextureManager.load(ctx, atlas);
		
		bgs[0] = new FixedBackground(bgTex,STAGE_WIDTH,STAGE_HEIGHT);
		
	    mbg = new MultibleBackground(bgs);
	    //spriteRenderer.setBackground(mbg);
		
		this.explosionAni.setOnAnimePlayedListener(new AnimationPlayedListener(){

			@Override
			public void onAnimationPlayed() {
				//Log.e("EnemyStageView","onexplosionPlayed");
				//spriteRenderer.removeRenderable(explosionAni);
				explosionAni.stop();
				viewHandler.post(mAttackEnd);
				//endTurn();
			}
		});
		
		this.splashAni.setOnAnimePlayedListener(new AnimationPlayedListener(){

			@Override
			public void onAnimationPlayed() {
				//Log.e("EnemyStageView","onsplashAniPlayed");
				//spriteRenderer.removeRenderable(explosionAni);
				splashAni.stop();
				viewHandler.post(mEndTurn);
				//endTurn();
			}
		});
		*/
		
		//bsSprite.setVisibility(false);
		//RjSprite.setVisibility(false);
		//klSprite.setVisibility(false);
		
		/*
		for(Ship ship:fleet){
			ship.setVisibility(false);
		}
		
		Utility.prepareFleet(fleet);
		chessBroad.ApplyShip(fleet);
		*/
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		super.surfaceCreated(holder);
		
		Canvas myCav = holder.lockCanvas();
		STAGE_WIDTH = myCav.getWidth();
		STAGE_HEIGHT = myCav.getHeight();
		//Log.d("StageSize:",Integer.toString(STAGE_WIDTH) + "," + Integer.toString(STAGE_HEIGHT));
		
		/*
		FixedBackground bg = (FixedBackground)bgs[0]; //= new FixedBackground(bgTex,STAGE_WIDTH,STAGE_HEIGHT);
		bg.Scale(STAGE_WIDTH, STAGE_HEIGHT);
		
		holder.unlockCanvasAndPost(myCav);*/
	}
	
	public void movingCursor(int x,int y){
		//aim.SetTarget(x, y);
		//Position snapPos = aim.Snaping(x, y, cellSize);
		/*
		if(curPos.x == snapPos.x && curPos.y == snapPos.y && this.chessBroad.CheckStatus(curPos)){
			if(Attack(snapPos)){
				this.step = turnStep.turnWait;
			}
		}*/
	
		//Log.e("movingCursor","movingCursor");
		
		//curPos.x = snapPos.x;
		//curPos.y = snapPos.y;
		
		//moveCursor = true;
	}
	
	/*
	@Override
	public void startTurn(){
		//Log.d("turn", "enemyTurnStart");
		super.startTurn();
		this.step = turnStep.turnWait;
	}
	
	@Override
	public void endTurn(){
		//Log.d("turn", "enemyTurnEnd");
		
		super.endTurn();
	}
	
	*/
	@Override
	public void onUpdate(){
		
		//aim.update();

	}
	
	@Override
	public void onTouchDown(int touchX, int touchY, int pressure) {
		super.onTouchDown(touchX, touchY, pressure);
	}
	
	@Override	
	public void onTouchMove(int touchX, int touchY, int pressure){
		super.onTouchMove(touchX, touchY, pressure);
	}
	
	@Override	
	public void onTouchUp(int touchX, int touchY, int pressure){
		/*
		if(step == turnStep.turnWait){
			movingCursor(touchX,touchY);
		}
		super.onTouchUp(touchX, touchY, pressure);
		*/
	}
	
}

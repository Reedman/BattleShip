package JinUzuki.Game.BattleShip;

import java.util.ArrayList;
import java.util.List;

import JinUzuki.Game.BattleShip.R;
import JinUzuki.Game.BattleShip.Data.gameSetting;
import JinUzuki.Game.BattleShip.Data.stage;
import JinUzuki.Game.BattleShip.Data.turnStep;
import JinUzuki.Game.BattleShip.Interface.TurnEndListener;
import JinUzuki.Game.BattleShip.Interface.TurnStartListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class BattleShipActivity extends Activity {
    protected static final String BattleShipActivity = null;

	/** Called when the activity is first created. */
	
	private LayoutInflater mInflater;

	private LinearLayout surfaceContainer;
	

	
	private List<View> mListViews;
	private GameView gView;
	//private EnemyStageView enemyView;
    private MyPagerAdapter myAdapter;
    private stage curStage;
    private boolean isObserver = false;
    private boolean bLock      = true;
    private Context ctx = null;
    
    private ImageButton nextBtn = null;
    private ImageButton prevBtn = null;
    
    // 当Main对用户可见时被调用  
    @Override
    public void onStart() {  
        //Log.d("BattleShipActivity", "onStart");  
        super.onStart();  
    }  
  
    // 当Main开始与用户交互时被调用，此时Main位于  
    // 其中Activity的栈顶，onPause()方法往往在onResume()滞后被调用  
    @Override
    public void onResume() {  
        //Log.d("BattleShipActivity", "onResume");  
        
		//int curPage = myViewPager.getCurrentItem();
		
		/*
		if(curPage == 0){
			homeView.resume();
			//showStage(stage.enemy);
			//curStage = stage.enemy;
		}
		else
		{
			enemyView.resume();
			//showStage(stage.home);
			//curStage = stage.home;
		}*/
		
		//homeView.resume();
		//enemyView.resume();
		
		//homeView.restart("home");
        
        super.onResume();  
    }  
  
    // 当要恢复先前的Activity时被调用（或按Home，BackSpace），通常在这里实现数据的持久化  
    // 这个函数要尽可能快的返回，要恢复的Activity在返回onPause()才能被调用  
    @Override
    public void onPause() {  
        //Log.d("BattleShipActivity", "onPause");
        
    	gView.pause();
		//enemyView.pause();
        
        super.onPause();  
    }  
  
    // 当Main不再对用户可视被调用，可能的原因1.恢复先前Activity 2.按Home 3.按Back键关闭软件  
    @Override
    public void onStop() {  
        //Log.d("BattleShipActivity", "onStop");  
		//homeView.pause();
		//enemyView.pause();
        super.onStop();  
    }  
      
    // 恢复之前停止(onStop())的Activity会被调用  
    @Override
    public void onRestart() {  
        //Log.d("BattleShipActivity", "onRestart");  
        super.onRestart();  
    }  
  
    // 当Activity被destroy最后一个被调用的方法。  
    // 1.activity执行完了，显式调用finish()方法  
    // 2.系统暂时破坏这个Activity的实例来释放空间  
    // 3.人为主动关闭该软件  
    @Override
    public void onDestroy() {  
		//homeView.stopGame();
		//enemyView.stopGame();
		
       // Log.d("BattleShipActivity", "onDestroy");  
        super.onDestroy();  
    }  
    



	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        ctx = this;
        //view01 = new CustomStageView(this);
        //setContentView(view01);
        //myAdapter = new MyPagerAdapter();
        setContentView(R.layout.main);
        
        surfaceContainer = (LinearLayout) findViewById(R.id.viewPager);
        
        //surfaceContainer.setAdapter(myAdapter);

        //mListViews = new ArrayList<View>();
        
  	    BattleShip myApp = (BattleShip) getApplicationContext();
	    gameSetting gs =  myApp.getGameSetting();
	    
	    gView = new GameView(this);
	    gView.setGameSetting(gs);
	    gView.setDrawingCacheEnabled(true);
	    gView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        surfaceContainer.addView(gView);
        
        
        //mListViews.add(homeView);
        //enemyView = new EnemyStageView(this);
        //enemyView.setGameSetting(gs);
        //enemyView.setDrawingCacheEnabled(true);
        //enemyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
        //mListViews.add(enemyView);
        
        curStage = stage.home;
        
        gView.setOnTurnEndListener(new TurnEndListener(){

			@Override
			public void onTurnEnd() {
				//showStage(stage.enemy);
				gView.switchStage();
				gView.startTurn();
			}
        });
        
        gView.setOnTurnStartListener(new TurnStartListener(){

			@Override
			public void onTurnStart() {
				//showStage(stage.home);
				///homeView.endTurn();
			}
        });
        
        /*
        enemyView.setOnTurnEndListener(new TurnEndListener(){

			@Override
			public void onTurnEnd() {
				
				showStage(stage.home);
				//homeView.startTurn();
				
			}
        });*/
        
        /*
        myViewPager.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				homeView.onTouchEvent(event);
				return true;
		}});
        
        
        myViewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				//Log.d("onPageScrollStateChanged",  String.valueOf(arg0));
				
				if(arg0 == 2){
					bLock = true;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
				if(arg1 == 0 && arg2 == 0){
					//bLock = false;
					
					if(arg0 == 0 ){
						//Log.d("onPageScrolled",  "onPageScrolled");
						//if(isObserver == true){
						//	isObserver = false;
						//	return;
						//}
						if(homeView.step == turnStep.turnWait) return;
						
						new	Thread(){ 
							public void run(){ 
								try{ 
										Thread.sleep(500); 
										viewHandler.post(mHomeStart);
                                   }   catch   (InterruptedException   ie)   { 
                                   //   ignore 
                                   } 
                            } 
						}.start(); 
					}
					else if(arg0 == 1){ 
						//enemyView.resume();
						
						//isObserver = false;
						new	Thread(){ 
							public void run(){ 
								try{ 
										Thread.sleep(500); 
										viewHandler.post(mEnemyStart);
                                   }   catch   (InterruptedException   ie)   { 
                                   //   ignore 
                                   } 
                            } 
						}.start(); 
					}
				}
			
			}

			@Override
			public void onPageSelected(int arg0) {
				//Log.d("onPageSelectedarg0",  String.valueOf(arg0));
			}
        	
        });
        */
        
        ImageButton rotBtn = (ImageButton)this.findViewById(R.id.rotateship);
        rotBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				gView.rotateShip();
			}}
        );  
        
        //switch home/enemy view for observer
        nextBtn = (ImageButton)this.findViewById(R.id.onNext);
        nextBtn.setVisibility(View.INVISIBLE);
        nextBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//if(!isObserver) return;
				//if(bLock) return;
				//if(homeView.step != turnStep.turnOver) return;
				if(gView.step != turnStep.turnWait) return;
				gView.step = turnStep.turnOver;
				
				/*
				int curPage = myViewPager.getCurrentItem();
				if(curPage == 0){
					showStage(stage.enemy);
					curStage = stage.enemy;
				}
				else
				{
					//showStage(stage.home);
					//curStage = stage.home;
				}*/
				
			}}
        );
        
        prevBtn = (ImageButton)this.findViewById(R.id.onPrev);
        prevBtn.setVisibility(View.INVISIBLE);
        prevBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//if(bLock) return;
				
				/*
				if(enemyView.step != turnStep.turnWait) return;
				
				homeView.step = turnStep.turnWait;
				isObserver = true;
				//isObserver = true;
				*/
				/*
				int curPage = myViewPager.getCurrentItem();
				if(curPage == 0){
					//showStage(stage.enemy);
					//curStage = stage.enemy;
				}
				else
				{
					showStage(stage.home);
					curStage = stage.home;
				}*/
			}
        	
        });
        
        ImageButton readyBtn = (ImageButton)this.findViewById(R.id.onReady);
        readyBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//gView.switchStage();
				
				
				if(gView.StartMission()){
					
					
					ImageButton ready = (ImageButton)((Activity)ctx).findViewById(R.id.onReady);
					ImageButton random = (ImageButton)((Activity)ctx).findViewById(R.id.onRandom);
					ImageButton rot = (ImageButton)((Activity)ctx).findViewById(R.id.rotateship);
				
					ready.setVisibility(View.INVISIBLE);
					random.setVisibility(View.INVISIBLE);
					rot.setVisibility(View.INVISIBLE);
				
					prevBtn.setVisibility(View.VISIBLE);
					nextBtn.setVisibility(View.VISIBLE);
					
				}
			}
        });
        
        ImageButton randomBtn = (ImageButton)this.findViewById(R.id.onRandom);
        randomBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				gView.randomShips();
			}
        });
        
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread thread, Throwable ex) {
            //任意一个线程异常后统一的处理
            //System.out.println(ex.getLocalizedMessage());
                finish();
                    
            	
            }
        });
    }
    
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
    	savedInstanceState.putBoolean("MyBoolean", true);
        savedInstanceState.putDouble("myDouble", 1.9);
        savedInstanceState.putInt("MyInt", 1);
        savedInstanceState.putString("MyString", "Welcome back to Android");
        
        // etc.
        super.onSaveInstanceState(savedInstanceState);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);

    }
    
    public void resetGame(){
    	
    }
    
    final Handler viewHandler = new Handler();

    final Runnable mHomeStart = new Runnable() {
         public void run() {
        	 gView.resume();
        	 gView.startTurn();
         }
    };
    
    final Runnable mEnemyStart = new Runnable() {
        public void run() {
        	//enemyView.resume();
        	//enemyView.startTurn();
        }
    };
    
    public void showStage(stage st){
    	//gView.pause();
		//enemyView.pause();
		
		/*
    	if(st == stage.enemy)	{
    		myViewPager.setCurrentItem(1, true);
    	}
    	else
    	{
    		myViewPager.setCurrentItem(0, true);
    	}*/
    }

    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
            dialog(); 
            return false; 
        }
        else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) {
        	float v = gView.sfx.getVolume();
        	gView.sfx.setVolumeOf(v -0.1f );
        	//v = enemyView.sfx.getVolume();
        	//enemyView.sfx.setVolumeOf(v -0.1f );
        	
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
        	float v = gView.sfx.getVolume();
        	gView.sfx.setVolumeOf(v + 0.1f );
        	//v = enemyView.sfx.getVolume();
        	//enemyView.sfx.setVolumeOf(v + 0.1f );
        	
            return true;
        }
        return false; 
    }
    
    protected void dialog(boolean victory) {
        AlertDialog.Builder builder = new Builder(ctx); 
        if(victory)
        	builder.setMessage("Congratulations! Victory"); 
        else
        	builder.setMessage("Game Over");
        
        builder.setTitle(""); 
        
        builder.setPositiveButton("Back to title", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        //((BattleShipActivity)ctx).
                		//homeView.stopGame();
                		//enemyView.stopGame();
                        
                        gView.stopGame();
                		//enemyView.stopGame();
                		
                        //ctx.startActivity(new Intent(ctx, TitleActivity.class));
                        //android.os.Process.killProcess(android.os.Process.myPid());
            			//((Activity)ctx).finish();
            			//System.exit(0);
                		finish();
                    } 
                }); 
        builder.setNegativeButton("Stay", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    }
    
    protected void dialog() { 
        AlertDialog.Builder builder = new Builder(BattleShipActivity.this); 
        builder.setMessage("Are you sure to quit? Game data will be lost!"); 
        builder.setTitle(""); 
        builder.setPositiveButton("Sure", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                        //((BattleShipActivity)ctx).
                        gView.stopGame();
                		//enemyView.stopGame();
                		
                        //android.os.Process.killProcess(android.os.Process.myPid());
                        finish();
                    } 
                }); 
        builder.setNegativeButton("no", 
                new android.content.DialogInterface.OnClickListener() { 
                    @Override 
                    public void onClick(DialogInterface dialog, int which) { 
                        dialog.dismiss(); 
                    } 
                }); 
        builder.create().show(); 
    }
    
    private class MyPagerAdapter extends PagerAdapter{
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            //Log.d("k", "destroyItem");
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
            //Log.d("k", "finishUpdate");
        }

        @Override
        public int getCount() {
            //Log.d("k", "getCount");
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            //Log.d("k", "instantiateItem");
            ((ViewPager) arg0).addView(mListViews.get(arg1),0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //Log.d("k", "isViewFromObject");
            return arg0==(arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            //Log.d("k", "restoreState");
        }
        
        @Override
        public Parcelable saveState() {
            //Log.d("k", "saveState");
            return null;
        }
        
        @Override
        public void startUpdate(View arg0) {
            //Log.d("k", "startUpdate");
        }

    }

}
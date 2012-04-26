/**
 * 
 */
package JinUzuki.Game.BattleShip;

import JinUzuki.Game.BattleShip.Data.gameSetting;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * @author v-alajin
 *
 */
public class TitleActivity extends Activity {

    //private gameSetting gs = new gameSetting();
	/**
	 * 
	 */
	public TitleActivity() {
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.titleview);

        ImageButton btnStart = (ImageButton)this.findViewById(R.id.btnStartGame);
        btnStart.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(TitleActivity.this, BattleShipActivity.class));
				//finish();
			}
        });
        

        ImageButton btnSet = (ImageButton)this.findViewById(R.id.btnSetGame);
        btnSet.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				//startActivity(new Intent(TitleActivity.this, SettingActivity.class));
				
		  	  	BattleShip myApp = (BattleShip) getApplicationContext();
		  	  	gameSetting gs = myApp.getGameSetting();
				
		        /*new一个Intent对象，并指定class*/ 
		        Intent intent = new Intent();  
		        intent.setClass(TitleActivity.this,SettingActivity.class);  
		        
		        /*new一个Bundle对象，并将要传递的数据传入*/ 
		        Bundle bundle = new Bundle();  
		        bundle.putBoolean("bPlaySound",gs.bPlaySound);  
		        bundle.putBoolean("bVibrate", gs.bVibrate);  
		        
		        /*将Bundle对象assign给Intent*/ 
		        intent.putExtras(bundle);  
		        
		        /*调用Activity EX03_11_1*/ 
		        startActivityForResult(intent,0);  
			}
        	
        });
        
        ImageButton btnQuit = (ImageButton)this.findViewById(R.id.btnQuitGame);
        btnQuit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
				//android.os.Process.killProcess(android.os.Process.myPid());
			}
        });
        
    }
    
    @Override 
    protected void onActivityResult(int requestCode, int resultCode,  
                                    Intent data)  
    {  
      switch (resultCode)  
      {   
        case RESULT_OK:     
          Bundle bunde = data.getExtras();
    	  BattleShip myApp = (BattleShip) getApplicationContext();
      	  gameSetting gs = myApp.getGameSetting();
    	  gs.bPlaySound = bunde.getBoolean("bPlaySound");  
    	  gs.bVibrate = bunde.getBoolean("bVibrate");  
    	  //myApp.setGameSetting(gs);
    	  
          break;  
        default:  
          break;  
       }   
     } 

}

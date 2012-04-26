/**
 * 
 */
package JinUzuki.Game.BattleShip;

import JinUzuki.Game.BattleShip.Data.gameSetting;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author v-alajin
 *
 */
public class GameOverActivity extends Activity {

	/**
	 * 
	 */
	public GameOverActivity() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	
	    setContentView(R.layout.gameover);
	    
	    Log.e("GameOverActivity","onCreate");
	    
	    LinearLayout bg = (LinearLayout)this.findViewById(R.id.gameoverView);
	    TextView txtV = (TextView)this.findViewById(R.id.txtViewRst);
	    
	    bg.setBackgroundResource(R.drawable.victory);
	    
	    Intent intent = getIntent();  
	    //String strFromCaller = intent.getStringExtra("key");  
	    
        //Bundle bData = this.getIntent().getExtras();
	    boolean rst = intent.getBooleanExtra("vic", true);
        //String rst = intent.getStringExtra("result");
        
        //Log.d("Bundle",rst);
        
        
		if(rst){
			Log.e("GameOverActivity", "victory");
			txtV.setText("Victory");
	        bg.setBackgroundResource(R.drawable.victory);
		}
		else{
			Log.e("GameOverActivity", "lose");
			txtV.setText("Game Over");
			bg.setBackgroundResource(R.drawable.sinking);
		}
		
	    
        Button btnTitle = (Button)this.findViewById(R.id.btnToTitle);
        btnTitle.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				startActivity(new Intent(GameOverActivity.this, TitleActivity.class));
				finish();
			}
        });
        
        /*
        Button btnQuit = (Button)this.findViewById(R.id.btnQuitGame);
        btnQuit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				android.os.Process.killProcess(android.os.Process.myPid());
			}
        });
		*/
	}

}

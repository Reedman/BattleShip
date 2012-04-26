/**
 * 
 */
package JinUzuki.Game.BattleShip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JinUzuki.Game.BattleShip.Adapter.CheckBoxAdapter;
import JinUzuki.Game.BattleShip.Data.gameSetting;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.View;  
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author v-alajin
 *
 */
public class SettingActivity extends Activity {

    private ListView mList; 
    private CheckBoxAdapter adapter;
	
	/**
	 * 
	 */
	public SettingActivity() {
		// TODO Auto-generated constructor stub
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.settingview);
        
        mList = (ListView) findViewById(R.id.set_list);
        TextView txtView = new TextView(this);
        txtView.setTextSize(20);
        txtView.setText("游戏设置(Settings)");
        
        mList.addHeaderView(txtView);
        
        gameSetting gs = new gameSetting();
        Bundle bData = this.getIntent().getExtras();
        gs.bPlaySound = bData.getBoolean("bPlaySound");
        gs.bVibrate = bData.getBoolean("bVibrate");
        
        //adapter
        adapter = new CheckBoxAdapter(this,getData(),gs); 
        mList.setAdapter(adapter);  
        
        // 添加点击  
        mList.setOnItemClickListener(new OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,  
                    long arg3) {  
                //setTitle("点击第" + arg2 + "个项目");  
                //Toast.makeText(SettingActivity.this, "点击第" + arg2 + "个项目",  
                //        Toast.LENGTH_LONG).show();  
            }  
        });  
        
    }
    
    @Override 
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
    	
    	
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { 
        	//ListAdapter  adp = mList.getAdapter();mList.getd
        	Intent intent = this.getIntent();
        	intent.putExtra("bPlaySound", adapter.set.bPlaySound);
        	intent.putExtra("bVibrate", adapter.set.bVibrate);
            this.setResult(RESULT_OK, intent);  
            this.finish();  
        }
        else if(keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) {
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_VOLUME_UP){
            return true;
        }
        return false; 
    }
    
    private List<Map<String, String>> getData() {  
        //data source
        List<Map<String, String>> mylist = new ArrayList<Map<String, String>>();  
        
        Map<String, String> map = new HashMap<String, String>();  
        map.put("0", "0");  
        map.put("itemTitle", "音效(sfx)");  
        mylist.add(map); 

        //map = new HashMap<String, String>();  
        //map.put("1", "1");  
        //map.put("itemTitle", "背景音乐");  
        //mylist.add(map); 

        map = new HashMap<String, String>();  
        map.put("1", "1");  
        map.put("itemTitle", "震动(vibrate)");  
        mylist.add(map); 

        return mylist;  
    }  

}

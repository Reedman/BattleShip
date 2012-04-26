/**
 * 
 */
package JinUzuki.Game.BattleShip.Adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import JinUzuki.Game.BattleShip.R;
import JinUzuki.Game.BattleShip.Data.gameSetting;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author v-alajin
 *
 */
public class CheckBoxAdapter extends BaseAdapter {  
	  
    private LayoutInflater mInflater;  
  
    private List<Map<String, String>> listData;  
  
    private Map<Integer, Map<String, String>> selectMap = new HashMap<Integer, Map<String, String>>(); 
    
    public gameSetting set = new gameSetting();
  
    private class ViewHolder {  
        public ImageView img;  
        public TextView title;  
        public CheckBox checkBox;  
    }  
  
    public CheckBoxAdapter(Context context, List<Map<String, String>> listData, gameSetting setting) {  
        this.mInflater = LayoutInflater.from(context);  
        this.listData = listData;  
        this.set = setting;
    }  
  
    @Override  
    public int getCount() {  
        return listData.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return listData.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(final int position, View convertView, ViewGroup parent) {  
        ViewHolder holder = null;  
        if (convertView == null) {  
            holder = new ViewHolder();  
            convertView = mInflater.inflate(R.layout.settingitem, null);  
            final View view = convertView;  
            holder.img = (ImageView) convertView.findViewById(R.id.img);  
            holder.title = (TextView) convertView.findViewById(R.id.itemTitle);  
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb);  
            holder.checkBox.setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View v) {  
                	CheckBox cb = (CheckBox)v; 
                	if(cb.isChecked()){
                    //if (selectMap.get(position) != null) {  
                        //selectMap.remove(position);
                        if(position == 0)
                        	set.bPlaySound = true;
                        else if(position == 1)
                        	set.bVibrate = true;
                        
                    } else {  
                        //selectMap.put(position, listData.get(position));
                        if(position == 0)
                        	set.bPlaySound = false;
                        else if(position == 1)
                        	set.bVibrate = false;
                    }  
                }  
            });  
            convertView.setTag(holder);  
        } else {  
            holder = (ViewHolder) convertView.getTag();  
        }  
  
        //holder.img.setBackgroundResource(R.drawable.icon);  
        holder.title.setText(listData.get(position).get("itemTitle"));  
  
        //if (selectMap.get(position) != null) {  
        //    holder.checkBox.setChecked(true);  
        //} else {  
        //    holder.checkBox.setChecked(false);  
        //}
        
        if(position == 0){
	        if(set.bPlaySound){
	        	holder.checkBox.setChecked(true);  
	        }
	        else {
	        	holder.checkBox.setChecked(false);  
	        }
        }
        else if(position == 1){
	        if(set.bVibrate){
	        	holder.checkBox.setChecked(true);  
	        }
	        else {
	        	holder.checkBox.setChecked(false);  
	        }
        }
  
        return convertView;  
    }  
}  

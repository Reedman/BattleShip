/**
 *  Copyright 2010 Bartl Dominic

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package at.bartinger.candroid;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

/**
 * I recommend CandroidActivity you to use this activity, because it sets the VolumeControlStream for games and
 * you can easily set your game to fullscreen
 * @author Dominic Bartl
 *
 */
public class CandroidActivity extends Activity{
	
	private DisplayMetrics dm;
	
	public boolean isFullscreen = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        
        Log.d(Constants.LOGTAG, "CandroidActivity created("+dm.widthPixels+"x"+dm.heightPixels+")");
		
		
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * You can set the application to fullscreen or to normal screen with the method
	 * @param fullscreen true if it should go to fullscreen
	 */
	public void setFullscreen(boolean fullscreen){
		if(fullscreen){
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
	        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
	        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		}else{
			this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); 
			this.getWindow().requestFeature(Window.FEATURE_CUSTOM_TITLE);
		}
		isFullscreen = fullscreen;
	}
	
	/**
	 * You can use this as the CandroidSurfaceView parameter
	 * @return returns the screen specified DisplayMetrics
	 */
	public DisplayMetrics getDisplayMetrics(){
		return dm;
	}
	
	@Override
	protected void onDestroy() {
		try {
			finalize();
		} catch (Throwable e) {}
		super.onDestroy();
	}
	
}

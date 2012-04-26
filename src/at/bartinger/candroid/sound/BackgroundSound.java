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

package at.bartinger.candroid.sound;

import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import at.bartinger.candroid.Constants;


public class BackgroundSound {

	private MediaPlayer aPlayer=new MediaPlayer();
	private float volume = 1;

	public BackgroundSound(Context context, String assetpath , boolean loop) {
		
		AssetFileDescriptor afd = null;
		try {
			afd = context.getAssets().openFd(assetpath);
			aPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			aPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			aPlayer.prepare();
			aPlayer.setLooping(loop);
		} catch (IOException e) {
			Log.e(Constants.LOGTAG, "Could not open " + assetpath);
		}
		
		
	}

	public void start(){
		if(!aPlayer.isPlaying()){
			aPlayer.start();
		}
	}

	public void stop(){
		if(aPlayer.isPlaying()){
			aPlayer.stop();
		}
	}
	
	public void setVolume(float v){
		aPlayer.setVolume(v, v);
		volume=v;
	}
	
	public void mute(){
		setVolume(0);
		volume=0;
	}
	
	public void toogleOn_Off(){
		if(volume == 0f) setVolume(1);
		else setVolume(0);
	}

}

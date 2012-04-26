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
import java.util.TreeMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

public class SFXManager {

	private Context context;
	private final int MAX_STREAMS = 10;
	
	private SoundPool soundpool;
	private float volume = 0.2f;
	
	private TreeMap<String,Integer> nameToId = new TreeMap<String, Integer>();

	
	
	public SFXManager(Context context) {
		this.context = context;
		soundpool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 100);
	}
		
	public void addSound(String soundname, String assetPath){
		try {
			nameToId.put(soundname, soundpool.load(context.getResources().getAssets().openFd(assetPath), 100));
		} catch (IOException e) {
			Log.e("CanvasGame", "Could not open SoundFile" + assetPath);
		}
	}

	public void play(String soundname){
		soundpool.play(nameToId.get(soundname), volume, volume, 1, 0, 1f);
	}
	
	public void play(String soundname, float rate){
		soundpool.play(nameToId.get(soundname), volume, volume, 1, 0, rate);
	}
	
	public void stopSounds(){
		soundpool.pause(1);
	}
	
	public void release() {
		soundpool.release();
		soundpool = null;
	}
	
	public void setVolumeOf(float v){
		volume = v;
		
	}
	
	public float getVolume(){
		return volume;
	}

}

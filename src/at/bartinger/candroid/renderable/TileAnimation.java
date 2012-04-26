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

package at.bartinger.candroid.renderable;

import java.util.ArrayList;

import JinUzuki.Game.BattleShip.Interface.AnimationPlayedListener;
import android.graphics.Bitmap;
import android.util.Log;
import at.bartinger.candroid.texture.Texture;


public class TileAnimation extends Sprite{

	/**
	 * @author Dominic Bartl
	 */


	public Sprite fullImage;//the source image

	private ArrayList<Bitmap> imagelist = new ArrayList<Bitmap>();//list of all images

	private int currentFrameCounter =0;//number of the current frame
	private int frameskipdelay; // time between every frame of the animation

	//number of times how often the image gets cut
	private int tileColumns;
	private int tileRows;

	//true if the animation is running
	public boolean isStarted = true;

	private long pasteTime = 0;
	private long lastTime;
	
	//listener
	AnimationPlayedListener _onAnimePlayed;

	public TileAnimation(Texture tex, int x, int y, int tileColumns, int tileRows, int frameskipdelay) {
		super(tex, x, y);

		this.frameskipdelay = frameskipdelay;
		this.tileRows = tileRows;
		this.tileColumns = tileColumns;
		fullImage = new Sprite(tex, 0, 0);
		cutImage();
		this.sprite = imagelist.get(0);
		lastTime = System.currentTimeMillis();
	}

	public TileAnimation(Texture[] textures, int x, int y, int frameskipdelay) {
		super(textures[0], x, y);

		this.frameskipdelay = frameskipdelay;
		fullImage = null;
		//tiles = assetPaths.length;
		for(int i = 0; i < textures.length; i++){
			imagelist.add(new Sprite(textures[i], x, y).sprite);
		}
		this.sprite = imagelist.get(0);
		lastTime = System.currentTimeMillis();
	}


	private void cutImage(){
		int cx = 0;
		int cy = 0; 
		int stepX = fullImage.width/tileColumns; this.width = stepX;
		int stepY = fullImage.height/tileRows;	this.height = stepY;
		for(int i = 1; i <= tileRows; i++){
			for(int j = 0; j < tileColumns; j++){
				if(cx+stepX <= fullImage.width){
					imagelist.add(Bitmap.createBitmap(fullImage.sprite, cx, cy, stepX, stepY));
					cx+=stepX; 
				}
			}
			cx=0;
			if(cy+stepY <= fullImage.height){
				cy+=stepY; 
			}

		}
	}

	@Override
	public void recycle() {
		for(Bitmap b : imagelist){
			b.recycle();
		}
		imagelist = null;
		if(fullImage != null)
		fullImage.recycle();
		super.recycle();
	}

	public void update(){
		super.update();
		if(isStarted){
			long deltaTime = System.currentTimeMillis() - lastTime;
			lastTime = System.currentTimeMillis();
			pasteTime+=deltaTime;
			if(pasteTime >= frameskipdelay){
				pasteTime = 0;
				nextFrame();
			}
		}

	}

	public void nextFrame(){
		if(currentFrameCounter < imagelist.size()-1){
			
			//Log.e("FrameCounter",Integer.toString(currentFrameCounter));
			//Log.e("imagelist",Integer.toString(imagelist.size()));
			
			currentFrameCounter++;
		}else{
			if(_onAnimePlayed != null)
				_onAnimePlayed.onAnimationPlayed();
			//currentFrameCounter=0;
			return;
		}
		sprite = imagelist.get(currentFrameCounter);
	}
	
	public void setOnAnimePlayedListener(AnimationPlayedListener _onAnimePlayed){
		this._onAnimePlayed = _onAnimePlayed;
	}

	public void start(){
		this.setVisibility(true);
		currentFrameCounter = 0;
		isStarted=true;
	}

	public void stop(){
		isStarted=false;
		this.setVisibility(false);
	}


}

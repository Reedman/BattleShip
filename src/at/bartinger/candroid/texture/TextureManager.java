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

package at.bartinger.candroid.texture;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import at.bartinger.candroid.Constants;

public class TextureManager {

	/**
	 * Loads a bitmap from a path and converts it to a bitmap.  This is
	 * a much-simplified version of an BitmapLoader
	 * @param context  The application context.
	 * @param atlas A TextureAtlas which is holding the Textures.
	 */
	
	public static void load(Context context, TextureAtlas atlas){
		BitmapFactory.Options DEFAULT_BITMAP_OPTIONS = new BitmapFactory.Options();
		DEFAULT_BITMAP_OPTIONS.inPreferredConfig = Bitmap.Config.ARGB_8888;
		
		Bitmap bitmap = null;
		InputStream is = null;
		long allTime = System.currentTimeMillis();
		for(Texture tex : atlas.getQueue()){
			try {
				is = context.getResources().getAssets().open(tex.path);
				
				bitmap = BitmapFactory.decodeStream(is, null, DEFAULT_BITMAP_OPTIONS);
				tex.isLoaded = true;
				
			}catch (IOException e) {
				//Log.e(Constants.LOGTAG, "Can't find/load Texture: " + tex.path);
				throw new NullPointerException("Can't find Texture: " + tex.path);
			} finally {
				
				try {
					
					is.close();
				} catch (IOException e) {
					//Log.d(Constants.LOGTAG, "Can't close BitmapInputStream");
				}
			}
			
			tex.tex =  bitmap;
		}
		//Log.d(Constants.LOGTAG, "Loaded " + atlas.getQueue().size() + " Bitmaps in "+(System.currentTimeMillis()-allTime));
	}
	
//	public static void load(Context context, TextureAtlas atlas){
//		
//		Bitmap bitmap = null;
//		
//		if (context != null) {
//
//			InputStream is = null;
//			
//			for(Texture tex : atlas.getQueue()){
//				try {
//					is = context.getResources().getAssets().open(tex.path);
//					bitmap = BitmapFactory.decodeStream(is, null, DEFAULT_BITMAP_OPTIONS);
//					tex.isLoaded = true;
//
//				}catch (IOException e) {
//					Log.e(Constants.LOGTAG, "Can't find/load Texture: " + tex.path);
//				} finally {
//					atlas.loadedQueue = true;
//					try {
//						atlas.loadedQueue = true;
//						is.close();
//					} catch (IOException e) {
//						Log.d(Constants.LOGTAG, "Can't close BitmapInputStream");
//					}
//				}
//				
//				tex.tex = bitmap;
//			}
//		}
//		bitmap.recycle();
//	}
	
	
	



}

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

import java.util.ArrayList;

public class TextureAtlas {
	
	private ArrayList<Texture> textures;
	public boolean loadedQueue = false;
	
	public TextureAtlas() {
		textures = new ArrayList<Texture>();
	}

	public void addTexture(Texture tex){
		textures.add(tex);
	}

	public ArrayList<Texture> getQueue() {
		return textures;
	}
	
	public void recycle(){
		for(Texture texture:textures){
			texture.recycle();
		}
	}
	
	
}

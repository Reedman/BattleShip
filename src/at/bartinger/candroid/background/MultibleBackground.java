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

package at.bartinger.candroid.background;

import android.graphics.Canvas;

public class MultibleBackground extends Background{

	private Background[] backgrounds;

	public MultibleBackground(Background[] bgs) {
		backgrounds = bgs;
	}

	@Override
	public void draw(Canvas canvas) {
		for(Background bg : backgrounds){
			bg.draw(canvas);
		}
	}

	@Override
	public void update() {
		for(Background bg : backgrounds){
			bg.update();
		}
	}
}

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

import android.graphics.Canvas;

/**
 * This is the base class of everything witch is able to be rendered by an Renderer
 * @author Dominic Bartl
 */

public abstract class Renderable {

		//identify
		public String id;
		public String name;
	
        // Position.
        public double x;
        public double y;
        public double z;
        
        // Anchor
        public double anchorX = 0;
        public double anchorY = 0;

        // Velocity.
        public int velocityX;
        public int velocityY;
        public int velocityZ;
        
        //Acceleration
        public double accelerationX = 1;
        public double accelerationY = 1;
        public double accelerationZ = 1;

        // Size.
        public int width;
        public int height;
        
        public void draw(Canvas canvas){}
        
        public void setAnchor(int x,int y){
        	anchorX = x;
        	anchorY = y;
        }
        
    	public boolean pointOn(int x, int y){
			return (x > this.x && y > this.y && x < this.x+this.width && y < this.y+this.height);
	}
        
        public void update(){
        	x+=velocityX;
            y+=velocityY;
            z+=velocityZ;
                
            velocityX*=accelerationX;
            velocityY*=accelerationY;
            velocityZ*=accelerationZ;
        }
}
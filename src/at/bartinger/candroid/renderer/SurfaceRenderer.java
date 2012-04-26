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

package at.bartinger.candroid.renderer;

import java.util.ArrayList;
import java.util.Iterator;

import android.graphics.Canvas;
import android.graphics.Color;
import at.bartinger.candroid.background.Background;
import at.bartinger.candroid.background.ColoredBackground;
import at.bartinger.candroid.renderable.Renderable;


public class SurfaceRenderer implements Renderer {

	private ArrayList<Renderable> renderables = new ArrayList<Renderable>();
	private Background background = new ColoredBackground(Color.green(100));

	public void setRenderables(ArrayList<Renderable> renderables) {
		this.renderables = renderables;
	}

	public void addRenderable(Renderable r) {
		synchronized(renderables){
			renderables.add(r);
		}
	}
	
	public void addRenderableAt(Renderable r, int loc) {
		synchronized(renderables){
			renderables.add(loc, r);
		}
	}

	public void removeRenderable(int location){
		synchronized(renderables){
			renderables.remove(location);
		}
	}
	
	public int getRenderableSize(){
		return renderables.size();
	}

	public void removeRenderable(Renderable r){
		synchronized(renderables){
			for(Iterator it = this.renderables.iterator();it.hasNext();)
			{
				Renderable rd = (Renderable)it.next();
				if(rd == r){
					it.remove();
				}
			}
		}
	}
	
	public Renderable getRenderable(String id){
		for(Renderable r:renderables)
		{
			if(r.id == id) return r;
		}
		return null;
	}
	
	//only float to his own type
	public void floatRenderable(Renderable r){
		synchronized(renderables){
			int idx = 0,pos = 0;
			for(Renderable rd:renderables)
			{
				if(r.getClass().equals(rd.getClass())){
					pos = idx;
				}
				idx++;
			}
			renderables.remove(r);
			renderables.add(pos, r);	
		}
	}
	
	public Renderable searchRenderable(int x,int y,Class cls){
		for(Renderable r:renderables)
		{
			if(r.pointOn(x, y) && r.getClass().equals(cls))	return r;
		}
		return null;
	}

	public void drawFrame(Canvas canvas) {

		canvas.drawColor(Color.BLACK);
		background.draw(canvas);
		background.update();
		synchronized(renderables){
			for(Iterator it = this.renderables.iterator();it.hasNext();)
			{
				Renderable r = (Renderable)it.next();
				r.update();
				r.draw(canvas);
			}
		}
	}


	public void setBackground(Background bg){
		background = bg;
	} 



}

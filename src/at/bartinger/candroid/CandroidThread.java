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

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import at.bartinger.candroid.renderer.Renderer;

/**
 * A generic Canvas rendering Thread. Delegates to a Renderer instance to do
 * the actual drawing.
 * @author Dominic Bartl
 *
 */
public class CandroidThread extends Thread {
	private boolean mDone;

	private Renderer mRenderer;
	private Runnable mEvent;
	private SurfaceHolder mSurfaceHolder =null;
	private CandroidSurfaceView view;
	private Canvas canvas;

	public boolean hasSurface = false;

	/**
	 * 
	 * @param view The View on witch  gets drawn onto.
	 * @param renderer The Renderer who draws.
	 */
	public CandroidThread(CandroidSurfaceView view, Renderer renderer) {
		super();
		mDone = false;

		mRenderer = renderer;
		this.view = view;
		mSurfaceHolder = view.getHolder();
		setName("CandroidThread");
	}

	@Override
	public void run() {


		/*
		 * This is our main activity thread's loop, we go until
		 * asked to quit.
		 */
		while (!mDone) {
			
			synchronized (this) {
				if(!hasSurface) {
					while (!hasSurface) {
						try {
							wait();
						} catch (InterruptedException e) {}
					}
				}

				if (mDone) {
					break;
				}

			}

			if (mEvent != null) {
				mEvent.run();
			}




			synchronized(mSurfaceHolder){

				canvas = mSurfaceHolder.lockCanvas();

				if (canvas != null) {
					view.onUpdate();
					view.onStartDrawing(canvas);
					mRenderer.drawFrame(canvas);
					view.onStopDrawing(canvas);
					mSurfaceHolder.unlockCanvasAndPost(canvas);
					
				}else{
					Log.e(Constants.LOGTAG, "Canvas are null");
				}
			}
		}
	}

	/**
	 * Override this for your updates in your game.
	 *              calculating, sprite-moving, animation updates, background updates, ...
	 * @param pastTime The time between the last update and now
	 */


	//don't call this from CanvasThread thread or it is a guaranteed deadlock!
	/**
	 * Kills the thread when he finished his running turn.
	 */
	public void requestExitAndWait() {
		synchronized(this) {
			mDone = true;
			notify();
		}
		try {
			join();
		} catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * Queue an "event" to be run on the rendering thread.
	 * @param r the runnable to be run on the rendering thread.
	 */
	public void setSecondRunnable(Runnable r) {
		synchronized(this) {
			mEvent = r;
		}
	}

	/**
	 * Clears the second runnable which runs on the thread
	 */
	public void clearSecondRunnable() {
		synchronized(this) {
			mEvent = null;
		}
	}

	/**
	 * Gets called when the Surface is created
	 */
	public void surfaceCreated() {
		synchronized(this) {
			hasSurface = true;
			notify();
		}
	}

	
	/**
	 * Gets called when the Surface gets destroyed
	 */
	public void surfaceDestroyed() {
		synchronized(this) {
			mDone = false;
			hasSurface = false;
			notify();
		}
	}





}
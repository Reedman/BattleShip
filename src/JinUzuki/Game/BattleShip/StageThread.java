/**
 * 
 */
package JinUzuki.Game.BattleShip;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import at.bartinger.candroid.Constants;
import at.bartinger.candroid.renderer.Renderer;

/**
 * @author JinUzuki
 *
 */
public class StageThread extends Thread {

	private boolean mDone;
	private boolean mCache;
	private boolean mPause;

	private Renderer mRenderer;
	private Runnable mEvent;
	private SurfaceHolder mSurfaceHolder =null;
	private StageView view;
	private Canvas canvas;

	public boolean hasSurface = false;
	
	/**
	 * 
	 */
	public StageThread(StageView view, Renderer renderer, String name) {
		super();
		mDone = false;
		mCache = false;
		mPause = false;

		mRenderer = renderer;
		this.view = view;
		mSurfaceHolder = view.getHolder();
		setName(name);
	}
	
	

	@Override
	public void run() {

		/*
		 * This is our main activity thread's loop, we go until
		 * asked to quit.
		 */
		while (!mDone) {
			
			synchronized (this) {

				if(mPause == true){
					//Log.e("trend",this.getName() + " is Paused");
					//continue;
					try {
						wait();
					} catch (InterruptedException e) {}
				}
				
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
				
				//Log.e(this.getName(), "Thread is running");

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
					//Log.e(Constants.LOGTAG, "Canvas are null");
				}
			}
		}
		
		//Log.e(Constants.LOGTAG, "Thread finish");
	}
	
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
	
	public void pause(){
		synchronized(this) {
			mPause = true;
		}
	}

	public void restart(){
		synchronized(this) {
			mPause = false;
			this.notify();
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

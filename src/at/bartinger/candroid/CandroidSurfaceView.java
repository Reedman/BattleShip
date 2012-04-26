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

import android.content.Context;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import at.bartinger.candroid.renderer.Renderer;

/**
 * CandroidSurfaceView is the base View you can use this for your game.
 * Create a new Class and extend it from CandroidSurfaceView.
 * For instance: public class MyGameView extends CandroidSurfaceView
 * @author Dominic Bartl
 *
 */

public class CandroidSurfaceView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder mHolder;
	public CandroidThread mCanvasThread;
	public int FPS,FPScount,allFPS,avgFPS = 0;

	//Touchscreen
	public boolean doTouch = false;
	public float touchPressure = 0;

	//Display
	public DisplayMetrics displayMetrics = null;
	public int SCREEN_WIDTH = 0;
	public int SCREEN_HEIGHT = 0;
	public float SCREEN_XDPI = 0;
	public float SCREEN_YDPI = 0;

	/**
	 * 
	 * @param context The application context
	 */
	public CandroidSurfaceView(Context context) {
		super(context);


		setFocusable(true);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

		//You cant use it. There wont be any information in the DisplayMetrics Object.
		displayMetrics = new DisplayMetrics();
	}

	/**
	 * 
	 * @param context The application context
	 * @param dm The screen specified DisplayMetrics use getDisplayMetrics(); from the CandroidActivity
	 */

	public CandroidSurfaceView(Context context, DisplayMetrics dm) {
		super(context);


		setFocusable(true);
		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_NORMAL);

		this.displayMetrics = dm;
		SCREEN_HEIGHT = displayMetrics.heightPixels;
		SCREEN_WIDTH = displayMetrics.widthPixels;
		SCREEN_XDPI = displayMetrics.xdpi;
		SCREEN_YDPI = displayMetrics.ydpi;

	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		int pressure = (int) (event.getPressure()*100);

		if(event.getAction() == MotionEvent.ACTION_DOWN){
			doTouch = true;
			onTouchDown(x,y,pressure);
			return true;
		}else if(event.getAction() == MotionEvent.ACTION_MOVE){
			onTouchMove(x,y,pressure);
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			doTouch = false;
			onTouchUp(x,y,pressure);
			return super.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	/**
	 * onTouchDown get called when the user touches the screen.
	 * @param touchX The x coordinate of the finger
	 * @param touchY The y coordinate of the finger
	 * @param pressure The size of the finger
	 */
	public void onTouchDown(int touchX, int touchY, int pressure){}

	/**
	 * 
	 * onTouchMove get called when the user moves the finger on the screen.
	 * @param touchX The x coordinate of the finger
	 * @param touchY The y coordinate of the finger
	 * @param pressure The size of the finger
	 */
	public void onTouchMove(int touchX, int touchY, int pressure){}

	/**
	 * onTouchUp get called when the user releases the screen with the finger.
	 * @param touchX The x coordinate of the finger
	 * @param touchY The y coordinate of the finger
	 * @param pressure The size of the finger
	 */
	public void onTouchUp(int touchX, int touchY, int pressure){}

	@Override
	public boolean onTrackballEvent(MotionEvent event) {

		return super.onTrackballEvent(event);
	}

	/**
	 * onTrackballLeft get called when the user moves the trackball left.
	 */
	public void onTrackballLeft(){}
	/**
	 * onTrackballRight get called when the user moves the trackball right.
	 */
	public void onTrackballRight(){}
	/**
	 * onTrackballUp get called when the user moves the trackball up.
	 */
	public void onTrackballUp(){}
	/**
	 * onTrackballDown get called when the user moves the trackball down.
	 */
	public void onTrackballDown(){}
	/**
	 * onTrackballPress get called when the user presses the trackball.
	 */
	public void onTrackballPress(){}

	/**
	 * onDPadLeft get called when the user press the left button on the D-Pad
	 */
	public void onDPadLeft(){}

	/**
	 * onDPadRight get called when the user press the right button on the D-Pad
	 */
	public void onDPadRight(){}

	/**
	 * onDPadUp get called when the user press the up button on the D-Pad
	 */
	public void onDPadUp(){}

	/**
	 * onDPadDown get called when the user press the down button on the D-Pad
	 */
	public void onDPadDown(){}

	/**
	 * onDPadCenter get called when the user press the center button on the D-Pad
	 */
	public void onDPadCenter(){}

	/**
	 * onHomeButton get called when the user presses the Home button
	 */
	public void onHomeButton(){}

	/**
	 * onBackButton get called when the user presses the Back button
	 */
	public void onBackButton(){}

	/**
	 * onSearchButton get called when the user presses the Search button
	 */
	public void onSearchButton(){}

	/**
	 * onMenuButton get called when the user presses the Menu button
	 */
	public void onMenuButton(){}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		switch (keyCode){
		case KeyEvent.KEYCODE_DPAD_UP:
			onDPadUp();
			onTrackballUp();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			onDPadDown();
			onTrackballDown();
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			onDPadLeft();
			onTrackballLeft();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			onDPadRight();
			onTrackballRight();
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			onDPadCenter();
			onTrackballPress();
			break;
		case KeyEvent.KEYCODE_HOME:
			onHomeButton();
			break;
		case KeyEvent.KEYCODE_BACK:
			onBackButton();
			break;
		case KeyEvent.KEYCODE_SEARCH:
			onSearchButton();
			break;
		case KeyEvent.KEYCODE_MENU:
			onMenuButton();
			break;
		}
		return super.onKeyDown(keyCode, event);
	}


	public void onDestroy(){

	}


	/**
	 * Here you update, calculate and move everything you need
	 * deltaTime is the time between the last update and now
	 * @param deltaTime
	 */
	public void onUpdate(){}


	/**
	 * Returns the SurfaceHolder where the Renderer draws onto.
	 * @return A SurfaceHolder
	 */
	public SurfaceHolder getSurfaceHolder() {
		return mHolder;
	}


	/**
	 * Sets the user's renderer and kicks off the rendering thread.
	 * @param renderer A Object which extends the base Renderer Class
	 */
	public void setRendererAndStart(Renderer renderer) {

		mCanvasThread = new CandroidThread(this, renderer);
		mCanvasThread.start();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(mCanvasThread != null){
			mCanvasThread.surfaceCreated();
		}
	}

	public void stopGame(){
		mCanvasThread.requestExitAndWait();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return
		onDestroy();
		mCanvasThread.requestExitAndWait();
		mCanvasThread.surfaceDestroyed();
		mHolder.removeCallback(this);

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
		onWindowResize(w, h);
	}

	/**
	 * This get called when the screen rotates
	 * @param width The new width of the screen
	 * @param height The new height of the screen
	 */
	public void onWindowResize(int width, int height){}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		//mCanvasThread.onWindowFocusChanged(hasFocus);
	}

	/**
	 * Set a Thread to be run on the rendering thread.
	 * @param r The runnable to be run on the rendering thread.
	 */
	public void setSecondRunnable(Runnable r) {
		mCanvasThread.setSecondRunnable(r);
	}

	/** Clears the runnable event, if any, from the rendering thread. */
	public void clearSecondRunnable() {
		mCanvasThread.clearSecondRunnable();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mCanvasThread.requestExitAndWait();
	}


	public void stopDrawing() {
		mCanvasThread.requestExitAndWait();
	}

	/**
	 * This is called when the View is starts with drawing
	 * Override this for your purposes!
	 * @param canvas draw with this Canvas
	 */
	public void onStartDrawing(Canvas canvas){}

	/**
	 * This is called when the View is finished with drawing
	 * Override this for your purposes!
	 * @param canvas draw with this Canvas
	 */
	public void onStopDrawing(Canvas canvas){}

	/**
	 * Override this and and and recycle all Textures and Sprites in it
	 */
	public void recycle(){}



}
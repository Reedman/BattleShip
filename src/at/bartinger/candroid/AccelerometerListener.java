package at.bartinger.candroid;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.content.Context;
import java.lang.UnsupportedOperationException;

/**
 * Use the AccelerometerListener to detect the motion of the phone
 * @author Dominic Bartl
 *
 */

public class AccelerometerListener implements SensorEventListener 
{
	public int FORCE_THRESHOLD = 800;//350
	public int TIME_THRESHOLD = 50;//100
	public int SHAKE_TIMEOUT = 250;//500
	public int SHAKE_DURATION = 300;//1000
	public int SHAKE_COUNT = 2;//3

	private SensorManager mSensorMgr;
	private float mLastX=-1.0f, mLastY=-1.0f, mLastZ=-1.0f;
	private long mLastTime;
	private AccelerationListener mShakeListener;
	private Context mContext;
	private int mShakeCount = 0;
	private long mLastShake;
	private long mLastForce;
	
	public interface AccelerationListener
	{
		public void onShake();
		public void onAccelerometerChange(float x, float y, float z);
		public void onAccuracyChanged(Sensor sensor, int accuracy);
	}

	/**
	 * This is the Listener for the Accelerometer
	 * Three Methods get called from here.
	 * onShake();
	 * onAccelerometerChange(float x, float y, float z);
	 * onAccuracyChanged(Sensor sensor, int accuracy);
	 * @param context The application context
	 * @param listener An AccelerationListener-Object from you
	 */
	public AccelerometerListener(Context context,AccelerationListener listener) 
	{ 
		mContext = context;
		resume();
		mShakeListener = listener;
	}
	
	/**
	 * If you have stoped the listening for the Accelerometer, you can resume it with resume();
	 */
	public void resume() {
		mSensorMgr = (SensorManager)mContext.getSystemService(Context.SENSOR_SERVICE);
		if (mSensorMgr == null) {
			throw new UnsupportedOperationException("Sensors not supported");
		}
		boolean supported = mSensorMgr.registerListener(this, mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) ,SensorManager.SENSOR_DELAY_GAME);
		if (!supported) {
			mSensorMgr.unregisterListener(this);
			throw new UnsupportedOperationException("Accelerometer not supported");
		}
	}

	/**
	 * Call the stop(); to unregister the Listener
	 */
	public void stop() {
		if (mSensorMgr != null) {
			mSensorMgr.unregisterListener(this);
			mSensorMgr = null;
		}
	}

	
	/**
	 * DONT OVERRIDE THIS. Use the AccelerationListener for your purposes.
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		mShakeListener.onAccuracyChanged(sensor, accuracy);
	}

	/**
	 * DONT OVERRIDE THIS. Use the AccelerationListener for your purposes.
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		mShakeListener.onAccelerometerChange(event.values[SensorManager.DATA_X], event.values[SensorManager.DATA_Y], event.values[SensorManager.DATA_Z]);
		long now = System.currentTimeMillis();

		if ((now - mLastForce) > SHAKE_TIMEOUT) {
			mShakeCount = 0;
		}

		if ((now - mLastTime) > TIME_THRESHOLD) {
			long diff = now - mLastTime;
			float speed = Math.abs(event.values[SensorManager.DATA_X] + event.values[SensorManager.DATA_Y] + event.values[SensorManager.DATA_Z] - mLastX - mLastY - mLastZ) / diff * 10000;
			if (speed > FORCE_THRESHOLD) {
				if ((++mShakeCount >= SHAKE_COUNT) && (now - mLastShake > SHAKE_DURATION)) {
					mLastShake = now;
					mShakeCount = 0;
					if (mShakeListener != null) { 
						mShakeListener.onShake(); 
					}
				}
				mLastForce = now;
			}
			mLastTime = now;
			mLastX = event.values[SensorManager.DATA_X];
			mLastY = event.values[SensorManager.DATA_Y];
			mLastZ = event.values[SensorManager.DATA_Z];
		}

	}

}


package com.rothen.rbtimer.Service;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEventListener;

/**
 * Created by apest on 15/03/2017.
 */

public class SensorManagement implements SensorEventListener {

    public interface SensorListener
    {
        void onStrongMouvement();
        void onWeakMouvement();
        void onNonSignificantMovement();
    }


    private SensorManager sensorManager;
    private Sensor sensor;

    private float[] gravity = new float[3];
    private float[] linearAcc = new float[3];
    private float[] prevLinearAcc = new float[3];

    private Context context;
    private SensorListener listener;




    public SensorManagement(Context context, SensorListener listener)
    {
        this.context = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        this.listener = listener;
    }

    public void onPause()
    {
        sensorManager.unregisterListener(this,sensor);
    }

    public void onResume()
    {
        sensorManager.registerListener(this,sensor,SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            // alpha is calculated as t / (t + dT)
            // with t, the low-pass filter's time-constant
            // and dT, the event delivery rate

            float alpha = 0.8f;

            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            prevLinearAcc[0]=linearAcc[0];
            prevLinearAcc[1]=linearAcc[1];
            prevLinearAcc[2]=linearAcc[2];

            linearAcc[0] = event.values[0] - gravity[0];
            linearAcc[1] = event.values[1] - gravity[1];
            linearAcc[2] = event.values[2] - gravity[2];


            for(int i = 0 ; i < 3 ; i++)
            {
                if (linearAcc[i] > 9)
                {
                    listener.onStrongMouvement();
                    break;
                }
                else if(linearAcc[i] > 5)
                {
                    listener.onWeakMouvement();
                    break;
                }
                else {
                    listener.onNonSignificantMovement();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }
}

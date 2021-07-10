package com.gravity.sensorapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.DatabaseUtils;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.gravity.sensorapplication.databinding.ActivityMainBinding;

import java.sql.Connection;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor mSensor;
    private Activity activity;
    private ActivityMainBinding binding;
    private MyServiceConnection connection;
    private ISensorAidlInterface anInterface;
    private float valueX, valueY, valueZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
//        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        sensorManager.registerListener(MainActivity.this, mSensor, 8);

        doBindService();
    }

    private void shareData() {

        try {
            anInterface.getSeonsorData(valueX, valueY, valueZ);
        }catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    private void doBindService() {

        connection = new MyServiceConnection();

        Intent i = new Intent();
        i.setClassName("com.gravity.sensorapplication", com.gravity.sensorapplication.MyService.class.getName());

        bindService(i, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR)
        {
            valueX = sensorEvent.values[0];
            valueY = sensorEvent.values[1];
            valueZ = sensorEvent.values[2];
            binding.txtView.setText(sensorEvent.values[0]+"\n"+sensorEvent.values[1]+"\n"+sensorEvent.values[2]);
        }

        if (anInterface != null){
            shareData();
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        connection = null;
    }

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                anInterface = ISensorAidlInterface.Stub.asInterface(iBinder);
            Toast.makeText(activity, "Connected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
                anInterface = null;
            Toast.makeText(activity, "Disconnected", Toast.LENGTH_SHORT).show();
        }
    }
}
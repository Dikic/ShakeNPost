package com.dile.shakenpost;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnDragListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Settings extends Activity implements SensorEventListener{
float X,Y,Z;
private SensorManager mSensorManager;
private Sensor mSensor;
private SeekBar sekBar;
private int lastDiff;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		X=Y=Z=0;
		sekBar=(SeekBar) findViewById(R.id.sBar);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	 	mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mSensor,SensorManager.SENSOR_DELAY_NORMAL);
		lastDiff=10;
		sekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				if(fromUser){
					lastDiff=progress;
				}
			}
		});
	}
public void onClick(View v){
	switch (v.getId()) {
	
	}
	
}
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			float[] prom = arg0.values;
			float x = prom[0];
			float y = prom[1];
			float z = prom[2];

			double dif = Math.sqrt(Math.pow(X - x,2)+Math.pow(Y - x,2)+Math.pow(Z - x,2));
//			 Log.i("test", dif + "");
			if(dif<100&&lastDiff<dif){
				sekBar.setProgress((int) dif);
				lastDiff=(int) dif;
			}
			
			Y=y;
			Z=z;
			X = x;
		}
	}

}

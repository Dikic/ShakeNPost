package com.dile.shakenpost;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements SensorEventListener {
	
	
	Facebook fb;
	String user;
	static String path;
	static double Red,Green,Blue;
	
	SharedPreferences sp;
	static ImageView slika;
	
	ImageButton btn;
	RelativeLayout relL;
	Intent intent;
	Bitmap img;
	static Uri url;
	static int efectNum;
	private SensorManager mSensorManager;
	private Sensor mSensor;
	static final int imgcode = 0;
	static boolean isactive=false;
	boolean isloged=true,rotate=false;
	float X,Y,Z;
	AsyncTask<String, Integer, byte[]> runner;
	boolean hasLoged=false;
	static Bitmap bmp;
	String access_token;
	long exp;
	
	@SuppressWarnings("deprecation")
	public void initialize(){
		
		
		
		btn=(ImageButton) findViewById(R.id.btnSh);
        relL=(RelativeLayout) findViewById(R.id.rl);
      
         slika=(ImageView) findViewById(R.id.img);
         
         mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
 		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
         efectNum=0;
         String APP_ID=getString(R.string.APP_ID);
         fb= new Facebook(APP_ID);
         bmp=null;
        Red=Green=Blue=1;
         X=0;
         sp=getPreferences(MODE_PRIVATE);
         access_token=sp.getString("access_token", null);
         exp=sp.getLong("access_expire", 0);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.activity_main);
        

        initialize();
         logiranje();
         
        btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		    	
				ContentValues values = new ContentValues();
				values.put(MediaStore.Images.Media.TITLE, "Shake n' post");
				Uri mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
				Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
				startActivityForResult(intentPicture,imgcode);
			}
		});  
    }

    public	static void updateImg() {
		// TODO Auto-generated method stub
		if(isactive){
			if(efectNum==1){
				slika.setImageBitmap(ImageEffects.createSepiaToningEffect(bmp, 2 , 30, 20, 10,1));
			}else if(efectNum==2){
				slika.setImageBitmap(ImageEffects.Greyscale3(bmp));
			}else if(efectNum==3){
				slika.setImageBitmap(ImageEffects.ColorFilter(bmp, Red, Green, Blue,1));
			}else if(efectNum==4){
				slika.setImageBitmap(ImageEffects.decreaseColorDepth(bmp, 50,1));
			}else if(efectNum==6){
				slika.setImageBitmap(ImageEffects.bomb(bmp,1));
			}else{
				slika.setImageBitmap(bmp);
			}
		} 
	}
	
	@SuppressWarnings("deprecation")
	private void logiranje() {
		// TODO Auto-generated method stub
		if(access_token!=null){
        	fb.setAccessToken(access_token);
        }else isloged=false;
        if(exp!=0){
        	fb.setAccessExpires(exp);
        }else isloged=false;
        
        if(!isloged){
        
    	if(!fb.isSessionValid()){
        	fb.authorize(MainActivity.this,new String[]{"photo_upload","publish_stream"}, new DialogListener() {
				
				@Override
				public void onFacebookError(FacebookError e) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "Facebook error", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onError(DialogError e) {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onComplete(Bundle values) {
					// TODO Auto-generated method stub
					Editor editor=sp.edit();
					editor.putString("access_token", fb.getAccessToken());
					editor.putLong("access_expire", fb.getAccessExpires());
					editor.commit();
					Toast.makeText(MainActivity.this, "You are loged in!", Toast.LENGTH_SHORT).show();
				}
				
				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
					Toast.makeText(MainActivity.this, "Action canceld", Toast.LENGTH_SHORT).show();
				}
			});
    	}}else return;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
    	if (requestCode == imgcode && resultCode==RESULT_OK) {
		url=data.getData();
		path = getRealPathFromURI(url);
		bmp=ImageEffects.Rotate(BitmapFactory.decodeFile(path), 5);
		isactive = true;
		 btn.setVisibility(Button.INVISIBLE);
		updateImg();
		onResume();
    	}else{
    		//dialog listner
    		fb.authorizeCallback(requestCode, resultCode, data);
    	}
    	
	}
	
	@SuppressWarnings("deprecation")
	private String getRealPathFromURI(Uri contentUri) {
		try
		{
			String[] proj = {MediaStore.Images.Media.DATA};
			Cursor cursor = managedQuery(contentUri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}catch (Exception e)
		{
        return contentUri.getPath();
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this, mSensor);
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
//		isSlikano=true;
		if (isactive)
			mSensorManager.registerListener(this, mSensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		super.onResume();
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.imgEff:
			startActivity(new Intent("com.dile.shakenpost.MENI"));
			return true;
		case R.id.singIn:
			logout();
			return true;
		case R.id.setting:
			startActivity(new Intent("com.dile.shakenpost.SETTINGS"));
			return true;	
		}
		return super.onOptionsItemSelected(item);
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
			if (X != 0 && dif > 20) {
				mSensorManager.unregisterListener(this, mSensor);
				postiraj();
			}
			Y=y;
			Z=z;
			X = x;
		}
	}
	
	private void postiraj() {
		// TODO Auto-generated method stub
		ConnectivityManager cm =(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		if(activeNetwork==null||!activeNetwork.isConnected()){
			Toast.makeText(this, "Conection problems!", Toast.LENGTH_SHORT).show();
			Vibrator vb=(Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			vb.vibrate(200);
			Timer tm=new Timer();
			tm.schedule(new TimerTask() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mSensorManager.registerListener(MainActivity.this, mSensor,SensorManager.SENSOR_DELAY_FASTEST);
				}
			}, 2500);
			hasLoged=true;
			return;
		}
		if(hasLoged){
			logiranje();
			hasLoged=false;
		}
		isactive=false;
		X=Y=Z=0;
		runner=new BackgrdTask(this).execute(path);
		bmp=null;
//		}
		
	}
	//on click e za login out
	public void logout() {
		
		Thread t=new Thread(new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				Editor editor=sp.edit();
				editor.putString("access_token", null);
				editor.putLong("access_expire", 0 );
				editor.commit();
					try {
						fb.logout(MainActivity.this.getBaseContext());
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(IllegalArgumentException ilae){
						ilae.printStackTrace();
					}finally{logiranje();}
				
			}
		});
		
		t.start();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		AlertDialog.Builder dialog=new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
		dialog.setMessage("Are you sure?");
		dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MainActivity.this.finish();
			}
		});
		dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.create();
		dialog.show();
//		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this, mSensor);
		efectNum=0;
		isactive=false;
		bmp=null;
		X=Y=Z=0;
		super.onDestroy();
	}
  
}

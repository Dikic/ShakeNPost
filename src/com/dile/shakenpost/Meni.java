package com.dile.shakenpost;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.provider.MediaStore.Audio;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Meni extends Activity{
float Y;
ImageView slika;
RelativeLayout ekran;
float dist=0;
AudioManager am;
int imgEff;
//MediaPlayer mp;
double Cr,Cg,Cb;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.imageff);
		Y=0;
		imgEff=MainActivity.efectNum;
		Cr=MainActivity.Red;
		Cg=MainActivity.Green;
		Cb=MainActivity.Blue;
		am=(AudioManager) getSystemService(Context.AUDIO_SERVICE);
//		mp=new MediaPlayer();
//		mp=MediaPlayer.create(this, R.raw.krk);
//		try {
//			mp.prepare();
////			mp.prepareAsync();
//			
//		} catch (IllegalStateException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		slika=(ImageView) findViewById(R.id.vrti);
		ekran=(RelativeLayout) findViewById(R.id.touch);
		ekran.setOnTouchListener(new View.OnTouchListener() {
			
			@SuppressLint("NewApi")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					Y=event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					float y=event.getY();
					slika.setRotation(dist);
					am.playSoundEffect(AudioManager.FX_KEY_CLICK);
					
//						mp.start();
					
					
					dist+=(Y-y);
					dist%=360;
					Y=y;
					Log.d("***MOVE**", "|"+Y+" | "+y+" | "+dist+" | ");
					break;
				case MotionEvent.ACTION_UP:
					if(dist<0)dist+=360;
					setGoodRotation(dist);
					break;
				}
				
				return true;
			}
		});
	}
	@SuppressLint("NewApi")
	protected void setGoodRotation(float dist2) {
		// TODO Auto-generated method stub
		Log.e("***DIST2***", ""+dist2);
		if(dist2<=30||dist2>330){
			slika.setRotation(0);
			dist=0;//sepia
			MainActivity.efectNum=1;
		}else if(dist2<=90&&dist2>30){
			slika.setRotation(60);
			dist=60;//blue
			MainActivity.efectNum=3;
			MainActivity.Blue=1;
			MainActivity.Green=0.5;
			MainActivity.Red=0.5;
		}else if(dist2<=150&&dist2>90){
			slika.setRotation(120);
			dist=120;//green
			MainActivity.efectNum=3;
			MainActivity.Blue=0.5;
			MainActivity.Green=1;
			MainActivity.Red=0.5;
		}else if(dist2<=210&&dist2>150){
			slika.setRotation(180);
			dist=180;//red
			MainActivity.efectNum=3;
			MainActivity.Blue=0.5;
			MainActivity.Green=0.5;
			MainActivity.Red=1;
		}else if(dist2<=270&&dist2>210){
			slika.setRotation(240);
			dist=240;//depth
			MainActivity.efectNum=4;
		}else if(dist2<=330&&dist2>270){
			slika.setRotation(300);
			dist=300;//gray
			MainActivity.efectNum=2;
		}
//		MainActivity.updateImg();
	}
	
public void doClick(View v){
	switch (v.getId()) {
	case R.id.cancelbtn:
		MainActivity.efectNum=imgEff;
		MainActivity.Red=Cr;
		MainActivity.Red=Cg;
		MainActivity.Red=Cb;
		finish();
		break;
	case R.id.okbtn:
		MainActivity.updateImg();
		finish();
		break;
	}
}
}

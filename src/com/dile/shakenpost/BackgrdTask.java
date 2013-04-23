package com.dile.shakenpost;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class BackgrdTask extends AsyncTask<String, Integer, byte[]> {
	MainActivity act;
	ProgressDialog progres;
	public BackgrdTask(MainActivity act) {
	super();
	this.act = act;
	}
	
	@Override
	protected byte[] doInBackground(String... params) {
		// TODO Auto-generated method stub
		Bitmap bmp=null;
		Bitmap bitmap=BitmapFactory.decodeFile(params[0]);
		if(act.efectNum==1){
			bmp=ImageEffects.createSepiaToningEffect(bitmap,3 , 30, 20, 10,1);
		}else if(act.efectNum==2){
			bmp=ImageEffects.Greyscale3(bitmap);
		}else if(act.efectNum==3){
			bmp=ImageEffects.ColorFilter(bitmap, act.Red, act.Green, act.Blue,1);
		}else if(act.efectNum==4){
			bmp=ImageEffects.decreaseColorDepth(bitmap, 50,1);
		}else if(act.efectNum==6){
			bmp=ImageEffects.bomb(bitmap,1);
		}
		bmp=ImageEffects.Rotate(bitmap, 1);
		
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		
		bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte []data=baos.toByteArray();
		
		try {
			String name="shakeNpost-"+params[0].substring(params[0].lastIndexOf("/")+1);
			File picFile=new File(Environment.getExternalStorageDirectory().toString()+"/shakeNpost/");
			picFile.mkdir();
			File file=new File(picFile,name);
			file.createNewFile();
			FileOutputStream fo=new FileOutputStream(file);
			fo.write(data, 0, data.length);
			fo.flush();
			fo.close();
			MediaScannerConnection.scanFile(act,
	                new String[] { file.toString() }, null,
	                new MediaScannerConnection.OnScanCompletedListener() {
				@Override
				public void onScanCompleted(String path, Uri uri) {
	                Log.i("ExternalStorage", "Scanned " + path + ":");
	                Log.i("ExternalStorage", "-> uri=" + uri);
	            }
	        });
			Log.d("JKJDF>>", "Izgleda se nesto sacuvalo :/ ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onPostExecute(byte[] result) {
		// TODO Auto-generated method stub
		
		Bundle params = new Bundle();
		params.putString("method", "photos.upload");
		params.putByteArray("picture", result);
		params.putString(Facebook.TOKEN, act.fb.getAccessToken());
		try {
			AsyncFacebookRunner asyncRunner = new AsyncFacebookRunner(act.fb);
			 asyncRunner.request(null, params, "POST", new SampleUploadListener(), null);
			 
		}catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		progres.dismiss();
		act.slika.setImageBitmap(null);
		Toast.makeText(act, "Photo uploaded", Toast.LENGTH_SHORT).show();
		act.btn.setVisibility(Button.VISIBLE);
		act.efectNum=0;
		super.onPostExecute(result);
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		progres=new ProgressDialog(act,ProgressDialog.THEME_HOLO_DARK);
		progres.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progres.setMessage("Your photo is uploading at the moment please wait.");
		progres.show();
		super.onPreExecute();
	}
}

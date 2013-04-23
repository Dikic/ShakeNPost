package com.dile.shakenpost;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;

@SuppressWarnings("deprecation")
public class SampleUploadListener implements RequestListener {

	
	@Override
	public void onComplete(String response, Object state) {
		// TODO Auto-generated method stub
		 try {
	            // process the response here: (executed in background thread)
	            Log.d("Facebook-Example", "Response: " + response.toString());
	            JSONObject json = Util.parseJson(response);
	            final String src = json.getString("src");

	            // then post the processed result back to the UI thread
	            // if we do not do this, an runtime exception will be generated
	            // e.g. "CalledFromWrongThreadException: Only the original
	            // thread that created a view hierarchy can touch its views."

	        } catch (JSONException e) {
	            Log.w("Facebook-Example", "JSON Error in response");
	        } catch (FacebookError e) {
	            Log.w("Facebook-Example", "Facebook Error: " + e.getMessage());
	        }
	}

	@Override
	public void onIOException(IOException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFacebookError(FacebookError e, Object state) {
		// TODO Auto-generated method stub

	}

}

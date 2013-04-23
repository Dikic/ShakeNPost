package com.dile.shakenpost;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;

public class ImageEffects {
	public static Bitmap rotate(Bitmap src, float degree) {
	    // create new matrix
	    Matrix matrix = new Matrix();
	    // setup rotation degree
	    matrix.postRotate(degree);
	 
	    // return new bitmap rotated using matrix
	    return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
	}
	public static Bitmap Rotate(Bitmap src,int quality) {
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(src.getHeight()/quality+1, src.getWidth()/quality+1, src.getConfig());
	    // pixel information
	    int A, R, G, B;
	    int pixel;
	 
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	 
	    // scan through every single pixel
	    for(int x = 0; x < width; x+=quality) {
	        for(int y = 0; y < height; y+=quality) {
	            // get one pixel color
	            pixel = src.getPixel(x, y);
	            // retrieve color of all channels
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // set new pixel color to output bitmap
//	            if((height-y-1)/quality>src.getHeight()/quality) continue;
//	            if(x/quality>src.getWidth()/quality)break;
	            bmOut.setPixel((height-y-1)/quality, x/quality, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	public static Bitmap Greyscale(Bitmap src,int quality) {
	    // constant factors
	    final double GS_RED = 0.299;
	    final double GS_GREEN = 0.587;
	    final double GS_BLUE = 0.114;
	 
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap( src.getWidth()/quality+1, src.getHeight()/quality+1, src.getConfig());
	    // pixel information
	    int A, R, G, B;
	    int pixel;
	 
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	 
	    // scan through every single pixel
	    for(int x = 0; x < width; x+=quality) {
	        for(int y = 0; y < height; y+=quality) {
	            // get one pixel color
	            pixel = src.getPixel(x, y);
	            // retrieve color of all channels
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // take conversion up to one single value
	            R = G = B = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x/quality, y/quality, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	public static Bitmap createSepiaToningEffect(Bitmap src, int depth, double red, double green, double blue, int quality) {
	    // image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width/quality+1,height/quality+1,src.getConfig());
	    // constant grayscale
	    final double GS_RED = 0.3;
	    final double GS_GREEN = 0.59;
	    final double GS_BLUE = 0.11;
	    // color information
	    int A, R, G, B;
	    int pixel;
	 
	    // scan through all pixels
	    for(int x = 0; x < width; x+=quality) {
	        for(int y = 0; y < height; y+=quality) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            // get color on each channel
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            // apply grayscale sample
	            B = G = R = (int)(GS_RED * R + GS_GREEN * G + GS_BLUE * B);
	 
	            // apply intensity level for sepid-toning on each channel
	            R += (depth * red);
	            if(R > 255) { R = 255; }
	 
	            G += (depth * green);
	            if(G > 255) { G = 255; }

	            B += (depth * blue);
	            if(B > 255) { B = 255; }
	 
	            // set new pixel color to output image
	            bmOut.setPixel(x/quality, y/quality, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	public static Bitmap decreaseColorDepth(Bitmap src, int bitOffset,int quality) {
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width/quality+1,height/quality+1, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;
	 
	    // scan through all pixels
	    for(int x = 0; x < width; x+=quality) {
	        for(int y = 0; y < height; y+=quality) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	 
	            // round-off color offset
	            R = ((R + (bitOffset / 2)) - ((R + (bitOffset / 2)) % bitOffset) - 1);
	            if(R < 0) { R = 0; }
	            G = ((G + (bitOffset / 2)) - ((G + (bitOffset / 2)) % bitOffset) - 1);
	            if(G < 0) { G = 0; }
	            B = ((B + (bitOffset / 2)) - ((B + (bitOffset / 2)) % bitOffset) - 1);
	            if(B < 0) { B = 0; }
	 
	            // set pixel color to output bitmap
	            bmOut.setPixel(x/quality, y/quality, Color.argb(A, R, G, B));
	        }
	    }
	 
	    // return final image
	    return bmOut;
	}
	
	public static Bitmap ColorFilter(Bitmap src, double red, double green, double blue,int quality) {
		// image size

	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(src.getWidth()/quality+1, src.getHeight()/quality+1, src.getConfig());
	    // pixel information
	    int A, R, G, B;
	    int pixel;
	 
	    // get image size
	    int width = src.getWidth();
	    int height = src.getHeight();
	 
	    // scan through every single pixel
	    for(int x = 0; x < width; x+=quality) {
	        for(int y = 0; y < height; y+=quality) {
	            // get one pixel color
	            pixel = src.getPixel(x, y);
	            // retrieve color of all channels
	            A = Color.alpha(pixel);
	            R = (int) (Color.red(pixel)*red);
	            G = (int) (Color.green(pixel)*green);
	            B = (int) (Color.blue(pixel)*blue);
	            // take conversion up to one single value
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x/quality, y/quality, Color.argb(A, R, G, B));
	        }
	    }
 
        // return final image
        return bmOut;
    }
	
	public static Bitmap bomb(Bitmap src,int quality) {
	   
	 
		 Bitmap bmOut = Bitmap.createBitmap( src.getWidth()/quality+1, src.getHeight()/quality+1,src.getConfig());
		    // pixel information
		    int A, R, G, B;
		    int pixel;
		    final double GS_RED = 0.299;
		    final double GS_GREEN = 0.587;
		    final double GS_BLUE = 0.114;
		    // get image size
		    int width = src.getWidth();
		    int height = src.getHeight();
		    int c=width+height;
		    // scan through every single pixel
		    for(int x = 0; x < width; x+=quality) {
		        for(int y = 0; y < height; y+=quality) {
		            // get one pixel color
		            pixel = src.getPixel(x, y);
		            // retrieve color of all channels
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);
		            // take conversion up to one single value
//		            R+=c;
//		            G+=R;
//		            B+=G;
//		            c+=B;
//		            c/=(R+G+B+1);
		            double s=(R+G+B)/2.0;
		            c=(int) (Math.sqrt(s*(s-R)*(s-G)*(s-B))+s);
		            R=c/(R+1);
		            B=c/(B+1);
		            G=c/(G+1);
		            if(B>225)B=225;
		            if(G>225)G=225;
		            if(R>225)G=225;
		            // set new pixel color to output bitmap
		            bmOut.setPixel(x/quality, y/quality, Color.argb(A, R, G, B));
		        }
		    }
		// return final image
	    return bmOut;
	}
	
	public static Bitmap Greyscale3(Bitmap src) {
		   
		 
		 Bitmap bmOut = Bitmap.createBitmap(src.getWidth(),src.getHeight(), src.getConfig());
		    // pixel information
		    int A, R, G, B;
		    int pixel;
		    final double GS_RED = 0.7;
		    final double GS_GREEN = 0.222;
		    final double GS_BLUE = 0.333;
		    // get image size
		    int width = src.getWidth();
		    int height = src.getHeight();
		
		    // scan through every single pixel
		    for(int x = 0; x < width; x++) {
		        for(int y = 0; y < height; y++) {
		            // get one pixel color
		            pixel = src.getPixel(x, y);
		            // retrieve color of all channels
		            A = Color.alpha(pixel);
		            R = Color.red(pixel);
		            G = Color.green(pixel);
		            B = Color.blue(pixel);
//		           TIME TO INVESTIGATE
		            bmOut.setPixel(x, y, Color.argb(A, R, G, B));
		           
		            
		        }
		    }
		// return final image
	    return bmOut;
	}
	
	
}
	


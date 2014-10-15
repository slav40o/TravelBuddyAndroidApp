package com.goofy.travelbuddy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Environment;

public class PhoneState {
	public static boolean isNetworkAvailable(Context context) {
		 ConnectivityManager connec =  
                 (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
   
		  if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
		       connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
		       connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
		       connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
		      
		      return true;
		       
		  } else {
			  return false;
		  }
    }
	
	/* Checks if external storage is available for read and write */
	public static boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public static boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
}

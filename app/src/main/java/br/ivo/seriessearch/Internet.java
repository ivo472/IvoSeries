package br.ivo.seriessearch;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Internet {
	public static boolean haveInternet(final Context context, int connection)
	{
	    boolean bNet = true;
	    NetworkInfo info = (NetworkInfo) ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	    if (info == null)
		{
	    	bNet = false;
	    }

	    if (bNet)
		{
	    	if (!info.isConnected())
	    		bNet = false;
	    }
	    
	    int iTypeNet = connection;
	    if (bNet && iTypeNet != 3)
		{
	    	if (info.isRoaming() && iTypeNet != 2)
		    	bNet = false;
	    	else if (info.getType() != iTypeNet)
	    		bNet = false;	    	
	    }
	    
	    return bNet;
	}	
}

package com.meticulus.codinalteparts.app;

/**
 * Created by meticulus on 4/7/14.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.meticulus.codinalteparts.app.FunctionsMain;


public class bootreceiver extends BroadcastReceiver  {

    SharedPreferences sharedPref;
    public void onReceive(Context arg0, Intent arg1)
    {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(arg0.getApplicationContext());

	if(FunctionsMain.glove_mode_is_supported() && sharedPref.getBoolean("glove",false))
	    FunctionsMain.set_glove(true);

	if(FunctionsMain.dt2w_is_supported() && sharedPref.getBoolean("dt2w",false))
	    FunctionsMain.set_dt2w(true);
    }
}

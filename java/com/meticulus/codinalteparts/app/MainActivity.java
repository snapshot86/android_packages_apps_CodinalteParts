package com.meticulus.codinalteparts.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.SystemProperties;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;

import com.meticulus.codinalteparts.app.FunctionsMain;

public class MainActivity extends Activity {

    TextView kernel, workaround, network; /* Headers */

    Switch dt2w, otg, glove, sensorex, /* Kernel */
	    sim2, /* Networking */
	    google_enc; /* Workarounds */

    ImageView whatis_dt2w, whatis_otg, whatis_glove, whatis_sensorex,/* Kernel */
	    whatis_sim2, /* Networking */
	    whatis_google_enc; /* Workarounds */

    LinearLayout otg_layout, glove_layout, sensorex_layout,/* Kernel */
	    sim2_layout, /* Networking */
	    google_enc_layout; /* Workarounds */

    SharedPreferences sharedPref;
    String device =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_main);

        /* Headers */
        kernel = (TextView) findViewById(R.id.kernel_textview);
        network = (TextView) findViewById(R.id.network_textview);
        workaround  = (TextView) findViewById(R.id.workaround_textview);

        /* Assign all switches */
	dt2w = (Switch) findViewById(R.id.switch_dt2w);
        glove = (Switch) findViewById((R.id.switch_glove));
        otg = (Switch) findViewById((R.id.switch_otg));
        sensorex = (Switch) findViewById((R.id.switch_sensorex));
        sim2 = (Switch) findViewById((R.id.switch_sim2));
        google_enc = (Switch) findViewById((R.id.switch_google_enc)); 

        /* Assign all switches onCheckChanged*/
	dt2w.setOnCheckedChangeListener(switchListener); 
        glove.setOnCheckedChangeListener(switchListener);
        otg.setOnCheckedChangeListener(switchListener);
        sensorex.setOnCheckedChangeListener(switchListener);
        sim2.setOnCheckedChangeListener(switchListener);
        google_enc.setOnCheckedChangeListener(switchListener);

	whatis_dt2w = (ImageView) findViewById(R.id.whatis_dt2w);
        whatis_dt2w.setOnClickListener(switchClickListener);

	whatis_glove = (ImageView) findViewById(R.id.whatis_glove);
        whatis_glove.setOnClickListener(switchClickListener);

	whatis_otg = (ImageView) findViewById(R.id.whatis_otg);
        whatis_otg.setOnClickListener(switchClickListener);

	whatis_sensorex = (ImageView) findViewById(R.id.whatis_sensorex);
        whatis_sensorex.setOnClickListener(switchClickListener);

	whatis_sim2 = (ImageView) findViewById(R.id.whatis_sim2);
        whatis_sim2.setOnClickListener(switchClickListener);

	whatis_google_enc = (ImageView) findViewById(R.id.whatis_google_enc);
        whatis_google_enc.setOnClickListener(switchClickListener);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this); 
        prepareUI();
 
    }

    private void prepareUI(){ 
	if(FunctionsMain.usb_host_is_supported())
	    otg.setChecked(FunctionsMain.usb_host_mode_is_on());
	else
	    otg.setClickable(false);

	sim2.setChecked(!SystemProperties.get("persist.radio.multisim.config", "single").equals("single"));

	if(FunctionsMain.glove_mode_is_supported())
	    glove.setChecked(FunctionsMain.glove_mode_is_on());
	else
	    glove.setEnabled(false);
	    
	if(FunctionsMain.dt2w_is_supported())	
		dt2w.setChecked(FunctionsMain.dt2w_is_on());
	else
	    dt2w.setClickable(false);

	google_enc.setChecked(SystemProperties.getBoolean("persist.sys.google_avc_enc",false));
	sensorex.setChecked(SystemProperties.getBoolean("persist.sys.sensorex",false));
    }

    private View.OnClickListener switchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ImageView thisSwitch = (ImageView)view;
            if(thisSwitch == whatis_otg){
                ShowDialog(getResources().getString(R.string.otg_title),FunctionsMain.usb_host_is_supported() ? getString(R.string.otg_desc) : getString(R.string.not_supported));
            }
            else if(thisSwitch == whatis_dt2w){
                ShowDialog(getResources().getString(R.string.dt2w_title),FunctionsMain.dt2w_is_supported() ? getString(R.string.dt2w_desc) : getString(R.string.not_supported));
            }
            else if(thisSwitch == whatis_sensorex){
                ShowDialog(getResources().getString(R.string.sensorex_title),getString(R.string.sensorex_desc));
            }
            else if(thisSwitch == whatis_google_enc){
                ShowDialog(getResources().getString(R.string.google_enc_title),getString(R.string.google_enc_desc));
            }
            else if(thisSwitch == whatis_glove){
                ShowDialog(getResources().getString(R.string.glove_title),FunctionsMain.glove_mode_is_supported() ? getString(R.string.glove_desc) : getString(R.string.not_supported));
            }
            else if(thisSwitch == whatis_sim2){
                ShowDialog(getResources().getString(R.string.sim2_title),getString(R.string.sim2_desc));
            }
        }
    };

    private CompoundButton.OnCheckedChangeListener switchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            Switch thisSwitch = (Switch)compoundButton;
            SharedPreferences.Editor editor = sharedPref.edit();
            if(thisSwitch == otg) {
		try { 
                    	FunctionsMain.set_otg(b);
                }
                catch(Exception e){e.printStackTrace();}
	    }
            else if(thisSwitch == dt2w) {
		try { 
                    	FunctionsMain.set_dt2w(b);
			editor.putBoolean("dt2w",b);
                }
                catch(Exception e){e.printStackTrace();}
	    }
            else if(thisSwitch == glove) {
		try { 
                    	FunctionsMain.set_glove(b);
			editor.putBoolean("glove",b);
                }
                catch(Exception e){e.printStackTrace();}
	    }
            else if(thisSwitch == sensorex){
                SystemProperties.set("persist.sys.sensorex",String.valueOf(b));
            }
            else if(thisSwitch == google_enc){
                SystemProperties.set("persist.sys.google_avc_enc",String.valueOf(b));
            }
            else if(thisSwitch == sim2){
		if(b)
                    SystemProperties.set("persist.radio.multisim.config","dsds");
		else
                    SystemProperties.set("persist.radio.multisim.config","single");

            }
            editor.apply();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public AlertDialog ShowDialog(String title,String message)
    {
        return ShowDialog(title,message,true);
    }
    public AlertDialog ShowDialog(String title,String message, boolean okbtn)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
	if(okbtn)
            builder.setPositiveButton("OK", null);
        AlertDialog dialog = builder.create();
        dialog.show();
	return dialog;
    }

    public ProgressDialog ShowIdProgDialog(String title,String message) {
	ProgressDialog pdialog = new ProgressDialog(this);
	pdialog.setTitle(title);
	pdialog.setMessage(message);
	pdialog.setIndeterminate(true);
	pdialog.setCancelable(false);
	pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	return pdialog;
    }
}

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

    TextView kernel, audio, charger,workaround, network, debugging; /* Headers */

    Switch dt2w, otg, glove, sensorex, /* Kernel */
	    sim2, /* Networking */
	    google_enc, /* Workarounds */
            charger_show_datetime, charger_no_suspend, /* Charger */
            autologcat, autokmsg, autoril; /* Debugging */

    ImageView whatis_dt2w, whatis_otg, whatis_glove, whatis_sensorex,/* Kernel */
	    whatis_sim2, /* Networking */
	    whatis_google_enc, /* Workarounds */
            whatis_charger_show_datetime, whatis_charger_no_suspend, /* Charger */
            whatis_autologcat,whatis_autokmsg, whatis_autorillog; /* Debugging */

    LinearLayout otg_layout, glove_layout, sensorex_layout,/* Kernel */
	    sim2_layout, /* Networking */
	    google_enc_layout, /* Workarounds */
            charger_show_datetime_layout, charger_no_suspend_layout, /* Charger */
            autologcat_layout, autokmsg_layout, autoril_layout; /* Debugging */

    SharedPreferences sharedPref;
    String device =  "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.xml.activity_main);

        /* Headers */
        kernel = (TextView) findViewById(R.id.kernel_textview);
        network = (TextView) findViewById(R.id.network_textview);
        charger = (TextView) findViewById(R.id.charger_textview);
        workaround  = (TextView) findViewById(R.id.workaround_textview);
        debugging = (TextView) findViewById(R.id.debugging_textview);

        /* Assign all switches */
	dt2w = (Switch) findViewById(R.id.switch_dt2w);
        glove = (Switch) findViewById((R.id.switch_glove));
        otg = (Switch) findViewById((R.id.switch_otg));
        sensorex = (Switch) findViewById((R.id.switch_sensorex));
        sim2 = (Switch) findViewById((R.id.switch_sim2));
        google_enc = (Switch) findViewById((R.id.switch_google_enc));
        charger_show_datetime = (Switch) findViewById(R.id.switch_charger_show_datetime);
        charger_no_suspend = (Switch) findViewById(R.id.switch_charger_no_suspend);
        autologcat = (Switch) findViewById(R.id.switch_autologcat);
        autokmsg = (Switch) findViewById(R.id.switch_autokmsg);
        autoril = (Switch)findViewById(R.id.switch_autorillog);

        /* Assign all switches onCheckChanged*/
	dt2w.setOnCheckedChangeListener(switchListener); 
        glove.setOnCheckedChangeListener(switchListener);
        otg.setOnCheckedChangeListener(switchListener);
        sensorex.setOnCheckedChangeListener(switchListener);
        sim2.setOnCheckedChangeListener(switchListener);
        google_enc.setOnCheckedChangeListener(switchListener);
        charger_show_datetime.setOnCheckedChangeListener(switchListener);
        charger_no_suspend.setOnCheckedChangeListener(switchListener);
        autologcat.setOnCheckedChangeListener(switchListener);
        autokmsg.setOnCheckedChangeListener(switchListener);
        autoril.setOnCheckedChangeListener(switchListener);

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

        whatis_charger_show_datetime = (ImageView) findViewById(R.id.what_is_charger_show_datetime);
        whatis_charger_show_datetime.setOnClickListener(switchClickListener);

        whatis_charger_no_suspend = (ImageView) findViewById(R.id.what_is_charger_no_suspend);
        whatis_charger_no_suspend.setOnClickListener(switchClickListener);

        whatis_autologcat = (ImageView) findViewById(R.id.whatis_autologcat);
        whatis_autologcat.setOnClickListener(switchClickListener);

        whatis_autokmsg = (ImageView) findViewById(R.id.whatis_autokmsg);
        whatis_autokmsg.setOnClickListener(switchClickListener);

        whatis_autorillog = (ImageView) findViewById(R.id.whatis_autorillog);
        whatis_autorillog.setOnClickListener(switchClickListener);

        sharedPref = PreferenceManager.getDefaultSharedPreferences(this); 
        prepareUI();
 
    }

    private void prepareUI(){ 
        charger_show_datetime.setChecked(FunctionsMain.getChargerShowDateTime());
        charger_no_suspend.setChecked(FunctionsMain.getChargerNoSuspend());
        autologcat.setChecked(sharedPref.getBoolean("autologcat",false));
        autokmsg.setChecked(sharedPref.getBoolean("autokmsg",false));
        autoril.setChecked(sharedPref.getBoolean("autoril",false));

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
                ShowDialog("USB Host Mode",FunctionsMain.usb_host_is_supported() ? getString(R.string.otg_desc) : getString(R.string.not_supported));
            }
            else if(thisSwitch == whatis_dt2w){
                ShowDialog("Doubletap 2 Wake",FunctionsMain.dt2w_is_supported() ? getString(R.string.dt2w_desc) : getString(R.string.not_supported));
            }
            else if(thisSwitch == whatis_sensorex){
                ShowDialog("SensorsEx",getString(R.string.sensorex_desc));
            }
            else if(thisSwitch == whatis_google_enc){
                ShowDialog("Workaround: Google Encoder",getString(R.string.google_enc_desc));
            }
            else if(thisSwitch == whatis_glove){
                ShowDialog("Touchscreen: Glove Mode",FunctionsMain.glove_mode_is_supported() ? getString(R.string.glove_desc) : getString(R.string.not_supported));
            }
            else if(thisSwitch == whatis_charger_show_datetime){
                ShowDialog("Date and Time in Charger",getString(R.string.charger_showdatetime_desc));
            }
            else if(thisSwitch == whatis_sim2){
                ShowDialog("SIM 2",getString(R.string.sim2_desc));
            }
            else if(thisSwitch == whatis_charger_no_suspend){
                ShowDialog("No Suspend in Charger",getString(R.string.charger_nosuspend_desc));
            }
            else if(thisSwitch == whatis_autologcat){
                ShowDialog("Auto Logcat",getString(R.string.autologcat_desc));
            }
            else if(thisSwitch == whatis_autokmsg){
                ShowDialog("Auto kmsg",getString(R.string.autokmsg_desc));
            }
            else if(thisSwitch == whatis_autorillog){
                ShowDialog("Auto kmsg",getString(R.string.autorillog_desc));
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
            else if(thisSwitch == charger_show_datetime){
                FunctionsMain.setChargerShowDateTime(b);
            }
            else if(thisSwitch == sim2){
		if(b)
                    SystemProperties.set("persist.radio.multisim.config","dsds");
		else
                    SystemProperties.set("persist.radio.multisim.config","single");

            }
            else if(thisSwitch == charger_no_suspend){
                FunctionsMain.setChargerNoSuspend(b);
            } 
            else if(thisSwitch == autologcat){
                if(b != sharedPref.getBoolean("autologcat",false))
                {
                    if(b)
                        ShowDialog("Auto logcat","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto logcat","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autologcat", b);
            }
            else if(thisSwitch == autokmsg){
                if(b != sharedPref.getBoolean("autokmsg",false))
                {
                    if(b)
                        ShowDialog("Auto kmsg","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto kmsg","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autokmsg", b);
            }
            else if(thisSwitch == autoril){
                if(b != sharedPref.getBoolean("autoril",false))
                {
                    if(b)
                        ShowDialog("Auto RIL log","Will be started on the next reboot.");
                    else
                        ShowDialog("Auto RIL log","Will NOT be started on the next reboot. "+
                                "Still running till then");
                }
                editor.putBoolean("autoril", b);
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

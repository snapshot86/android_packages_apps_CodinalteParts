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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.app.Dialog;
import java.lang.Math;
import android.content.DialogInterface;
import com.meticulus.codinalteparts.app.FunctionsMain;

public class LedActivity extends Activity implements DialogInterface.OnDismissListener {

    LinearLayout lowpower, charging, fullpower, notification;
    ImageView whatis_lowpower, whatis_charging, whatis_fullpower,whatis_notification;
    String hexlowpower, hexcharging, hexfullpower, hexnotification;
    LedColorDialog thisdiag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led); 
	setTitle(getString(R.string.led_section_name));

	lowpower = (LinearLayout) findViewById(R.id.led_lowpower_layout);
	charging = (LinearLayout) findViewById(R.id.led_charging_layout);
	fullpower = (LinearLayout) findViewById(R.id.led_fullpower_layout);
	notification = (LinearLayout) findViewById(R.id.led_noti_layout);

	lowpower.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
	    ShowLedDialog("persist.sys.lights_HAL_lp", hexlowpower);
	}});
	charging.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
	    ShowLedDialog("persist.sys.lights_HAL_c", hexcharging);
	}});
	fullpower.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
	    ShowLedDialog("persist.sys.lights_HAL_fp", hexfullpower);
	}});
	notification.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View v) {
	    ShowLedDialog("persist.sys.lights_HAL_n", hexnotification);
	}});
	whatis_lowpower = (ImageView) findViewById(R.id.whatis_led_lowpower);
	whatis_charging = (ImageView) findViewById(R.id.whatis_led_charging);
	whatis_fullpower = (ImageView) findViewById(R.id.whatis_led_fullpower);
	whatis_notification = (ImageView) findViewById(R.id.whatis_led_noti);

        whatis_lowpower.setOnClickListener(whatisClickListener);
        whatis_charging.setOnClickListener(whatisClickListener);
        whatis_fullpower.setOnClickListener(whatisClickListener);
        whatis_notification.setOnClickListener(whatisClickListener);
        
	refreshColors();
    } 
    @Override
    public void onPause() {
	super.onPause();
	if(thisdiag != null && thisdiag.isShowing())
		thisdiag.onStop();
    }

    public void onResume() {
	super.onResume();
	if(thisdiag != null && thisdiag.isShowing())
		thisdiag.onStart();
	
    }
    private View.OnClickListener whatisClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            ImageView thisWhatIs = (ImageView)view;
            if(thisWhatIs == whatis_lowpower){
                ShowDialog(getResources().getString(R.string.led_lowpower_title), getString(R.string.led_lowpower_desc));
            }
            else if(thisWhatIs == whatis_charging){
                ShowDialog(getResources().getString(R.string.led_charging_title),getString(R.string.led_charging_desc));
            }
            else if(thisWhatIs == whatis_fullpower){
                ShowDialog(getResources().getString(R.string.led_fullpower_title),getString(R.string.led_fullpower_desc));
            }
            else if(thisWhatIs == whatis_notification){
                ShowDialog(getResources().getString(R.string.led_noti_title),getString(R.string.led_notification_desc));
            }
        }
    };

    public void refreshColors() {
	hexlowpower = SystemProperties.get("persist.sys.lights_HAL_lp","0xffff0000").replace("0x","").toUpperCase();
	hexcharging = SystemProperties.get("persist.sys.lights_HAL_c","0xffffff00").replace("0x","").toUpperCase();
	hexfullpower = SystemProperties.get("persist.sys.lights_HAL_fp","0xff00ff00").replace("0x","").toUpperCase();
	hexnotification = SystemProperties.get("persist.sys.lights_HAL_n","0xff0000ff").replace("0x","").toUpperCase();
	lowpower.setBackgroundColor((int)Long.parseLong(hexlowpower,16));
	charging.setBackgroundColor((int)Long.parseLong(hexcharging,16));
	fullpower.setBackgroundColor((int)Long.parseLong(hexfullpower,16));
	notification.setBackgroundColor((int)Long.parseLong(hexnotification,16));
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
    public void ShowLedDialog(String property, String color) {
	LedColorDialog lcd = new LedColorDialog(this);
	lcd.setOnDismissListener(this);
	lcd.setProperty(property);
	lcd.show();
	lcd.setColor(color);
	thisdiag = lcd;
    }
    @Override
    public void onDismiss(DialogInterface dialog) {
        LedColorDialog lcd = (LedColorDialog)dialog;
	SystemProperties.set(lcd.getProperty(),lcd.getHexColor());
	refreshColors();
    }
}

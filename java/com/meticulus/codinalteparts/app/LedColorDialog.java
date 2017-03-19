package com.meticulus.codinalteparts.app;

import android.content.Context;
import android.os.Bundle;
import android.app.Dialog;
import android.widget.SeekBar;
import android.widget.TextView;
import android.annotation.NonNull;
import android.view.View;
import android.widget.Button;

public class LedColorDialog extends Dialog {
    SeekBar red_sb, green_sb, blue_sb;
    SeekBar.OnSeekBarChangeListener osbcl;
    TextView red_txt,green_txt,blue_txt;
    int red, green, blue = 0;
    String mProperty = "";
    Button okbtn;

    public LedColorDialog(@NonNull Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.dialog_color);
	setTitle("Color Selection");
	setCancelable(true);

	red_txt = (TextView) findViewById(R.id.red_text);
	green_txt = (TextView) findViewById(R.id.green_text);
	blue_txt = (TextView) findViewById(R.id.blue_text);

        red_sb = (SeekBar) findViewById(R.id.red_seekbar);
        green_sb = (SeekBar) findViewById(R.id.green_seekbar);
        blue_sb = (SeekBar) findViewById(R.id.blue_seekbar);

	red_sb.setMax(255);
	green_sb.setMax(255);
	blue_sb.setMax(255);

	osbcl = new SeekBar.OnSeekBarChangeListener() {
	    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		if(progress == 0 || progress == 127 || progress == 255) {
			//String txt = String.valueOf(progress);
			if(red_sb == seekBar) {
			    red = progress;
			    FunctionsMain.writeLedRed(progress);
			} else if(green_sb == seekBar) {
			    green = progress;
			    FunctionsMain.writeLedGreen(progress);
			} else {
			    blue = progress;
			    FunctionsMain.writeLedBlue(progress);
			}
			return;
		}
		if(progress > 0 && progress <= 64)
			progress = 0;
		else if(progress > 64 && progress <= 191)
			progress = 127;
		else
			progress = 255;
		seekBar.setProgress(progress);
	    }
	    public void onStartTrackingTouch(SeekBar seekBar) {}
	    public void onStopTrackingTouch(SeekBar seekBar) {}
	};

	red_sb.setOnSeekBarChangeListener(osbcl);
	green_sb.setOnSeekBarChangeListener(osbcl);
	blue_sb.setOnSeekBarChangeListener(osbcl);

	okbtn = (Button) findViewById(R.id.lcd_ok_button);
	okbtn.setOnClickListener( new View.OnClickListener() {
		public void onClick(View v) {
		    clearLed();
		    LedColorDialog.this.dismiss();
		}
	});
	
    }

    private String intToHex(int value) {
	switch(value) {
	    case 127:
		return "7f";
	    case 255:
		return "ff";
	    default:
		return "00";
	}
    }
    public void clearLed() {
	FunctionsMain.writeLedRed(0);
	FunctionsMain.writeLedGreen(0);
	FunctionsMain.writeLedBlue(0);

    }
    public void setColor(String hexcolor) {
	red = Integer.parseInt(hexcolor.substring(1,3), 16);
	green = Integer.parseInt(hexcolor.substring(3,5),16);
	blue = Integer.parseInt(hexcolor.substring(5,7),16);
	red_sb.setProgress(red);
	green_sb.setProgress(green);
	blue_sb.setProgress(blue);
	FunctionsMain.writeLedRed(red);
	FunctionsMain.writeLedGreen(green);
	FunctionsMain.writeLedBlue(blue);

    }
    public String getHexColor() {
	return "0xff" + intToHex(red) + intToHex(green) + intToHex(blue);
    }
    public void setProperty(String prop) {
	mProperty = prop;
    }
    public String getProperty() {
	return mProperty;
    }
}

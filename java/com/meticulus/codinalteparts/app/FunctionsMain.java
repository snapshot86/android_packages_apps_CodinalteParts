package com.meticulus.codinalteparts.app;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiInfo;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.SystemProperties;
import android.util.Log;

import java.io.*;
import java.io.FileInputStream;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.BufferedReader;
/**
 * Created by meticulus on 4/7/14.
 */
public class FunctionsMain {

    private static final String TAG = "Codinalte Parts";

    private static final String GLOVE_MODE_FILE = "/sys/devices/platform/huawei_touch/touch_glove";

    private static final String EASY_WAKEUP_GESTURE_FILE = "/sys/devices/platform/huawei_touch/easy_wakeup_gesture";

    private static final String WAKEUP_GESTURE_ENABLE_FILE = "/sys/devices/platform/huawei_touch/wakeup_gesture_enable";

    private static final String USB_HOST_FILE = "/sys/devices/ff100000.hisi_usb/plugusb";

    private static final String RED_BRIGHTNESS_FILE = "/sys/class/leds/red/brightness";
    private static final String GREEN_BRIGHTNESS_FILE = "/sys/class/leds/green/brightness";
    private static final String BLUE_BRIGHTNESS_FILE = "/sys/class/leds/blue/brightness";

    public static void writeLedRed(int value) {
	writeLine(RED_BRIGHTNESS_FILE, String.valueOf(value));
    }

    public static void writeLedGreen(int value) {
	writeLine(GREEN_BRIGHTNESS_FILE, String.valueOf(value));
    }

    public static void writeLedBlue(int value) {
	writeLine(BLUE_BRIGHTNESS_FILE, String.valueOf(value));
    }

    public static String readOneLine(String fileName) {
        String line = null;
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(fileName), 512);
            line = reader.readLine();
        } catch (FileNotFoundException e) {
            Log.w(TAG, "No such file " + fileName + " for reading", e);
        } catch (IOException e) {
            Log.e(TAG, "Could not read from file " + fileName, e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                // Ignored, not much we can do anyway
            }
        }

        return line;
    }

    public static boolean isFileWritable(String fileName) {
        final File file = new File(fileName);
        return file.exists() && file.canWrite();
    }

    public static boolean writeLine(String fileName, String value) {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(fileName));
            writer.write(value);
        } catch (FileNotFoundException e) {
            Log.w(TAG, "No such file " + fileName + " for writing", e);
            return false;
        } catch (IOException e) {
            Log.e(TAG, "Could not write to file " + fileName, e);
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                // Ignored, not much we can do anyway
            }
        }

        return true;
    }

    public static boolean glove_mode_is_supported() {

	return isFileWritable(GLOVE_MODE_FILE);
    }

    public static boolean glove_mode_is_on() {
       String result = "";
       try {
           result = readOneLine(GLOVE_MODE_FILE);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"Glove mode is" + result);
       return result.equals("1");
    }

    public static void set_glove(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Setting Glove Mode ON");
	        writeLine(GLOVE_MODE_FILE, "1");
	    } else {
		Log.i(TAG, "Settings Glove Mode OFF");
	        writeLine(GLOVE_MODE_FILE, "0"); 
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static boolean dt2w_is_supported() {

	return isFileWritable(EASY_WAKEUP_GESTURE_FILE) && 
			isFileWritable(WAKEUP_GESTURE_ENABLE_FILE);
    }

    public static boolean dt2w_is_on() {
       String result = "";
       String result2 = "";
       try {
           result = readOneLine(EASY_WAKEUP_GESTURE_FILE);
           result2 = readOneLine(WAKEUP_GESTURE_ENABLE_FILE);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"DT2W is" + result);
       return result.equals("0x0001") && result2.equals("1");
    }

    public static void set_dt2w(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Settings DT2W ON");
	        writeLine(EASY_WAKEUP_GESTURE_FILE, "1");
	        writeLine(WAKEUP_GESTURE_ENABLE_FILE, "1");
	    } else {
		Log.i(TAG, "Settings DT2W OFF");
	        writeLine(EASY_WAKEUP_GESTURE_FILE, "0");
	        writeLine(WAKEUP_GESTURE_ENABLE_FILE, "0");
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static boolean usb_host_is_supported() {

	return isFileWritable(USB_HOST_FILE);
    }

    public static boolean usb_host_mode_is_on() {
       String result = "";
       try {
           result = readOneLine(USB_HOST_FILE);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"USB HOST is" + result);
       return result.contains("OTG_DEV_HOST");
    }

    public static void set_otg(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Settings USB HOST ON");
	        writeLine(USB_HOST_FILE, "hoston");
	    } else {
		Log.i(TAG, "Settings USB HOST OFF");
	        writeLine(USB_HOST_FILE, "hostoff"); 
	    }
        } catch(Exception e){e.printStackTrace();}
    }
 
}

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

import java.io.File;
import java.io.FileInputStream;
import java.util.Random;

import org.cyanogenmod.internal.util.FileUtils;
/**
 * Created by meticulus on 4/7/14.
 */
public class FunctionsMain {

    private static final String TAG = "Codinalte Parts";

    /* General */

    private static final String TEMP_DIR_CMD = "mkdir -p /data/local/tmp";

    /* Offline Charging */

    private static final String CHARGER_SETTINGS_PATH = "/cache/misc/charger";

    private static final String CHARGER_SHOWDATETIME_PATH = CHARGER_SETTINGS_PATH + "/charger_show_datetime";

    private static final String CHARGER_SHOWDATETIME_ENABLE = "touch " + CHARGER_SHOWDATETIME_PATH;

    private static final String CHARGER_SHOWDATETIME_DISABLE = "rm " + CHARGER_SHOWDATETIME_PATH;

    private static final String CHARGER_NOSUSPEND_PATH = CHARGER_SETTINGS_PATH + "/charger_no_suspend";

    private static final String CHARGER_NOSUSPEND_ENABLE = "touch " + CHARGER_NOSUSPEND_PATH;

    private static final String CHARGER_NOSUSPEND_DISABLE = "rm " + CHARGER_NOSUSPEND_PATH;

    private static final String GLOVE_MODE_FILE = "/sys/devices/platform/huawei_touch/touch_glove";

    private static final String EASY_WAKEUP_GESTURE_FILE = "/sys/devices/platform/huawei_touch/easy_wakeup_gesture";

    private static final String WAKEUP_GESTURE_ENABLE_FILE = "/sys/devices/platform/huawei_touch/wakeup_gesture_enable";

    private static final String USB_HOST_FILE = "/sys/devices/ff100000.hisi_usb/plugusb";

    /* Logging Vars */

    private static final String CMD_KMSG = "cat /proc/kmsg | while read LINE;do " + "" +
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f5) - $LINE\" >> "+
            "/sdcard/autolog_kmsg_\"$DATE\".txt;done &";

    private static final String CMD_LOGCAT = "logcat | while read LINE;do "+
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f4) - $LINE\" >> "+
            "/sdcard/autolog_logcat_\"$DATE\".txt;done &";

    private static final String CMD_RILLOG = "cat /dev/log/radio | while read LINE;do "+
            "DATE=$(busybox date -I); echo \"$(busybox date | cut -d ' ' -f4) - $LINE\" >> "+
            "/sdcard/autolog_rillog_\"$DATE\".txt;done &";

    public static void setChargerShowDateTime(boolean enabled) {

        try {
            if (enabled) {
                Log.i(TAG, "Enabling ChargerShowDateTime");
		SystemProperties.set("persist.sys.charger_showtime","1");
            } else {
                Log.i(TAG, "Disabling ChargerShowDateTime");
                SystemProperties.set("persist.sys.charger_showtime","0");
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    public static boolean getChargerShowDateTime() {

        return SystemProperties.get("persist.sys.charger_showtime","0").equals("1");

    }
    public static void setChargerNoSuspend(boolean enabled) {

        try {
            if (enabled) {
                Log.i(TAG, "Enabling ChargerNoSuspend");
		SystemProperties.set("persist.sys.charger_nosuspend","1");
            } else {
                Log.i(TAG, "Disabling ChargerNoSuspend");
		SystemProperties.set("persist.sys.charger_nosuspend","0");
            }
        }catch(Exception ex){ex.printStackTrace();}

    }

    public static boolean getChargerNoSuspend() {

        return SystemProperties.get("persist.sys.charger_nosuspend", "0").equals("1");

    } 

    public static void startAutokmsg()
    {
        try
        {
            Log.i(TAG, "Running auto kmsg...");
            CommandUtility.ExecuteNoReturn(CMD_KMSG,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void startAutologcat()
    {
        try
        {
            Log.i(TAG, "Running auto logcat...");
            CommandUtility.ExecuteNoReturn(CMD_LOGCAT,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }
    public static void startAutorillog()
    {
        try
        {
            Log.i(TAG, "Running auto ril log...");
            CommandUtility.ExecuteNoReturn(CMD_RILLOG,true, false);
        }
        catch(Exception e){e.printStackTrace();}
    }

    public static boolean glove_mode_is_supported() {

	return FileUtils.isFileWritable(GLOVE_MODE_FILE);
    }

    public static boolean glove_mode_is_on() {
       String result = "";
       try {
           result = FileUtils.readOneLine(GLOVE_MODE_FILE);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"Glove mode is" + result);
       return result.equals("1");
    }

    public static void set_glove(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Setting Glove Mode ON");
	        FileUtils.writeLine(GLOVE_MODE_FILE, "1");
	    } else {
		Log.i(TAG, "Settings Glove Mode OFF");
	        FileUtils.writeLine(GLOVE_MODE_FILE, "0"); 
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static boolean dt2w_is_supported() {

	return FileUtils.isFileWritable(EASY_WAKEUP_GESTURE_FILE) && 
			FileUtils.isFileWritable(WAKEUP_GESTURE_ENABLE_FILE);
    }

    public static boolean dt2w_is_on() {
       String result = "";
       String result2 = "";
       try {
           result = FileUtils.readOneLine(EASY_WAKEUP_GESTURE_FILE);
           result2 = FileUtils.readOneLine(WAKEUP_GESTURE_ENABLE_FILE);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"DT2W is" + result);
       return result.equals("0x0001") && result2.equals("1");
    }

    public static void set_dt2w(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Settings DT2W ON");
	        FileUtils.writeLine(EASY_WAKEUP_GESTURE_FILE, "1");
	        FileUtils.writeLine(WAKEUP_GESTURE_ENABLE_FILE, "1");
	    } else {
		Log.i(TAG, "Settings DT2W OFF");
	        FileUtils.writeLine(EASY_WAKEUP_GESTURE_FILE, "0");
	        FileUtils.writeLine(WAKEUP_GESTURE_ENABLE_FILE, "0");
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static boolean usb_host_is_supported() {

	return FileUtils.isFileWritable(USB_HOST_FILE);
    }

    public static boolean usb_host_mode_is_on() {
       String result = "";
       try {
           result = FileUtils.readOneLine(USB_HOST_FILE);

       } catch(Exception e) {e.printStackTrace();}
       Log.i(TAG,"USB HOST is" + result);
       return result.contains("OTG_DEV_HOST");
    }

    public static void set_otg(boolean on) {
	try {
	    if(on) {
		Log.i(TAG, "Settings USB HOST ON");
	        FileUtils.writeLine(USB_HOST_FILE, "hoston");
	    } else {
		Log.i(TAG, "Settings USB HOST OFF");
	        FileUtils.writeLine(USB_HOST_FILE, "hostoff"); 
	    }
        } catch(Exception e){e.printStackTrace();}
    }

    public static void testProp()
    {
        Log.e(TAG, "Device is: " + Build.MODEL);
    }
}

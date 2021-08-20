package net.utils;

import android.content.Context;
import android.content.SharedPreferences;

//SharedPreferences manager class
public class SharedPrefs {

    public static final String SCREEN_HEIGHT = "screen_height";
    public static final String SCREEN_WIDTH = "screen_width";


    // save facebook accesstoken
    public static final String AccessToken = "AccessToken";

    public static final String BACKGROUND_IMAGE = "background_image";

    public static final String SHOWFACE = "showface";

    public static final String ProfileId = "ProfileId";

    public static final String IS_SHAYRI = "is_shayri";
    public static final String AD_INDEX = "ad_index";
    public static final String ITEM_SIZE = "item_size";
    //SharedPreferences file name
    private static String SHARED_PREFS_FILE_NAME = "name_on_cake_shared_prefs";
    public static int AD_index;
    //here you can centralize all your shared prefs keys
    public static final String ACCESS_TOKEN = "access_token";
    public static final String SPLASH_AD_DATA = "splash_ad_data";
    public static String COUNT = "count";

    public static final String URL_INDEX = "URL_INDEX";
    public static final String FULL_AD_IMAGE = "full_ad_image";

    // use for select photo background from camera
    public static final String BACKGROUND_CAMERA = "camera_file";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(SHARED_PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public static void clearPrefs(Context context) {

        //TODO : Set KEY_IS_LOGIN=false if clear pref
//        if (getBoolean(context, KEY_IS_LOGIN))
        getPrefs(context).edit().clear().commit();
    }

    public static boolean contain(Context context, String key) {
        return getPrefs(context).contains(key);
    }

    //Save Booleans
    public static void savePref(Context context, String key, boolean value) {
        getPrefs(context).edit().putBoolean(key, value).commit();
    }

    //Get Booleans
    public static boolean getBoolean(Context context, String key) {
        return getPrefs(context).getBoolean(key, false);
    }

    //Get Booleans if not found return a predefined default value
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        return getPrefs(context).getBoolean(key, defaultValue);
    }

    //Strings
    public static void save(Context context, String key, String value) {
        getPrefs(context).edit().putString(key, value).commit();
    }


    public static String getString(Context context, String key) {
        return getPrefs(context).getString(key, "");
    }

    public static String getString(Context context, String key, String defaultValue) {
        return getPrefs(context).getString(key, defaultValue);
    }

    //Integers
    public static void save(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return getPrefs(context).getInt(key, 0);
    }

    public static int getInt(Context context, String key, int defaultValue) {
        return getPrefs(context).getInt(key, defaultValue);
    }

    //Floats
    public static void save(Context context, String key, float value) {
        getPrefs(context).edit().putFloat(key, value).commit();
    }

    public static float getFloat(Context context, String key) {
        return getPrefs(context).getFloat(key, 0);
    }

    public static float getFloat(Context context, String key, float defaultValue) {
        return getPrefs(context).getFloat(key, defaultValue);
    }

    //Longs
    public static void save(Context context, String key, long value) {
        getPrefs(context).edit().putLong(key, value).commit();
    }

    public static long getLong(Context context, String key) {
        return getPrefs(context).getLong(key, 0);
    }
    //Integers
    public static void saveclick(Context context, String key, int value) {
        getPrefs(context).edit().putInt(key, value).commit();
    }
    public static long getLong(Context context, String key, long defaultValue) {
        return getPrefs(context).getLong(key, defaultValue);
    }
    public static void removeKey(Context context, String key) {
        getPrefs(context).edit().remove(key).commit();
    }

    public static void setRateStatus(Context context) {
        getPrefs(context).edit().putBoolean("RateStatus", true).apply();
    }

    public static boolean getRateStatus(Context context) {
        return getPrefs(context).getBoolean("RateStatus", false);
    }
}
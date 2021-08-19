/**
 * Copyright (c) IndiaNIC InfoTech Ltd.
 * Created By : Nomesh Gaur
 * Created Date : 02 Feb 2011
 * Modified By :
 * Modified Date :
 */
package net.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

public class Util {

    public static int mYear;
    public static String mMonth;
    public static int mDay;
    public static int a;
    public static int r;
    public static int g;
    public static int b;
    public static int check;
    public static String photoPath;
    public static String framePath = Environment.getExternalStorageDirectory() + "/My App";
    public static String message;
    public static String name;
    public static String address;
    public static String city;
    public static String postal;
    public static String country;
    public static String sendDate;
    public static Matrix photoMatrix;
    public static Bitmap bb;
    public static Bitmap bitmapFrame = null;
    public static Bitmap bitmapPhoto = null;
    public static Bitmap rotatedBitmapPhoto = null;
    public static Bitmap fristPhoto = null;
    public static Bitmap color_bitmap = null;
    public static android.net.Uri Uri = null;
    public static int photoX;
    public static int photoY;
    public static String IMEI;

    /*
     * To check network connection is availability.
     *
     * @param Context Context of the current activity.
     * @return boolean
     */
    public static boolean checkConnection(Context context) {

        final ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else
            return false;
    }

    public static void alertDialogBox(Context context, String title, String msg) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setNeutralButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.dismiss();
                            }
                        }).setCancelable(true).create().show();
    }

}

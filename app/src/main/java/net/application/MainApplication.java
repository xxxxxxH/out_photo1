package net.application;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainApplication extends Application {


    private static MainApplication singleton;


    @Override
    public void onCreate() {
        super.onCreate ();

        singleton = this;
        try {

            //TODO to solve camera issue
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ();
            StrictMode.setVmPolicy (builder.build ());


            PackageInfo info = getPackageManager ().getPackageInfo (getPackageName (), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {

                MessageDigest md = MessageDigest.getInstance ("SHA");
                md.update (signature.toByteArray ());
                Log.d ("KeyHash:", Base64.encodeToString (md.digest (), Base64.DEFAULT));
            }


            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder (getApplicationContext ())
                    .diskCacheExtraOptions (480, 800, null)
                    .diskCacheSize (100 * 1024 * 1024)
                    .diskCacheFileCount (100)
                    .build ();
            ImageLoader.getInstance ().init (config);

        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }

    }

    public static MainApplication getInstance() {
        return singleton;
    }

}
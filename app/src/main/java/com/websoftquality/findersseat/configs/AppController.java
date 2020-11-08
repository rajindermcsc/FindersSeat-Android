package com.websoftquality.findersseat.configs;
/**
 * @package com.trioangle.igniter
 * @subpackage configs
 * @category AppController
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.dependencies.component.AppComponent;
import com.websoftquality.findersseat.dependencies.component.DaggerAppComponent;
import com.websoftquality.findersseat.dependencies.module.ApplicationModule;
import com.websoftquality.findersseat.dependencies.module.NetworkModule;
//import instagram.InstagramHelper;

/*****************************************************************
 AppController
 ****************************************************************/
public class AppController extends Application {

    private static AppComponent appComponent;
    private static AppController mInstance;

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;
    // private static InstagramHelper instagramHelper;

    public static AppComponent getAppComponent() {
        return appComponent;
    }


    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);    // Multiple dex initialize
        mInstance = this;
        // Dagger%COMPONENT_NAME%
        appComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this)) // This also corresponds to the name of your module: %component_name%Module
                .networkModule(new NetworkModule(getResources().getString(R.string.base_url)))
                .build();

        // MultiDex.install(this);
        /*instagramHelper = new InstagramHelper.Builder()
                .withClientId(getResources().getString(R.string.instagram_client_id))
                .withRedirectUrl(getResources().getString(R.string.redirect_url))
                .withScope("basic+public_content+relationships")
                .build();*/
    }

    /* public static InstagramHelper getInstagramHelper() {
         return instagramHelper;
     }*/
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }
}

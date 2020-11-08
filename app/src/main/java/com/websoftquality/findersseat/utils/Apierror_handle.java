package com.websoftquality.findersseat.utils;

import android.content.Context;
import android.util.Log;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.websoftquality.findersseat.R;


public class Apierror_handle
{
    private static final String TAG = "Apierror_handle";
    MessageToast messageToast;
    Context context;
    public Apierror_handle(Context context) {
        this.context=context;
        messageToast=new MessageToast(context);
    }
    public void get_error(VolleyError error)
    {
        Log.e(TAG, "get_error: " );
        if (error instanceof TimeoutError)
        {
            messageToast.showDialog(context.getResources().getString(R.string.timeouterror));
        } else if (error instanceof NoConnectionError) {
            messageToast.showDialog(context.getResources().getString(R.string.No_INTERNET_CONNECTION));
        } else {
            messageToast.showDialog(context.getResources().getString(R.string.defaulterror));
        }
    }
}

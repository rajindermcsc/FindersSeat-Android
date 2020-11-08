package com.websoftquality;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.adapters.NotificationAdapter;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    RecyclerView rv_notifications;
    LinearLayoutManager linearLayoutManager;
    NotificationAdapter notificationAdapter;
    JSONObject jsonObject;
    Loading loading;
    MessageToast messageToast;
    String apiurl,apilike;
    Apierror_handle apierror_handle;
    @Inject
    SessionManager sessionManager;
    TextView nodata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notifications, container, false);
        AppController.getAppComponent().inject(this);

        rv_notifications=view.findViewById(R.id.rv_notifications);
        nodata=view.findViewById(R.id.nodata);
        linearLayoutManager=new LinearLayoutManager(getContext());
        loading=new Loading(getContext());
        messageToast=new MessageToast(getContext());
        apierror_handle=new Apierror_handle(getContext());
        api_get_notifications();
        return view;
    }

    private void api_get_notifications() {
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apilike);
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.notificationapi), response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    if (jsonObject.getJSONArray("data").length()==0){
                        nodata.setVisibility(View.VISIBLE);
                        rv_notifications.setVisibility(View.GONE);
                    }
                    else {
                        nodata.setVisibility(View.GONE);
                        rv_notifications.setVisibility(View.VISIBLE);
                        notificationAdapter = new NotificationAdapter(getContext(), jsonObject);
                        rv_notifications.setLayoutManager(linearLayoutManager);
                        rv_notifications.setAdapter(notificationAdapter);
                    }

                }
                else
                {
                    loading.hideDialog();
                    jsonObject.getString("message");

                }
            }
            catch(Exception e)
            {
                Log.e("TAG", "onResponse: "+e.getMessage());
                loading.hideDialog();
                e.printStackTrace();
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.e("TAG", "onResponse: "+error.getMessage());
                loading.hideDialog();

                try
                {
                    apierror_handle.get_error(error);
                }catch (Exception e)
                {
                    Log.e("TAG", "onErrorResponse: " + e.getMessage());
                }
            }
        })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<>();
                params.put("access_token", sessionManager.getToken());

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.adapters.profile.EnlargeSliderAdapter;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.views.customize.CirclePageIndicator;
import com.websoftquality.findersseat.views.customize.IgniterViewPager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class OtherProfileView extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "OtherProfileView";
    Intent intent;
    String id;
    RelativeLayout rlt_unlike_lay,rlt_like_lay,rtl_arrow_down;
    Apierror_handle apierror_handle;
    MessageToast messageToast;
    Loading loading;
    JSONObject jsonObject,jsonAccept;
    @Inject
    SessionManager sessionManager;
    String apiurl;
    TextView tv_user_name_age;
    private IgniterViewPager viewPager;
    private CirclePageIndicator pageIndicator;
    ArrayList<String> listimages=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile_view);
        AppController.getAppComponent().inject(this);
        intent=getIntent();
        id=intent.getStringExtra("id");
        rtl_arrow_down=findViewById(R.id.rtl_arrow_down);
        rlt_like_lay=findViewById(R.id.rlt_like_lay);
        tv_user_name_age=findViewById(R.id.tv_user_name_age);
        rlt_unlike_lay=findViewById(R.id.rlt_unlike_lay);
        rlt_like_lay.setOnClickListener(this);
        rtl_arrow_down.setOnClickListener(this);
        rlt_unlike_lay.setOnClickListener(this);
        messageToast=new MessageToast(this);
        apierror_handle=new Apierror_handle(this);
        loading=new Loading(this);
        viewPager = findViewById(R.id.vp_enlarge_profile);
        pageIndicator = findViewById(R.id.cpi_enlarge_profile);
        apiurl=getResources().getString(R.string.base_url)+getResources().getString(R.string.profileapi);
        getProfile(apiurl);
    }



    private void getProfile(String apiurl) {
        Log.e(TAG, "getHomePage: "+apiurl);
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, response -> {
            try{
                loading.hideDialog();
                jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {

                    listimages.clear();


                    listimages.add(jsonObject.getJSONObject("data").getString("avater"));
                    for (int i=0;i<jsonObject.getJSONObject("data").getJSONArray("mediafiles").length();i++){
                        listimages.add(jsonObject.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(i).getString("avater"));
                    }
                    Log.e("TAG", "onCreate: "+listimages);
                    if (jsonObject.getJSONObject("data").getJSONArray("mediafiles").length()==0){

                    }
                    else {

                        viewPager.setAdapter(new EnlargeSliderAdapter(this,listimages));
                        pageIndicator.setViewPager(viewPager, 0);
                    }
                    initPageIndicator();
                    Log.e(TAG, "getProfile: "+jsonObject.getJSONObject("data").getString("avater"));
                    tv_user_name_age.setText(jsonObject.getJSONObject("data").getString("username")+", "+
                            jsonObject.getJSONObject("data").getString("age"));
                }
                else
                {
                    loading.hideDialog();

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
                Map<String, String> params = new HashMap<String, String>();
                Log.e(TAG, "getParams: "+sessionManager.getToken());
                params.put("access_token", sessionManager.getToken());
                params.put("user_id", id);
                params.put("fetch", "data");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void initPageIndicator() {
        final float density = getResources().getDisplayMetrics().density;
        pageIndicator.setRadius(3 * density);
        pageIndicator.setPageColor(ContextCompat.getColor(this, R.color.gray_text_color));
        pageIndicator.setFillColor(ContextCompat.getColor(this, R.color.color_accent));
        pageIndicator.setStrokeColor(ContextCompat.getColor(this, R.color.gray_text_color));
        pageIndicator.setOnClickListener(null);
        pageIndicator.setExtraSpacing((float) (1.5 * density));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlt_unlike_lay:
                apiAccept("dislikes");
                break;
            case R.id.rlt_like_lay:
                apiAccept("likes");
                break;
            case R.id.rtl_arrow_down:
                finish();
                break;
        }
    }


    private void apiAccept(String likes) {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.addlikesapi), response -> {
            try{
                loading.hideDialog();
                jsonAccept = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonAccept);
                if (jsonAccept.getString("code").equals("200"))
                {
                    if (likes.equalsIgnoreCase("likes")){
                        messageToast.showDialog("Request Accepted");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                finish();
                            }
                        },2500);
                    }
                    else {
                        messageToast.showDialog("Request Rejected");new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                finish();
                            }
                        },2500);

                    }
                }
                else
                {
                    loading.hideDialog();
                    messageToast.showDialog(jsonAccept.getString("message"));

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
                Map<String, String> params = new HashMap<String, String>();
                params.put("access_token", sessionManager.getToken());
                if (likes.equalsIgnoreCase("likes")) {
                    params.put("likes", id);
                    params.put("dislikes", "");
                }
                else {
                    params.put("likes", "");
                    params.put("dislikes", id);
                }
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
package com.websoftquality;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.views.EnlargeProfile;
import com.websoftquality.findersseat.views.main.SelectPaymentActivity;
import com.websoftquality.findersseat.views.main.SettingActivity;
import com.websoftquality.findersseat.views.profile.EditProfile;
import com.websoftquality.findersseat.views.profile.EnlargeProfileActivity;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileDemoFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ProfileDemoFragment";
    CircleImageView civ_profile_image;
    Loading loading;
    Apierror_handle apierror_handle;
    MessageToast messageToast;
    TextView tv_user_name_age;
    String apiurl;
    @Inject
    SessionManager sessionManager;
    JSONObject jsonObject;
    LinearLayout llt_edit_profile,llt_settings;
    Button btn_igniter_plus;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_demo, container, false);
        AppController.getAppComponent().inject(this);
        civ_profile_image=view.findViewById(R.id.civ_profile_image);
        tv_user_name_age=view.findViewById(R.id.tv_user_name_age);
        llt_edit_profile=view.findViewById(R.id.llt_edit_profile);
        llt_settings=view.findViewById(R.id.llt_settings);
        btn_igniter_plus=view.findViewById(R.id.btn_igniter_plus);
        apierror_handle=new Apierror_handle(getContext());
        loading=new Loading(getContext());
        messageToast=new MessageToast(getContext());
        apiurl=getResources().getString(R.string.base_url)+getResources().getString(R.string.profileapi);
        civ_profile_image.setOnClickListener(this);
        llt_edit_profile.setOnClickListener(this);
        llt_settings.setOnClickListener(this);
        btn_igniter_plus.setOnClickListener(this);
        getProfile(apiurl);
        return view;
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
                    Log.e(TAG, "getProfile: "+jsonObject.getJSONObject("data").getString("avater"));
                    tv_user_name_age.setText(jsonObject.getJSONObject("data").getString("username")+", "+
                            jsonObject.getJSONObject("data").getString("age"));
                    Glide.with(getContext())
                            .load(jsonObject.getJSONObject("data").getString("avater"))
                            //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            //.placeholder(R.color.gray_color)
                            //.error(R.color.gray_color)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(civ_profile_image);
//                    onSuccessShowAllMatches(jsonObject);
//
////                        Intent intent=new Intent(LoginEmailActivity.this,HomeActivity.class);
//                    sessionManager.setToken(jsonObject.getJSONObject("data").getString("access_token"));
//                    sessionManager.setUserName(jsonObject.getString("user_name"));
//                    sessionManager.setUserId(jsonObject.getJSONObject("data").getInt("user_id"));
////                        startActivity(intent);
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
                params.put("user_id", String.valueOf(sessionManager.getUserId()));
                params.put("fetch", "data");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.civ_profile_image){
            Intent intent;
            intent = new Intent(getContext(), EnlargeProfile.class);
            intent.putExtra("jsonObject", String.valueOf(jsonObject));
            startActivity(intent);
        }else if (v.getId()==R.id.llt_edit_profile){
            Intent intent;
            intent = new Intent(getContext(), EditProfile.class);
            intent.putExtra("jsonObject", String.valueOf(jsonObject));
            startActivity(intent);
        }
        else if (v.getId()==R.id.llt_settings){
            Intent intent;
            intent = new Intent(getContext(), SettingActivity.class);
            intent.putExtra("jsonObject", String.valueOf(jsonObject));
            startActivity(intent);
        }
        else if (v.getId()==R.id.btn_igniter_plus){
            Intent intent;
            intent = new Intent(getContext(), SelectPaymentActivity.class);
            startActivity(intent);
        }
    }
}
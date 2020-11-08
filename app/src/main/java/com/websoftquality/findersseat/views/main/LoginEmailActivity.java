package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class LoginEmailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_login_phone,text_forgot_password;
    EditText edt_user_name,edt_password;
    JSONObject jsonObject;
    Loading loading;
    MessageToast messageToast;
    String apiurl,device_id;
    Apierror_handle apierror_handle;
    @Inject
    SessionManager sessionManager;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);
        AppController.getAppComponent().inject(this);
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);
        messageToast=new MessageToast(this);
        edt_user_name=findViewById(R.id.edt_user_name);
        edt_password=findViewById(R.id.edt_password);
        tv_login_phone=findViewById(R.id.tv_login_phone);
        text_forgot_password=findViewById(R.id.text_forgot_password);
        tv_login_phone.setOnClickListener(this);
        text_forgot_password.setOnClickListener(this);
        handler=new Handler();
        device_id= FirebaseInstanceId.getInstance().getToken();
        sessionManager.setDeviceId(device_id);
        Log.e("TAG", "onCreate: "+device_id);
        updateDeviceId();
    }

    private void updateDeviceId() {
        if (!sessionManager.getToken().equals("")) {
            if (sessionManager.getDeviceId().equals("")) {
                sessionManager.setDeviceId(FirebaseInstanceId.getInstance().getToken());
            }
        }
    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login_phone:
                if (edt_user_name.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Please Enter UserName", Toast.LENGTH_SHORT).show();
                }else if (edt_password.getText().toString().trim().isEmpty()){
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                }else if (edt_password.getText().toString().length()<6){
                    Toast.makeText(this, "Password should be at least 6 digits", Toast.LENGTH_SHORT).show();
                }
                else if (!validEmail(edt_user_name.getText().toString())) {
                    Toast.makeText(this,"Enter valid Email",Toast.LENGTH_LONG).show();
                }
                else {
                    loginApi(getResources().getString(R.string.base_url)+getResources().getString(R.string.loginapi));
                }
                break;
            case R.id.text_forgot_password:
                Intent intent_forgot=new Intent(LoginEmailActivity.this,ForgotPasswordActivity.class);
                startActivity(intent_forgot);
                break;
        }
    }


    private void loginApi(String apiurl) {
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    messageToast.showDialog("Login Successful");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                    messageToast.showDialog("Login Successful");
                            try {
                                Intent intent=new Intent(LoginEmailActivity.this,HomeActivity.class);
                                sessionManager.setToken(jsonObject.getJSONObject("data").getString("access_token"));
                                sessionManager.setUserName(jsonObject.getJSONObject("data").getJSONObject("user_info").getString("username"));
                                sessionManager.setUserId(jsonObject.getJSONObject("data").getInt("user_id"));
                                sessionManager.setMinAge("18");
                                sessionManager.setMaxAge("40");
                                sessionManager.setDistance("100");
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },2500);


                }
                else
                {
                    loading.hideDialog();
                    messageToast.showDialog(jsonObject.getString("message"));
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
                params.put("username", edt_user_name.getText().toString());
                params.put("platform", "mobile");
                params.put("password", edt_password.getText().toString());
                params.put("device_id", device_id);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
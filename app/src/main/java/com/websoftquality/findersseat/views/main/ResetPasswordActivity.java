package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed;
    Intent intent;
    String otp,email;
    Loading loading;
    MessageToast messageToast;
    String apiurl;
    Apierror_handle apierror_handle;
    Handler handler;
    EditText edt_password,edt_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        tv_proceed=findViewById(R.id.tv_proceed);
        edt_password=findViewById(R.id.edt_password);
        edt_confirm_password=findViewById(R.id.edt_confirm_password);
        intent=getIntent();
        otp=intent.getStringExtra("otp");
        email=intent.getStringExtra("email");
        tv_proceed.setOnClickListener(this);
        loading=new Loading(this);
        messageToast=new MessageToast(this);
        apierror_handle=new Apierror_handle(this);
        handler=new Handler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_proceed:
                validations();


                break;
        }
    }

    private void validations() {
        if (edt_password.getText().toString().isEmpty()){
            messageToast.showDialog("Please Enter Password");
        }else if (edt_confirm_password.getText().toString().isEmpty()){
            messageToast.showDialog("Please Enter Confirm Password");
        }else if (edt_password.getText().toString().length()<6){
            messageToast.showDialog("Password should be atleast 6 digits");
        }
        else if (!edt_confirm_password.getText().toString().trim().equals(edt_password.getText().toString().trim())){
            messageToast.showDialog("Password Doesn't match");
        }
        else {
            apiurl=getResources().getString(R.string.base_url)+getResources().getString(R.string.replacepasswordapi);
            forgotpasswordApi(apiurl);
        }
    }

    private void forgotpasswordApi(String apiurl) {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    messageToast.showDialog(jsonObject.getString("message"));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            try {
                                Intent intent=new Intent(ResetPasswordActivity.this,LoginEmailActivity.class);
                                startActivity(intent);
                                finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    },2500);

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
                params.put("email", email);
                params.put("email_code", otp);
                params.put("password", otp);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
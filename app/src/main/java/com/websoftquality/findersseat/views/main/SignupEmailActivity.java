package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.anurag.multiselectionspinner.MultiSelectionSpinnerDialog;
import com.anurag.multiselectionspinner.MultiSpinner;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.CustomSpinnerAdapterr;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SignupEmailActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_first_name,edt_last_name,edt_user_name,edt_email,edt_books,edt_sda,edt_sda_equivalent,edt_church,edt_password,
            edt_confirm_password;
    Spinner spinner_diet,spinner_sabbath,spinner_state_death,spinner_ministry;
    ArrayList<String> sabbath=new ArrayList<>();
    ArrayList<String> stateofdead=new ArrayList<>();
    ArrayList<String> diet=new ArrayList<>();
    ArrayList<String> ministry=new ArrayList<>();
    TextView tv_login_phone;
    JSONObject jsonObject;
    Loading loading;
    MessageToast messageToast;
    String apiurl;
    Apierror_handle apierror_handle;
    @Inject
    SessionManager sessionManager;
    CustomSpinnerAdapterr customSpinnerAdapterr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);
        AppController.getAppComponent().inject(this);
        loading=new Loading(this);
        apierror_handle=new Apierror_handle(this);
        messageToast=new MessageToast(this);
        edt_first_name=findViewById(R.id.edt_first_name);
        edt_last_name=findViewById(R.id.edt_last_name);
        edt_user_name=findViewById(R.id.edt_user_name);
        edt_email=findViewById(R.id.edt_email);
        edt_books=findViewById(R.id.edt_books);
        edt_sda=findViewById(R.id.edt_sda);
        edt_sda_equivalent=findViewById(R.id.edt_sda_equivalent);
        edt_church=findViewById(R.id.edt_church);
        edt_password=findViewById(R.id.edt_password);
        edt_confirm_password=findViewById(R.id.edt_confirm_password);
        spinner_sabbath=findViewById(R.id.spinner_sabbath);
        spinner_state_death=findViewById(R.id.spinner_state_death);
        spinner_diet=findViewById(R.id.spinner_diet);
        spinner_ministry=findViewById(R.id.spinner_ministry);
        tv_login_phone=findViewById(R.id.tv_login_phone);
//        spinner_sabbath.initMultiSpinner(this,spinner_sabbath);
//        spinner_state_death.initMultiSpinner(this,spinner_state_death);
//        spinner_ministry.initMultiSpinner(this,spinner_ministry);
        sabbath.add("None");
        sabbath.add("Saturday");
        sabbath.add("Sunday");
        sabbath.add("Monday");
        sabbath.add("Friday");
        stateofdead.add("None");
        stateofdead.add("Go to heaven");
        stateofdead.add("Become Ghosts");
        stateofdead.add("The dead know nothing and spirit returns to God");
        stateofdead.add("Go to hell");
        diet.add("None");
        diet.add("Vegan");
        diet.add("Vegetarian");
        diet.add("Pescitarian");
        diet.add("Omnivore");
        ministry.add("None");
        ministry.add("Health Ministry");
        ministry.add("Family Ministry");
        ministry.add("Missionary");
        ministry.add("Education");
        ministry.add("Religious Liberty");
        ministry.add("Children's Ministry");
        ministry.add("Communication");
        tv_login_phone.setOnClickListener(this);

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,sabbath);
        spinner_sabbath.setAdapter(customSpinnerAdapterr);
        spinner_sabbath.setSelection(0);

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,stateofdead);
        spinner_state_death.setAdapter(customSpinnerAdapterr);
        spinner_state_death.setSelection(0);

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,ministry);
        spinner_ministry.setAdapter(customSpinnerAdapterr);
        spinner_ministry.setSelection(0);

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,diet);
        spinner_diet.setAdapter(customSpinnerAdapterr);
        spinner_diet.setSelection(0);


    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_login_phone){
            validation();
        }
    }

    private void validation() {
//        Intent intent=new Intent(SignupEmailActivity.this,SignupHeightActivity.class);
//        startActivity(intent);
        if (edt_user_name.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter User Name");
        }else if (edt_user_name.getText().toString().length()<5){
            messageToast.showDialog("User Name should be atleast 5 characters");
        }else if (edt_email.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter Email");
        }else if (edt_books.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter Books in bible");
        }else if (edt_sda.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter SDA Prophet");
        }else if (edt_sda_equivalent.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter SDA Equivalent to Boy Scouts");
        } else if (edt_sda_equivalent.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter SDA Equivalent to Boy Scouts");
        }else if (edt_church.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter Church you attend");
        }else if (edt_password.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter Password");
        }else if (edt_confirm_password.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Enter Confirm Password");
        }else if (!edt_confirm_password.getText().toString().trim().equals(edt_password.getText().toString().trim())){
            messageToast.showDialog("Password Doesn't match");
        }
        else
        {
            apiSignup(getResources().getString(R.string.base_url)+getResources().getString(R.string.registeruserapi));
        }
    }

    private void apiSignup(String apiurl) {
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
                    Intent intent=new Intent(SignupEmailActivity.this,SignupHeightActivity.class);
                    sessionManager.setToken(jsonObject.getJSONObject("data").getString("access_token"));
                    sessionManager.setUserName(jsonObject.getJSONObject("data").getJSONObject("user_info").getString("username"));
                    sessionManager.setUserId(jsonObject.getJSONObject("data").getInt("user_id"));
                    startActivity(intent);
                }
                else
                {
                    loading.hideDialog();

                }
            }
            catch(Exception e) {
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
                Log.e("TAG", "getParams: "+edt_user_name.getText().toString());
                Log.e("TAG", "getParams: "+edt_password.getText().toString());
                Log.e("TAG", "getParams: "+edt_email.getText().toString());
                Log.e("TAG", "getParams: "+edt_first_name.getText().toString());
                Log.e("TAG", "getParams: "+edt_last_name.getText().toString());
                Log.e("TAG", "getParams: "+spinner_sabbath.getSelectedItem().toString());
                Log.e("TAG", "getParams: "+spinner_state_death.getSelectedItem().toString());
                Log.e("TAG", "getParams: "+edt_books.getText().toString());
                Log.e("TAG", "getParams: "+edt_sda.getText().toString());
                Log.e("TAG", "getParams: "+edt_sda_equivalent.getText().toString());
                Log.e("TAG", "getParams: "+spinner_diet.getSelectedItem().toString());
                Log.e("TAG", "getParams: "+spinner_ministry.getSelectedItem().toString());
                Log.e("TAG", "getParams: "+edt_church.getText().toString());
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", edt_user_name.getText().toString());
                params.put("password", edt_password.getText().toString());
                params.put("email", edt_email.getText().toString());
                params.put("first_name", edt_first_name.getText().toString());
                params.put("last_name", edt_last_name.getText().toString());
                params.put("test_q1", spinner_sabbath.getSelectedItem().toString());
                params.put("test_q2", spinner_state_death.getSelectedItem().toString());
                params.put("test_q3", edt_books.getText().toString());
                params.put("test_q4", edt_sda.getText().toString());
                params.put("test_q5", edt_sda_equivalent.getText().toString());
                params.put("question1", spinner_diet.getSelectedItem().toString());
                params.put("question2", spinner_ministry.getSelectedItem().toString());
                params.put("church_attend", edt_church.getText().toString());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


}
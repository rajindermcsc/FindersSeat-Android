package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.utils.PayPalConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class SelectPaymentActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rlt_one_month,rlt_six_month,rlt_twelve_month,rlt_life_time;
    Button btn_continue;
    TextView tv_tife,tv_per_year,tv_per_six_month,tv_per_month,tv_six_month,tv_twelve_month,tv_one_month,tv_life_time,
            tv_six_month_book;
    public static final int PAYPAL_REQUEST_CODE = 123;
    String amount="$ 7.99";
    String[] amountfinal;
    Apierror_handle apierror_handle;
    MessageToast messageToast;
    Loading loading;
    JSONObject jsonObject;
    @Inject
    SessionManager sessionManager;
    String type="2";


    //Paypal Configuration Object
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment);
        AppController.getAppComponent().inject(this);
        Intent intent = new Intent(this, PayPalService.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        messageToast=new MessageToast(this);
        apierror_handle=new Apierror_handle(this);
        loading=new Loading(this);
        startService(intent);
        tv_six_month=findViewById(R.id.tv_six_month);
        tv_twelve_month=findViewById(R.id.tv_twelve_month);
        tv_one_month=findViewById(R.id.tv_one_month);
        tv_life_time=findViewById(R.id.tv_life_time);
        tv_six_month_book=findViewById(R.id.tv_six_month_book);
        rlt_one_month=findViewById(R.id.rlt_one_month);
        rlt_six_month=findViewById(R.id.rlt_six_month);
        rlt_twelve_month=findViewById(R.id.rlt_twelve_month);
        rlt_life_time=findViewById(R.id.rlt_life_time);
        tv_tife=findViewById(R.id.tv_tife);
        tv_per_year=findViewById(R.id.tv_per_year);
        tv_per_six_month=findViewById(R.id.tv_per_six_month);
        tv_per_month=findViewById(R.id.tv_per_month);
        btn_continue=findViewById(R.id.btn_continue);
        rlt_one_month.setOnClickListener(this);
        rlt_six_month.setOnClickListener(this);
        rlt_twelve_month.setOnClickListener(this);
        rlt_life_time.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rlt_one_month:
                tv_six_month_book.setVisibility(View.INVISIBLE);
                rlt_one_month.setBackground(getResources().getDrawable(R.drawable.rect_blue_border));
                rlt_six_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_twelve_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_life_time.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                tv_per_month.setTextColor(getResources().getColor(R.color.blue1));
                tv_one_month.setTextColor(getResources().getColor(R.color.blue1));
                tv_six_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_six_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_twelve_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_year.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_tife.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_life_time.setTextColor(getResources().getColor(R.color.text_light_gray));
                amount=tv_per_month.getText().toString();
                type="1";
                break;
            case R.id.rlt_six_month:
                type="2";
                tv_six_month_book.setVisibility(View.VISIBLE);
                rlt_one_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_six_month.setBackground(getResources().getDrawable(R.drawable.rect_blue_border));
                rlt_twelve_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_life_time.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                amount=tv_per_six_month.getText().toString();
                tv_six_month.setTextColor(getResources().getColor(R.color.blue1));
                tv_per_six_month.setTextColor(getResources().getColor(R.color.blue1));
                tv_twelve_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_year.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_tife.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_life_time.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_one_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.rlt_twelve_month:
                type="3";
                tv_six_month_book.setVisibility(View.INVISIBLE);
                rlt_one_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_twelve_month.setBackground(getResources().getDrawable(R.drawable.rect_blue_border));
                rlt_six_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_life_time.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                amount=tv_per_six_month.getText().toString();
                tv_twelve_month.setTextColor(getResources().getColor(R.color.blue1));
                tv_per_year.setTextColor(getResources().getColor(R.color.blue1));
                tv_tife.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_life_time.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_one_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_six_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_year.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.rlt_life_time:
                type="4";
                tv_six_month_book.setVisibility(View.INVISIBLE);
                rlt_one_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_life_time.setBackground(getResources().getDrawable(R.drawable.rect_blue_border));
                rlt_twelve_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                rlt_six_month.setBackground(getResources().getDrawable(R.drawable.rect_white_border));
                amount=tv_tife.getText().toString();
                tv_tife.setTextColor(getResources().getColor(R.color.blue1));
                tv_life_time.setTextColor(getResources().getColor(R.color.blue1));
                tv_per_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_one_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_six_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_six_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_twelve_month.setTextColor(getResources().getColor(R.color.text_light_gray));
                tv_per_year.setTextColor(getResources().getColor(R.color.text_light_gray));
                break;
            case R.id.btn_continue:
                getPayment();
                break;
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    private void getPayment() {

        amountfinal=amount.split(" ");
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amountfinal[1])), "USD", "Advent Mingle",
                PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);

        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {

            //If the result is OK i.e. user has not canceled the payment
            if (resultCode == Activity.RESULT_OK) {
                //Getting the payment confirmation
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);

                //if confirmation is not null
                if (confirm != null) {
                    try {
                        //Getting the payment details
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        Log.i("paymentExample", paymentDetails);
                        JSONObject jsonDetails = new JSONObject(paymentDetails);

                        if (jsonDetails.getJSONObject("response").getString("state").equalsIgnoreCase("approved")){

                            messageToast.showDialog("Payment Successful");
                            api_set_pro();
                        }
                        else {
                            messageToast.showDialog("Payment Failed. Please Try Again Later!");
                        }
                        //Starting a new activity for the payment details and also putting the payment details with intent
//                        startActivity(new Intent(this, ConfirmationActivity.class)
//                                .putExtra("PaymentDetails", paymentDetails)
//                                .putExtra("PaymentAmount", amountfinal[1]));

                    } catch (JSONException e) {
                        Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paymentExample", "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paymentExample", "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }


    private void api_set_pro() {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.setproapi), response -> {
            try{
                loading.hideDialog();
                jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    finish();
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
                params.put("access_token", sessionManager.getToken());
                params.put("type", amountfinal[1]);
                params.put("price", amountfinal[1]);
                params.put("via", "PayPal");

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
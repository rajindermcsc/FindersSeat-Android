package com;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.interfaces.DropDownClickListener;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.CommonMethods;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.views.chat.ChatConversationAdapter;
import com.websoftquality.findersseat.views.chat.LikeListAdapter;
import com.websoftquality.findersseat.views.chat.MatchesAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView back_button;
    LinearLayoutManager linearLayoutManager;
    Loading loading;
    JSONObject jsonlikes,jsonmsg;
    MessageToast messageToast;
    Apierror_handle apierror_handle;
    String apilikes,name,image,id;
    Intent intent;
    @Inject
    SessionManager sessionManager;
    ChatConversationAdapter chatConversationAdapter;
    RecyclerView rv_chat_conversation_list;
    RelativeLayout rlt_empty_chat;
    TextView tv_name,tv_menu_icon;
    EditText edt_new_msg;
    ImageView iv_send;
    CircleImageView civ_empty_chat_image;
    AlertDialog dialog;
    @Inject
    CommonMethods commonMethods;
    DropDownClickListener listener = new DropDownClickListener() {
        @Override
        public void onDropDrownClick(String value) {

            if (value.equalsIgnoreCase(getString(R.string.MUTE_NOTIFICATION))){

            }else if(value.equalsIgnoreCase(getString(R.string.BLOCK))){
                apiblock();
//                getUnmatchDetails();
            }else if(value.equalsIgnoreCase(getString(R.string.REPORT))){
                apireport();
//                getReportDetails();
            }else if(value.equalsIgnoreCase(getString(R.string.UNMATCH))){
                apidislike();
//                getUnmatchDetails();
            }
        }
    };

    private void apidislike() {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.addlikesapi), response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    finish();
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
                params.put("likes", "");
                params.put("dislikes", id);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void apireport() {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.reportapi), response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    finish();
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
                params.put("report_userid", id);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void apiblock() {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.blockapi), response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    finish();
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
                params.put("block_userid", id);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        AppController.getAppComponent().inject(this);
        intent=getIntent();
        dialog = commonMethods.getAlertDialog(this);
        name=intent.getStringExtra("name");
        image=intent.getStringExtra("image");
        id=intent.getStringExtra("id");
        edt_new_msg=findViewById(R.id.edt_new_msg);
        tv_menu_icon=findViewById(R.id.tv_menu_icon);
        tv_menu_icon.setOnClickListener(this);
        iv_send=findViewById(R.id.iv_send);
        back_button=findViewById(R.id.back_button);
        rlt_empty_chat=findViewById(R.id.rlt_empty_chat);
        civ_empty_chat_image=findViewById(R.id.civ_empty_chat_image);
        tv_name=findViewById(R.id.tv_name);
        rv_chat_conversation_list=findViewById(R.id.rv_chat_conversation_list);
        back_button.setOnClickListener(this);
        messageToast=new MessageToast(this);
        apierror_handle=new Apierror_handle(this);
        loading=new Loading(this);
        iv_send.setOnClickListener(this);
        apilikes=getResources().getString(R.string.base_url)+getResources().getString(R.string.conversationchatlistapi);
        apilikes();
    }

    private void apilikes() {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, apilikes, response -> {
            try{
                loading.hideDialog();
                jsonlikes = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonlikes);
                if (jsonlikes.getString("code").equals("200"))
                {

                    if (jsonlikes.getJSONArray("data").length()==0){
                        rlt_empty_chat.setVisibility(View.VISIBLE);
                        tv_name.setText(name);
                        Glide.with(this)
                                .load(image)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(civ_empty_chat_image);
                    }
                    else {
                        rlt_empty_chat.setVisibility(View.GONE);
                        linearLayoutManager=new LinearLayoutManager(this);
                        chatConversationAdapter=new ChatConversationAdapter(this,jsonlikes,sessionManager.getUserId());
                        rv_chat_conversation_list.setLayoutManager(linearLayoutManager);
                        rv_chat_conversation_list.setAdapter(chatConversationAdapter);
                    }
                }
                else
                {
                    loading.hideDialog();
                    messageToast.showDialog(jsonlikes.getString("message"));

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
                params.put("to_userid", id);
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_menu_icon:
                commonMethods.dropDownMenu(this, v, null, getResources().getStringArray(R.array.QUICK_CHAT_MENU_TITLE), listener);
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.iv_send:
                validations();
                break;
        }
    }

    private void validations() {
        if (edt_new_msg.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Please Enter Text to send");
        }
        else {
            apisend();
        }
    }

    private void apisend() {
        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, getResources().getString(R.string.base_url)+getResources().getString(R.string.sendmessageapi), response -> {
            try{
                loading.hideDialog();
                jsonmsg = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonmsg);
                if (jsonmsg.getString("status").equals("200"))
                {
                    edt_new_msg.setText("");
                    apilikes();
                }
                else
                {
                    loading.hideDialog();
                    messageToast.showDialog(jsonmsg.getString("message"));

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
                params.put("to_userid", id);
                params.put("message", edt_new_msg.getText().toString());
                params.put("hash_id", "");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
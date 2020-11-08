package com.websoftquality.findersseat.views.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

import javax.inject.Inject;

public class LikesUserActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "LikesUserActivity";
    Intent intent;
    RecyclerView rv_likes_list;
    LikeListAdapter likeListAdapter;
    ImageView back_button;
    LinearLayoutManager linearLayoutManager;
    Loading loading;
    JSONObject jsonlikes;
    MessageToast messageToast;
    Apierror_handle apierror_handle;
    String apilikes;
    @Inject
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_likes_user);
        AppController.getAppComponent().inject(this);
        intent=getIntent();
        rv_likes_list=findViewById(R.id.rv_likes_list);
        back_button=findViewById(R.id.back_button);
        back_button.setOnClickListener(this);
        messageToast=new MessageToast(this);
        apierror_handle=new Apierror_handle(this);
        loading=new Loading(this);
        apilikes=getResources().getString(R.string.base_url)+getResources().getString(R.string.likedapi);

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
                if (jsonlikes.getString("status").equals("200"))
                {

                    if (jsonlikes.getJSONArray("data").length()==0){

                    }
                    else {
                        linearLayoutManager=new LinearLayoutManager(this);
                        likeListAdapter=new LikeListAdapter(this,jsonlikes,sessionManager.getToken());
                        rv_likes_list.setLayoutManager(linearLayoutManager);
                        rv_likes_list.setAdapter(likeListAdapter);
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
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_button:
                onBackPressed();
                break;
        }
    }
}
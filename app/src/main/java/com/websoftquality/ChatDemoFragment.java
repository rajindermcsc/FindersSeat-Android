package com.websoftquality;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.views.chat.ConversationAdapter;
import com.websoftquality.findersseat.views.chat.LikesUserActivity;
import com.websoftquality.findersseat.views.chat.MatchesAdapter;
import com.websoftquality.findersseat.views.main.PremiumAdapter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;


public class ChatDemoFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ChatDemoFragment";
    RecyclerView rv_new_matches_list,rv_message_list,rv_premium;
    LinearLayoutManager linearLayoutManager,linearLayoutManager1,linearLayoutManager2;
    MatchesAdapter matchesAdapter;
    PremiumAdapter premiumAdapter;
    ConversationAdapter conversationAdapter;
    JSONObject jsonObject,jsonObjectConversation,jsonlikes,jsonpro;
    Loading loading;
    Apierror_handle apierror_handle;
    MessageToast messageToast;
    String apiurl,apiconversation,apilikes,apigetpro;
    @Inject
    SessionManager sessionManager;
    LinearLayout llt_empty_new_list,llt_empty_search_list,llt_premium;
    RelativeLayout rlt_empty_message,rltGold;
    TextView tv_no_match,tv_like_count,tv_gold_title,tv_messages_title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat_demo, container, false);
        AppController.getAppComponent().inject(this);
        apierror_handle=new Apierror_handle(getContext());
        loading=new Loading(getContext());
        messageToast=new MessageToast(getContext());
        rv_new_matches_list=view.findViewById(R.id.rv_new_matches_list);
        rv_message_list=view.findViewById(R.id.rv_message_list);
        rv_premium=view.findViewById(R.id.rv_premium);
        llt_empty_new_list=view.findViewById(R.id.llt_empty_new_list);
        llt_empty_search_list=view.findViewById(R.id.llt_empty_search_list);
        llt_premium=view.findViewById(R.id.llt_premium);
        rlt_empty_message=view.findViewById(R.id.rlt_empty_message);
        rltGold=view.findViewById(R.id.rltGold);
        rltGold.setOnClickListener(this);
        tv_no_match=view.findViewById(R.id.tv_no_match);
        tv_like_count=view.findViewById(R.id.tv_like_count);
        tv_gold_title=view.findViewById(R.id.tv_gold_title);
        tv_messages_title=view.findViewById(R.id.tv_messages_title);

        apiurl=getResources().getString(R.string.base_url)+getResources().getString(R.string.matchesapi);
        apigetpro=getResources().getString(R.string.base_url)+getResources().getString(R.string.getproapi);
        apiconversation=getResources().getString(R.string.base_url)+getResources().getString(R.string.conversationlistapi);
        apilikes=getResources().getString(R.string.base_url)+getResources().getString(R.string.likedapi);
        apimatches();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        apimatches();
    }

    private void apimatches() {

        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, response -> {
            try{
//                loading.hideDialog();
                jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    apiconversationlist();
                    if (jsonObject.getJSONArray("data").length()==0){
                        llt_empty_new_list.setVisibility(View.GONE);
                        llt_empty_search_list.setVisibility(View.GONE);
                        rlt_empty_message.setVisibility(View.VISIBLE);
                    }
                    else {
                        llt_empty_new_list.setVisibility(View.VISIBLE);
                        llt_empty_search_list.setVisibility(View.VISIBLE);
                        rlt_empty_message.setVisibility(View.GONE);
                        linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                        matchesAdapter=new MatchesAdapter(getContext(),jsonObject);
                        rv_new_matches_list.setLayoutManager(linearLayoutManager);
                        rv_new_matches_list.setAdapter(matchesAdapter);
                    }
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
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    private void apilikes() {
//        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, apilikes, response -> {
            try{
                loading.hideDialog();
                jsonlikes = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonlikes);
                if (jsonlikes.getString("status").equals("200"))
                {
                    api_getpro();
                    if (jsonlikes.getJSONArray("data").length()==0){
                        rltGold.setVisibility(View.GONE);
                        if (jsonObjectConversation.getJSONArray("data").length()==0){
                            tv_no_match.setVisibility(View.VISIBLE);
                            rlt_empty_message.setVisibility(View.GONE);
                        }
                        else {

                            rlt_empty_message.setVisibility(View.VISIBLE);
                            tv_no_match.setVisibility(View.GONE);
                        }

                    }
                    else {
                        if (jsonObjectConversation.getJSONArray("data").length()==0){
                            tv_no_match.setVisibility(View.VISIBLE);
                            rlt_empty_message.setVisibility(View.GONE);
                        }
                        else {

                            rlt_empty_message.setVisibility(View.GONE);
                            tv_no_match.setVisibility(View.GONE);
                        }
                        String count= String.valueOf(jsonlikes.getJSONArray("data").length());
                        tv_like_count.setText(count);
                        tv_gold_title.setText(count.concat(" Likes You Have"));
                        rltGold.setVisibility(View.VISIBLE);
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

    private void api_getpro() {
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, apigetpro, response -> {
            try{
                loading.hideDialog();
                jsonpro = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonpro);
                if (jsonpro.getString("code").equals("200"))
                {
                    if (jsonpro.getJSONArray("data").length()==0){
                        llt_premium.setVisibility(View.GONE);

                    }
                    else {
                        llt_premium.setVisibility(View.VISIBLE);
                        linearLayoutManager2=new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false);
                        premiumAdapter=new PremiumAdapter(getContext(),jsonpro);
                        rv_premium.setLayoutManager(linearLayoutManager2);
                        rv_premium.setAdapter(premiumAdapter);
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
                params.put("limit", "50");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void apiconversationlist() {
//        loading.showDialog();
        String tag_string_req = "req_login";
        StringRequest strReq = new StringRequest(Request.Method.POST, apiconversation, response -> {
            try{
//                loading.hideDialog();
                jsonObjectConversation = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObjectConversation);
                if (jsonObjectConversation.getString("code").equals("200"))
                {
                    apilikes();
                    if (jsonObjectConversation.getJSONArray("data").length()==0){

                    }
                    else {
                        rlt_empty_message.setVisibility(View.GONE);
                        tv_no_match.setVisibility(View.GONE);
                        tv_messages_title.setVisibility(View.VISIBLE);
                        rv_message_list.setVisibility(View.VISIBLE);
                        linearLayoutManager1=new LinearLayoutManager(getContext());
                        conversationAdapter=new ConversationAdapter(getContext(),jsonObjectConversation);
                        rv_message_list.setLayoutManager(linearLayoutManager1);
                        rv_message_list.setAdapter(conversationAdapter);
                    }
                }
                else
                {
                    loading.hideDialog();
                    messageToast.showDialog(jsonObjectConversation.getString("message"));

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
            case R.id.rltGold:
                Intent intent=new Intent(getContext(), LikesUserActivity.class);
                startActivity(intent);
                break;
        }
    }
}
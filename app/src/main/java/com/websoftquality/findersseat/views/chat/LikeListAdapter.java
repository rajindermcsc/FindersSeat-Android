package com.websoftquality.findersseat.views.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ChatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class LikeListAdapter extends RecyclerView.Adapter {
    Context context;
    JSONObject jsonObject,jsonAccept,jsonReject;
    int count;
    Loading loading;
    MessageToast messageToast;
    Apierror_handle apierror_handle;
    String token;
    ImageView accept,reject,message;

    public LikeListAdapter(Context context, JSONObject jsonObject, String token) {
        this.context=context;
        this.jsonObject=jsonObject;
        this.token=token;
        loading=new Loading(context);
        messageToast=new MessageToast(context);
        apierror_handle=new Apierror_handle(context);
        try {
            count=jsonObject.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.likes_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (jsonObject.getJSONArray("data").getJSONObject(position).getString("match").equalsIgnoreCase("1")){

                message.setVisibility(View.VISIBLE);
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
            }
            else {
                message.setVisibility(View.GONE);
                accept.setVisibility(View.VISIBLE);
                reject.setVisibility(View.VISIBLE);
            }
            Glide.with(context)
                    .load(jsonObject.getJSONArray("data").getJSONObject(position).getString("avater"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((MyViewHolder)holder).civ_user_image);
            ((MyViewHolder)holder).tv_other_message.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("first_name").concat(" ")
            .concat(jsonObject.getJSONArray("data").getJSONObject(position).getString("last_name")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView civ_user_image;
        TextView tv_other_message;

        public MyViewHolder(View view) {
            super(view);
            civ_user_image=view.findViewById(R.id.civ_user_image);
            tv_other_message=view.findViewById(R.id.tv_other_message);
            accept=view.findViewById(R.id.accept);
            reject=view.findViewById(R.id.reject);
            message=view.findViewById(R.id.message);
            accept.setOnClickListener(this);
            reject.setOnClickListener(this);
            message.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
                try {
                    if (v.getId()==R.id.reject) {
                        apiAccept(jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("id"),"dislikes",accept,reject,message);
                    }
                    else if (v.getId()==R.id.accept){
                        apiAccept(jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("id"),"likes",accept,reject,message);
                    }
                    else if (v.getId()==R.id.message){
                        try {
                            Intent intent=new Intent(context, ChatActivity.class);
                            intent.putExtra("id",jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("id"));
                            intent.putExtra("image",jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("avater"));
                            intent.putExtra("name",jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("first_name").concat(" ").concat(jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("last_name")));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                        catch (Exception e){
                            Log.e("TAG", "onClick: "+e.getMessage());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        private void apiAccept(String uid, String likes, ImageView accept, ImageView reject, ImageView message) {
            loading.showDialog();
            this.accept=accept;
            this.reject=reject;
            this.message=message;
            String tag_string_req = "req_login";
            StringRequest strReq = new StringRequest(Request.Method.POST, context.getResources().getString(R.string.base_url)+context.getResources().getString(R.string.addlikesapi), response -> {
                try{
                    loading.hideDialog();
                    jsonAccept = new JSONObject(response);
                    Log.e("TAG", "onResponse: "+jsonAccept);
                    if (jsonAccept.getString("code").equals("200"))
                    {
                        if (likes.equalsIgnoreCase("likes")){
                            message.setVisibility(View.VISIBLE);
                            accept.setVisibility(View.GONE);
                            reject.setVisibility(View.GONE);
                            messageToast.showDialog("Request Accepted");
                        }
                        else {
                            messageToast.showDialog("Request Rejected");

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
                    Log.e("TAG", "getParams: "+uid);
                    params.put("access_token", token);
                    if (likes.equalsIgnoreCase("likes")) {
                        params.put("likes", uid);
                        params.put("dislikes", "");
                    }
                    else {
                        params.put("likes", "");
                        params.put("dislikes", uid);
                    }
                    return params;
                }
            };
            strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}


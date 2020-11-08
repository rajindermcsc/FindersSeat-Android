package com.websoftquality.findersseat.views.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.websoftquality.findersseat.R;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversationAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ConversationAdapter";
    Context context;
    JSONObject jsonObject;
    int count;
    public ConversationAdapter(Context context, JSONObject jsonObject) {
        this.context=context;
        this.jsonObject=jsonObject;
        try {
            Log.e(TAG, "ConversationAdapter: "+jsonObject);
            count=jsonObject.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.conversation_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            Glide.with(context)
                    .load(jsonObject.getJSONArray("data").getJSONObject(position).getJSONObject("user").getString("avater"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((MyViewHolder)holder).civ_other_user_image);
            ((MyViewHolder)holder).tv_other_message.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("text"));
            ((MyViewHolder)holder).tv_time.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView civ_other_user_image;
        TextView tv_other_message,tv_time;
        RelativeLayout rlt_other_user_chat;
        public MyViewHolder(View view) {
            super(view);
            civ_other_user_image=view.findViewById(R.id.civ_other_user_image);
            tv_other_message=view.findViewById(R.id.tv_other_message);
            tv_time=view.findViewById(R.id.tv_time);
            rlt_other_user_chat=view.findViewById(R.id.rlt_other_user_chat);
            rlt_other_user_chat.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.rlt_other_user_chat:
                    try {
                        Intent intent = new Intent(context, ChatActivity.class);
                        intent.putExtra("name", jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getJSONObject("user").getString("fullname"));
                        intent.putExtra("id", jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getJSONObject("user").getString("id"));
                        intent.putExtra("image", jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getJSONObject("user").getString("avater"));
                        context.startActivity(intent);
                    }
                    catch (Exception e){
                        Log.e(TAG, "onClick: "+e.getMessage());
                    }
                    break;
            }
        }
    }
}

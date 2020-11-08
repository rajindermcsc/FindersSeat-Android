package com.websoftquality.findersseat.views.chat;

import android.content.Context;
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

public class ChatConversationAdapter extends RecyclerView.Adapter {
    private static final String TAG = "ChatConversationAdapter";
    Context context;
    JSONObject jsonlikes;

    int count,userId;
    public ChatConversationAdapter(Context context, JSONObject jsonlikes, int userId) {
        this.context=context;
        this.jsonlikes=jsonlikes;
        this.userId=userId;
        try {
            Log.e(TAG, "MatchesAdapter: "+jsonlikes);
            count=jsonlikes.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chat_conversation_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            if (jsonlikes.getJSONArray("data").getJSONObject(position).getString("from").equalsIgnoreCase(String.valueOf(userId))){
                ((MyViewHolder)holder).rlt_user_chat.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).tv_user_message.setText(jsonlikes.getJSONArray("data").getJSONObject(position).getString("text"));
                Glide.with(context)
                        .load(jsonlikes.getJSONArray("data").getJSONObject(position).getString("from_avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((MyViewHolder)holder).civ_user_image);
            }
            else {
                ((MyViewHolder)holder).rlt_other_user_chat.setVisibility(View.VISIBLE);
                ((MyViewHolder)holder).tv_other_message.setText(jsonlikes.getJSONArray("data").getJSONObject(position).getString("text"));
                Glide.with(context)
                        .load(jsonlikes.getJSONArray("data").getJSONObject(position).getString("from_avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(((MyViewHolder)holder).civ_other_user_image);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        CircleImageView civ_user_image,civ_other_user_image;
        RelativeLayout rlt_user_chat,rlt_other_user_chat;
        TextView tv_user_message,tv_other_message;
        public MyViewHolder(View view) {
            super(view);
            civ_user_image=view.findViewById(R.id.civ_user_image);
            civ_other_user_image=view.findViewById(R.id.civ_other_user_image);
            rlt_user_chat=view.findViewById(R.id.rlt_user_chat);
            rlt_other_user_chat=view.findViewById(R.id.rlt_other_user_chat);
            tv_user_message=view.findViewById(R.id.tv_user_message);
            tv_other_message=view.findViewById(R.id.tv_other_message);
        }
    }
}

package com.websoftquality.findersseat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.views.chat.ConversationAdapter;
import com.websoftquality.findersseat.views.main.OtherProfileView;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificationAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NotificationAdapter";
    Context context;
    JSONObject jsonObject;
    int count;
    public NotificationAdapter(Context context, JSONObject jsonObject) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.notification_adapter_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            Glide.with(context)
                    .load(jsonObject.getJSONArray("data").getJSONObject(position).getJSONObject("notifier").getString("avater"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((MyViewHolder)holder).iv_notifications);
            if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("got_new_match")){

                ((MyViewHolder)holder).tv_notifications.setText("You got a new match.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("visit")){

                ((MyViewHolder)holder).tv_notifications.setText("You got a new visit.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("like")){

                ((MyViewHolder)holder).tv_notifications.setText("You got a new like.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("dislike")){

                ((MyViewHolder)holder).tv_notifications.setText("You got a new Dislike.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("send_gift")){

                ((MyViewHolder)holder).tv_notifications.setText("You got a new gift.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("message")){

                ((MyViewHolder)holder).tv_notifications.setText("You got a new message.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("accept_chat_request")){

                ((MyViewHolder)holder).tv_notifications.setText("Someone accepted your chat request.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("decline_chat_request")){

                ((MyViewHolder)holder).tv_notifications.setText("Someone rejected your chat request.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("create_story")){

                ((MyViewHolder)holder).tv_notifications.setText("Someone created a new story with you.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("approve_story")){

                ((MyViewHolder)holder).tv_notifications.setText("Your story has been approved.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("disapprove_story")){

                ((MyViewHolder)holder).tv_notifications.setText("Your story has been disapproved.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("approve_receipt")){

                ((MyViewHolder)holder).tv_notifications.setText("Your bank transfer has been approved.");
            }else if (jsonObject.getJSONArray("data").getJSONObject(position).getString("type").equalsIgnoreCase("disapprove_receipt")){

                ((MyViewHolder)holder).tv_notifications.setText("We have rejected your bank transfer, please contact us for more details.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_notifications;
        CircleImageView iv_notifications;
        LinearLayout ll_main;
        public MyViewHolder(View view) {
            super(view);
            tv_notifications=view.findViewById(R.id.tv_notifications);
            iv_notifications=view.findViewById(R.id.iv_notifications);
            ll_main=view.findViewById(R.id.ll_main);
            ll_main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.ll_main){
                try {
                    if (jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("type").equalsIgnoreCase("like")){

                        Intent intent=new Intent(context, OtherProfileView.class);
                        intent.putExtra("id",jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("notifier_id"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

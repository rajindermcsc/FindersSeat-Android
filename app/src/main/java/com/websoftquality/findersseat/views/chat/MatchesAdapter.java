package com.websoftquality.findersseat.views.chat;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.adapters.chat.UserListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchesAdapter extends RecyclerView.Adapter {
    private static final String TAG = "MatchesAdapter";
    Context context;
    JSONObject jsonObject;
    int count;
    public MatchesAdapter(Context context, JSONObject jsonObject) {
        this.context=context;
        this.jsonObject=jsonObject;
        try {
            Log.e(TAG, "MatchesAdapter: "+jsonObject);
            count=jsonObject.getJSONArray("data").length();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_new_matches_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            ((MyViewHolder)holder).tv_user_name.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("first_name").concat(" ")
                    .concat(jsonObject.getJSONArray("data").getJSONObject(position).getString("last_name")));
            Glide.with(context)
                    .load(jsonObject.getJSONArray("data").getJSONObject(position).getString("avater"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(((MyViewHolder)holder).civ_match_user_image);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: "+e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return count;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_user_name;
        CircleImageView civ_match_user_image;
        RelativeLayout llt_new_matches;
        public MyViewHolder(View view) {
            super(view);
            tv_user_name=view.findViewById(R.id.tv_user_name);
            civ_match_user_image=view.findViewById(R.id.civ_match_user_image);
            llt_new_matches=view.findViewById(R.id.llt_new_matches);
            llt_new_matches.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.llt_new_matches:
                    try {
                        Intent intent=new Intent(context, ChatActivity.class);
                        intent.putExtra("name",tv_user_name.getText().toString());
                        intent.putExtra("id",jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("id"));
                        intent.putExtra("image",jsonObject.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("avater"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}

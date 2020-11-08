package com.websoftquality.findersseat.views.main;

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
import com.websoftquality.findersseat.views.chat.MatchesAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class PremiumAdapter extends RecyclerView.Adapter {
    private static final String TAG = "PremiumAdapter";
    Context context;
    JSONObject jsonpro;
    int count;
    public PremiumAdapter(Context context, JSONObject jsonpro) {
        this.context=context;
        this.jsonpro=jsonpro;
        try {
            Log.e(TAG, "MatchesAdapter: "+jsonpro);
            count=jsonpro.getJSONArray("data").length();
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
            ((MyViewHolder)holder).tv_user_name.setText(jsonpro.getJSONArray("data").getJSONObject(position).getString("first_name").concat(" ")
                    .concat(jsonpro.getJSONArray("data").getJSONObject(position).getString("last_name")));
            Glide.with(context)
                    .load(jsonpro.getJSONArray("data").getJSONObject(position).getString("avater"))
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
            tv_user_name = view.findViewById(R.id.tv_user_name);
            civ_match_user_image = view.findViewById(R.id.civ_match_user_image);
            llt_new_matches = view.findViewById(R.id.llt_new_matches);
            llt_new_matches.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llt_new_matches:
                    try {
                        Intent intent = new Intent(context, OtherProfileView.class);
                        intent.putExtra("id", jsonpro.getJSONArray("data").getJSONObject(getAdapterPosition()).getString("id"));
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

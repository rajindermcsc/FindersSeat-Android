package com.websoftquality.findersseat.adapters.matches;
/**
 * @package com.trioangle.igniter
 * @subpackage adapters.matches
 * @category MatchesSwipeAdapter
 * @author Trioangle Product Team
 * @version 1.0
 **/

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.obs.CustomTextView;

import java.util.ArrayList;

import javax.inject.Inject;

import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.datamodels.matches.MatchingProfile;
import com.websoftquality.findersseat.utils.ImageUtils;
import com.websoftquality.findersseat.views.profile.EnlargeProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

/*****************************************************************
 Adapter for matched swipe user
 ****************************************************************/

public class MatchesSwipeAdapter extends BaseAdapter {

    @Inject
    ImageUtils imageUtils;
    @Inject
    SessionManager sessionManager;
    private Context context;
    private LayoutInflater inflater;
    JSONObject jsonObject;
    int count;

    public MatchesSwipeAdapter(JSONObject jsonObject, Context context) {
        this.context = context;
        this.jsonObject = jsonObject;
        this.inflater = LayoutInflater.from(context);
        try {
            Log.e("TAG", "MatchesSwipeAdapter: "+jsonObject);
            count=jsonObject.getJSONArray("data").length();
            Log.e("TAG", "MatchesSwipeAdapter: "+count);
        } catch (JSONException e) {
            Log.e("TAG", "MatchesSwipeAdapter: "+e.getMessage());
            e.printStackTrace();
        }
        AppController.getAppComponent().inject(this);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        try {
            return jsonObject.getJSONArray("data").get(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            v = inflater.inflate(R.layout.swipe_card_item, parent, false);
        }
        ImageView userImage = v.findViewById(R.id.iv_user_image);
        CustomTextView userNameAge = v.findViewById(R.id.tv_user_name_age);
        CustomTextView userDesignation = v.findViewById(R.id.tv_designation);
        CustomTextView userProfession = v.findViewById(R.id.tv_profession);
        CustomTextView userQuestion = v.findViewById(R.id.tv_question2);
        ImageView superlike = v.findViewById(R.id.superlike);
        LinearLayout linearLayout = v.findViewById(R.id.linearLayout2);

        final MatchingProfile currentUser;
        try {
//            currentUser = jsonObject.getJSONArray("data").get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        if (currentUser != null) {
            //fill user Image
        try {
            imageUtils.loadSliderImage(context, userImage, jsonObject.getJSONArray("data").getJSONObject(position).getString("avater"));
            superlike.setVisibility(View.GONE);
            userNameAge.setText(new StringBuilder().append(jsonObject.getJSONArray("data").getJSONObject(position).getString("username")));
            userDesignation.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("church_attend"));
            userProfession.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("question1"));
            userQuestion.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("question2"));
//            userDesignation.setText(jsonObject.getJSONArray("data").getJSONObject(position).getString("location"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //fill user details
//            if (!TextUtils.isEmpty(currentUser.getName()) && currentUser.getAge() > 0) {
//                userNameAge.setText(new StringBuilder().append(currentUser.getName()).append(", ").append(currentUser.getAge()).toString());
//            } else {
//                userNameAge.setText(new StringBuilder().append(currentUser.getName()));
//            }
//
//            if (!TextUtils.isEmpty(currentUser.getWork())) {
//                userDesignation.setVisibility(View.VISIBLE);
//                userDesignation.setText(currentUser.getWork());
//            } else {
//                userDesignation.setVisibility(View.GONE);
//            }
//
//            if (!TextUtils.isEmpty(currentUser.getCollege())) {
//                userProfession.setVisibility(View.VISIBLE);
//                userProfession.setText(currentUser.getCollege());
//            } else {
//                userProfession.setVisibility(View.GONE);
//            }
//
//            if (!TextUtils.isEmpty(currentUser.getSuperLike()) && currentUser.getSuperLike().equalsIgnoreCase("yes")) {
//                superlike.setVisibility(View.VISIBLE);
//            } else {
//                superlike.setVisibility(View.GONE);
//            }
//        }

        /*linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EnlargeProfileActivity.class);
                intent.putExtra("navType", 1);
                intent.putExtra("userId",  currentUser.getUserId());
                context.startActivity(intent);
            }
        });*/

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(context, EnlargeProfileActivity.class);
                intent.putExtra("navType", 1);
                intent.putExtra("userId",  currentUser.getUserId());
                context.startActivity(intent);*/

                int x = sessionManager.getTouchX();
                int y = sessionManager.getTouchY();
                if (x <= (v.getRight() / 2) && ((v.getBottom() / 3) * 2.5) >= y) {
                } else if (x > (v.getRight() / 2) && ((v.getBottom() / 3) * 2.2) >= y) {
                } else {
                    Intent intent = new Intent(context, EnlargeProfileActivity.class);
                    intent.putExtra("navType", 1);
//                    intent.putExtra("userId", currentUser.getUserId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.ub__fade_in, R.anim.ub__fade_out);
                }
            }
        });

        return v;
    }

}

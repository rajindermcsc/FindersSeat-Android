package com.websoftquality.findersseat.views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.adapters.profile.EnlargeSliderAdapter;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.views.customize.CirclePageIndicator;
import com.websoftquality.findersseat.views.customize.IgniterViewPager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EnlargeProfile extends AppCompatActivity implements View.OnClickListener {
    JSONObject jsonObject;
    Intent intent;
    TextView tv_user_name_age;
    private IgniterViewPager viewPager;
    private CirclePageIndicator pageIndicator;
    ArrayList<String> listimages=new ArrayList<>();
    RelativeLayout rtl_arrow_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enlarge_profile);
        intent=getIntent();
        tv_user_name_age=findViewById(R.id.tv_user_name_age);
        viewPager = findViewById(R.id.vp_enlarge_profile);
        pageIndicator = findViewById(R.id.cpi_enlarge_profile);
        rtl_arrow_down = findViewById(R.id.rtl_arrow_down);
        rtl_arrow_down.setOnClickListener(this);
        try {
            listimages.clear();

            jsonObject=new JSONObject(intent.getStringExtra("jsonObject"));

            tv_user_name_age.setText(jsonObject.getJSONObject("data").getString("username")+", "+
                    jsonObject.getJSONObject("data").getString("age"));

            listimages.add(jsonObject.getJSONObject("data").getString("avater"));
            for (int i=0;i<jsonObject.getJSONObject("data").getJSONArray("mediafiles").length();i++){
                listimages.add(jsonObject.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(i).getString("avater"));
            }
            Log.e("TAG", "onCreate: "+listimages);
            if (jsonObject.getJSONObject("data").getJSONArray("mediafiles").length()==0){

            }
            else {

                viewPager.setAdapter(new EnlargeSliderAdapter(this,listimages));
                pageIndicator.setViewPager(viewPager, 0);
            }
            initPageIndicator();
            Log.e("TAG", "onCreate: "+jsonObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPageIndicator() {
        final float density = getResources().getDisplayMetrics().density;
        pageIndicator.setRadius(3 * density);
        pageIndicator.setPageColor(ContextCompat.getColor(this, R.color.gray_text_color));
        pageIndicator.setFillColor(ContextCompat.getColor(this, R.color.color_accent));
        pageIndicator.setStrokeColor(ContextCompat.getColor(this, R.color.gray_text_color));
        pageIndicator.setOnClickListener(null);
        pageIndicator.setExtraSpacing((float) (1.5 * density));
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.rtl_arrow_down){
            onBackPressed();
        }
    }
}
package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.interfaces.OnRangeSeekBarChangeListener;
import com.websoftquality.findersseat.interfaces.OnRangeSeekBarFinalValueListener;
import com.websoftquality.findersseat.interfaces.OnSeekBarChangeListener;
import com.websoftquality.findersseat.interfaces.OnSeekBarFinalValueListener;
import com.websoftquality.findersseat.utils.CommonMethods;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.views.customize.CustomDialog;
import com.websoftquality.findersseat.views.customize.CustomSeekBar;
import com.websoftquality.findersseat.views.customize.RangeSeekBar;
import com.websoftquality.findersseat.views.profile.SettingsActivity;

import javax.inject.Inject;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView back_button,save_button;
    MessageToast messageToast;
    CardView cv_help_support,cv_logout;
    TextView tv_license,tv_privacy_policy,tv_terms_service,tvMaxDistance,tvAgeRange;
    @Inject
    CommonMethods commonMethods;
    @Inject
    CustomDialog customDialog;
    @Inject
    SessionManager sessionManager;
    private AlertDialog dialog;
    private SeekBar sbMaxDistance;
    private RangeSeekBar sbAgeRange;
    private String maxDistance, minAge, maxAge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        AppController.getAppComponent().inject(this);
        tvMaxDistance=findViewById(R.id.tvMaxDistance);
        tvAgeRange=findViewById(R.id.tvAgeRange);
        back_button=findViewById(R.id.back_button);
        save_button=findViewById(R.id.save_button);
        cv_help_support=findViewById(R.id.cv_help_support);
        cv_logout=findViewById(R.id.cv_logout);
        tv_license=findViewById(R.id.tv_license);
        tv_privacy_policy=findViewById(R.id.tv_privacy_policy);
        tv_terms_service=findViewById(R.id.tv_terms_service);
        messageToast=new MessageToast(this);
        back_button.setOnClickListener(this);
        cv_help_support.setOnClickListener(this);
        tv_license.setOnClickListener(this);
        tv_privacy_policy.setOnClickListener(this);
        tv_terms_service.setOnClickListener(this);
        save_button.setOnClickListener(this);
        cv_logout.setOnClickListener(this);
        sbAgeRange = findViewById(R.id.sb_age_range);
        sbMaxDistance = findViewById(R.id.sb_max_distance);
        sbMaxDistance.setProgress(Integer.parseInt(sessionManager.getDistance()));
        dialog = commonMethods.getAlertDialog(this);
        tvMaxDistance.setText(String.format(getString(R.string.miles), sessionManager.getDistance()));

        sbAgeRange.setMaxValue(55).setMinValue(18).apply();
        sbAgeRange.setMaxStartValue(Float.parseFloat(sessionManager.getMaxAge())).setMinStartValue(Float.parseFloat(sessionManager.getMinAge())).apply();

        tvAgeRange.setText(String.format(getString(R.string.age), sessionManager.getMinAge(), sessionManager.getMaxAge()));

        initSeekBarChangeListener();
    }


    private void initSeekBarChangeListener() {
        sbMaxDistance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                sbMaxDistance.setProgress(progress);
                tvMaxDistance.setText(String.format(getString(R.string.miles), String.valueOf(progress)));
                maxDistance = String.valueOf(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbAgeRange.setOnRangeSeekbarChangeListener(new OnRangeSeekBarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                String max = String.valueOf(maxValue);
                minAge = String.valueOf(minValue);
                maxAge = max;
                if (max.equals("55")) max = max + "+";

                tvAgeRange.setText(String.format(getString(R.string.age), minValue, max));
            }
        });

        sbAgeRange.setOnRangeSeekbarFinalValueListener(new OnRangeSeekBarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {

            }
        });
    }

    private void loadUrl(String url) {
        System.out.println("url " + url);
        if (!TextUtils.isEmpty(url)) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialog(String title, String msg, String buttonText, final int index) {

        customDialog = new CustomDialog(index, title, msg, buttonText, getResources().getString(R.string.cancel), new CustomDialog.btnAllowClick() {
            @Override
            public void clicked() {
                sessionManager.clearToken();
                sessionManager.clearAll();
                finishAffinity();
                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        }, null);
        customDialog.show(SettingActivity.this.getSupportFragmentManager(), "");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cv_logout:
                String msg = getResources().getString(R.string.suretolog);
                String title = getResources().getString(R.string.logout) + "?";
                String btnText = getResources().getString(R.string.logout);
                showDialog(title, msg, btnText, 0);
                break;
            case R.id.tv_license:
                loadUrl("http://findersseat.com//about");
                break;
            case R.id.cv_help_support:
                loadUrl("http://findersseat.com//contact");
                break;
            case R.id.tv_privacy_policy:
                loadUrl("http://findersseat.com//privacy");
                break;
            case R.id.tv_terms_service:
                loadUrl("http://findersseat.com//terms");
                break;
            case R.id.back_button:
                onBackPressed();
                break;
            case R.id.save_button:
                messageToast.showDialog("Settings Saved Successfully");
                sessionManager.setMinAge(String.valueOf(sbAgeRange.getSelectedMinValue()));
                sessionManager.setMaxAge(String.valueOf(sbAgeRange.getSelectedMaxValue()));
                sessionManager.setDistance(String.valueOf(sbMaxDistance.getProgress()));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                },2500);
                break;
        }
    }
}
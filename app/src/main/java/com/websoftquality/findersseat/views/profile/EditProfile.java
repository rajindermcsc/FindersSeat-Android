package com.websoftquality.findersseat.views.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.obs.CustomTextView;
import com.obs.image_cropping.CropImage;
import com.obs.image_cropping.ImageMinimumSizeCalculator;
import com.websoftquality.findersseat.BuildConfig;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.backgroundtask.ImageCompressAsyncTask;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.Constants;
import com.websoftquality.findersseat.configs.RunTimePermission;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.interfaces.ImageListener;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.CommonMethods;
import com.websoftquality.findersseat.utils.CustomSpinnerAdapterr;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.utils.RequiredPermissions;
import com.websoftquality.findersseat.utils.SingleUploadBroadcastReceiver;
import com.websoftquality.findersseat.views.customize.CustomDialog;
import com.websoftquality.findersseat.views.main.HomeActivity;
import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;

import static com.android.volley.VolleyLog.TAG;

public class EditProfile extends AppCompatActivity implements View.OnClickListener, SingleUploadBroadcastReceiver.Delegate, ImageListener {
    Spinner spinner_height,spinner_hair_color,spinner_country,spinner_gender,spinner_work,spinner_education_level,
            spinner_language,spinner_ethnicity,spinner_body_type,spinner_character,spinner_children,spinner_friends,
            spinner_pets,spinner_live,spinner_car,spinner_smoke,spinner_drink,spinner_travel;
    EditText edt_phone_number,edt_about_me,edt_location;
    ArrayList<String> height=new ArrayList<>();
    ArrayList<String> haricolor=new ArrayList<>();
    ArrayList<String> country= new ArrayList<>();
    ArrayList<String> gender=new ArrayList<>();
    ArrayList<String> work=new ArrayList<>();
    ArrayList<String> education=new ArrayList<>();
    ArrayList<String> language=new ArrayList<>();
    ArrayList<String> ethnicity=new ArrayList<>();
    ArrayList<String> body=new ArrayList<>();
    ArrayList<String> character=new ArrayList<>();
    ArrayList<String> children=new ArrayList<>();
    ArrayList<String> friends=new ArrayList<>();
    ArrayList<String> pets=new ArrayList<>();
    ArrayList<String> live=new ArrayList<>();
    ArrayList<String> car=new ArrayList<>();
    ArrayList<String> smoke=new ArrayList<>();
    ArrayList<String> drink=new ArrayList<>();
    ArrayList<String> travel=new ArrayList<>();
    CustomSpinnerAdapterr customSpinnerAdapterr;
    TextView text_birthday;
    JSONObject jsonObject;
    JSONArray jsonArray;
    Loading loading;
    MessageToast messageToast;
    String apiurl;
    Apierror_handle apierror_handle;
    @Inject
    SessionManager sessionManager;
    DatePickerDialog picker;
    String dob;
    String[] heigh_;
    String height_int,hair_int,country_int,gender_int,heighttext;
    String work_int="1";
    String education_int="1";
    String language_int="English";
    String ethnicity_int="1";
    String body_int="1";
    String character_int="1";
    String children_int="1";
    String friends_int="1";
    String pets_int="1";
    String live_int="1";
    String smoke_int="1";
    String drink_int="1";
    String car_int="1";
    String travel_int="1";
    @Inject
    RunTimePermission runTimePermission;
    @Inject
    CustomDialog customDialog;
    private File imageFile = null;
    private Uri imageUri;
    private String imagePath = "";
    @Inject
    CommonMethods commonMethods;
    public final SingleUploadBroadcastReceiver uploadReceiver = new SingleUploadBroadcastReceiver();
    Context context;
    MultipartUploadRequest uploadRequest;
    private boolean isDelete = false;
    EditProfile mActivity;
    RequiredPermissions requiredPermissions;
    public static final int RequestPermissionCode = 7;
    private AlertDialog dialog;
    private RelativeLayout rltProfileImageOne, rltProfileImageTwo, rltProfileImageThree, rltProfileImageFour, rltProfileImageFive, rltProfileImageSix;
    private ImageView ivUserImageOne, ivUserImageTwo, ivUserImageThree, ivUserImageFour, ivUserImageFive, ivUserImageSix;
    private ImageView tvAddIconOne, tvAddIconTwo, tvAddIconThree, tvAddIconFour, tvAddIconFive, tvAddIconSix;
    private ImageView tvCloseIconOne, tvCloseIconTwo, tvCloseIconThree, tvCloseIconFour, tvCloseIconFive, tvCloseIconSix;
    JSONObject jsonObjectProfile;
    Intent intent;
//    CircleImageView profile_pic;
    ImageView back_button,save_button;
    int clickPos = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        AppController.getAppComponent().inject(this);
        intent=getIntent();
        try {
            jsonObjectProfile=new JSONObject(intent.getStringExtra("jsonObject"));
            Log.e(TAG, "onCreate: "+jsonObjectProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        profile_pic = findViewById(R.id.profile_pic);
        back_button=findViewById(R.id.back_button);
        save_button=findViewById(R.id.save_button);
//        profile_pic.setOnClickListener(this);
        back_button.setOnClickListener(this);
        save_button.setOnClickListener(this);
        rltProfileImageOne = findViewById(R.id.rlt_profile_image_one);
        rltProfileImageTwo = findViewById(R.id.rlt_profile_image_two);
        rltProfileImageThree = findViewById(R.id.rlt_profile_image_three);
        rltProfileImageFour = findViewById(R.id.rlt_profile_image_four);
        rltProfileImageFive = findViewById(R.id.rlt_profile_image_five);
        rltProfileImageSix = findViewById(R.id.rlt_profile_image_six);

        ivUserImageOne = rltProfileImageOne.findViewById(R.id.iv_user_image);
        ivUserImageTwo = rltProfileImageTwo.findViewById(R.id.iv_user_image);
        ivUserImageThree = rltProfileImageThree.findViewById(R.id.iv_user_image);
        ivUserImageFour = rltProfileImageFour.findViewById(R.id.iv_user_image);
        ivUserImageFive = rltProfileImageFive.findViewById(R.id.iv_user_image);
        ivUserImageSix = rltProfileImageSix.findViewById(R.id.iv_user_image);

        tvAddIconOne = rltProfileImageOne.findViewById(R.id.tv_add_icon);
        tvAddIconTwo = rltProfileImageTwo.findViewById(R.id.tv_add_icon);
        tvAddIconThree = rltProfileImageThree.findViewById(R.id.tv_add_icon);
        tvAddIconFour = rltProfileImageFour.findViewById(R.id.tv_add_icon);
        tvAddIconFive = rltProfileImageFive.findViewById(R.id.tv_add_icon);
        tvAddIconSix = rltProfileImageSix.findViewById(R.id.tv_add_icon);

        tvCloseIconOne = rltProfileImageOne.findViewById(R.id.tv_close_icon);
        tvCloseIconTwo = rltProfileImageTwo.findViewById(R.id.tv_close_icon);
        tvCloseIconThree = rltProfileImageThree.findViewById(R.id.tv_close_icon);
        tvCloseIconFour = rltProfileImageFour.findViewById(R.id.tv_close_icon);
        tvCloseIconFive = rltProfileImageFive.findViewById(R.id.tv_close_icon);
        tvCloseIconSix = rltProfileImageSix.findViewById(R.id.tv_close_icon);



        tvAddIconOne.setTag(1);
        tvAddIconTwo.setTag(2);
        tvAddIconThree.setTag(3);
        tvAddIconFour.setTag(4);
        tvAddIconFive.setTag(5);
        tvAddIconSix.setTag(6);

        tvCloseIconOne.setTag(1);
        tvCloseIconTwo.setTag(2);
        tvCloseIconThree.setTag(3);
        tvCloseIconFour.setTag(4);
        tvCloseIconFive.setTag(5);
        tvCloseIconSix.setTag(6);

        tvAddIconOne.setOnClickListener(this);
        tvAddIconTwo.setOnClickListener(this);
        tvAddIconThree.setOnClickListener(this);
        tvAddIconFour.setOnClickListener(this);
        tvAddIconFive.setOnClickListener(this);
        tvAddIconSix.setOnClickListener(this);

        tvCloseIconOne.setOnClickListener(this);
        tvCloseIconTwo.setOnClickListener(this);
        tvCloseIconThree.setOnClickListener(this);
        tvCloseIconFour.setOnClickListener(this);
        tvCloseIconFive.setOnClickListener(this);
        tvCloseIconSix.setOnClickListener(this);

        try {
            Log.e(TAG, "onCreateprofile: "+jsonObjectProfile.getJSONObject("data"));
            Log.e(TAG, "onCreateprofile: "+jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles"));
            Log.e(TAG, "onCreateprofile@@: "+jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"));
            if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==0){

            }
            else if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==1) {
                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageOne);
            }else if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==2) {
                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageOne);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(1).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageTwo);
            }else if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==3) {
                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageOne);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(1).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageTwo);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(2).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageThree);
            }else if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==4) {
                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageOne);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(1).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageTwo);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(2).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageThree);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(3).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageFour);
            }
            else if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==5) {
                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageOne);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(1).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageTwo);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(2).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageThree);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(3).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageFour);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(4).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageFive);
            }

            else if (jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").length()==6) {
                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(0).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageOne);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(1).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageTwo);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(2).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageThree);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(3).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageFour);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(4).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageFive);

                Glide.with(this)
                        .load(jsonObjectProfile.getJSONObject("data").getJSONArray("mediafiles").getJSONObject(5).getString("avater"))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivUserImageSix);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: "+e.getMessage());
        }


        spinner_height=findViewById(R.id.spinner_height);
        spinner_hair_color=findViewById(R.id.spinner_hair_color);
        spinner_country=findViewById(R.id.spinner_country);
        spinner_gender=findViewById(R.id.spinner_gender);
        spinner_work=findViewById(R.id.spinner_work);
        spinner_education_level=findViewById(R.id.spinner_education_level);
        spinner_language=findViewById(R.id.spinner_language);
        spinner_ethnicity=findViewById(R.id.spinner_ethnicity);
        spinner_body_type=findViewById(R.id.spinner_body_type);
        spinner_character=findViewById(R.id.spinner_character);
        spinner_children=findViewById(R.id.spinner_children);
        spinner_friends=findViewById(R.id.spinner_friends);
        spinner_pets=findViewById(R.id.spinner_pets);
        spinner_live=findViewById(R.id.spinner_live);
        spinner_car=findViewById(R.id.spinner_car);
        spinner_smoke=findViewById(R.id.spinner_smoke);
        spinner_drink=findViewById(R.id.spinner_drink);
        spinner_travel=findViewById(R.id.spinner_travel);
        text_birthday=findViewById(R.id.text_birthday);
        edt_phone_number=findViewById(R.id.edt_phone_number);
        edt_about_me=findViewById(R.id.edt_about_me);
        edt_location=findViewById(R.id.edt_location);
        text_birthday.setOnClickListener(this);
        loading=new Loading(this);
        requiredPermissions = new RequiredPermissions(this);
        apierror_handle=new Apierror_handle(this);
        messageToast=new MessageToast(this);
        dialog = commonMethods.getAlertDialog(EditProfile.this);
        height.add("Height");
        height.add("3.0 Feet");
        height.add("3.2 Feet");
        height.add("3.4 Feet");
        height.add("3.6 Feet");
        height.add("3.8 Feet");
        height.add("4.0 Feet");
        height.add("4.2 Feet");
        height.add("4.4 Feet");
        height.add("4.6 Feet");
        height.add("4.8 Feet");
        height.add("5.0 Feet");
        height.add("5.2 Feet");
        height.add("5.4 Feet");
        height.add("5.6 Feet");
        height.add("5.8 Feet");
        height.add("6.0 Feet");
        height.add("6.2 Feet");
        height.add("6.4 Feet");
        height.add("6.6 Feet");
        height.add("6.8 Feet");
        height.add("7.0 Feet");
        height.add("7.2 Feet");
        height.add("7.4 Feet");
        height.add("7.6 Feet");
        height.add("7.8 Feet");
        height.add("8.0 Feet");

        haricolor.add("Choose your Hair Color");
        haricolor.add("Brown");
        haricolor.add("Black");
        haricolor.add("White");
        haricolor.add("Sandy");
        haricolor.add("Gray or Partially Gray");
        haricolor.add("Red/Auburn");
        haricolor.add("Blond/Strawberry");
        haricolor.add("Blue");
        haricolor.add("Green");
        haricolor.add("Orange");
        haricolor.add("Pink");
        haricolor.add("Purple");
        haricolor.add("Partly or Completely Bald");
        haricolor.add("Other");

        gender.add("Choose your Gender");
        gender.add("Male");
        gender.add("Female");

        work.add("I'm studying");
        work.add("I'm working");
        work.add("I'm looking for work");
        work.add("I'm retired");
        work.add("Self-Employed");
        work.add("Other");
        education.add("Secondary school");
        education.add("ITI");
        education.add("College");
        education.add("University");
        education.add("Advanced degree");
        education.add("Other");
        language.add("English");
        language.add("Arabic");
        language.add("Dutch");
        language.add("French");
        language.add("German");
        language.add("Italian");
        language.add("Portuguese");
        language.add("Russian");
        language.add("Spanish");
        language.add("Turkish");
        ethnicity.add("White");
        ethnicity.add("Black");
        ethnicity.add("Middle Eastern");
        ethnicity.add("North African");
        ethnicity.add("Latin American");
        ethnicity.add("Mixed");
        ethnicity.add("Asian");
        ethnicity.add("Other");
        body.add("Slim");
        body.add("Sporty");
        body.add("Curvy");
        body.add("Round");
        body.add("Supermodel");
        body.add("Average");
        body.add("Other");
        character.add("Accommodating");
        character.add("Adventurous");
        character.add("Calm");
        character.add("Careless");
        character.add("Cheerful");
        character.add("Demanding");
        character.add("Extroverted");
        character.add("Honest");
        character.add("Generous");
        character.add("Humorous");
        character.add("Introverted");
        character.add("Liberal");
        character.add("Lively");
        character.add("Loner");
        character.add("Nervous");
        character.add("Possessive");
        character.add("Quiet");
        character.add("Reserved");
        character.add("Sensitive");
        character.add("Shy");
        character.add("Social");
        character.add("Spontaneous");
        character.add("Stubborn");
        character.add("Suspicious");
        character.add("Thoughtful");
        character.add("Proud");
        character.add("Considerate");
        character.add("Friendly");
        character.add("Polite");
        character.add("Reliable");
        character.add("Careful");
        character.add("Helpful");
        character.add("Patient");
        character.add("Optimistic");
        children.add("No, never");
        children.add("Someday, maybe");
        children.add("Expecting");
        children.add("I already have kids");
        children.add("I have kids and don't want more");
        friends.add("No friends");
        friends.add("Some friends");
        friends.add("Many friends");
        friends.add("Only good friends");
        pets.add("None");
        pets.add("Have pets");
        live.add("Alone");
        live.add("Parents");
        live.add("Friends");
        live.add("Partner");
        live.add("Children");
        car.add("Other");
        car.add("My own car");
        smoke.add("Never");
        smoke.add("I smoke sometimes");
        smoke.add("Chain Smoker");
        drink.add("Never");
        drink.add("I drink sometimes");
        travel.add("Yes, all the time");
        travel.add("Yes, sometimes");
        travel.add("Not very much");
        travel.add("No");

        setImageViewCount();
        try {
            String json=loadJSONFromAsset(getApplicationContext());

//            jsonObject=new JSONObject(json);
            jsonArray=new JSONArray(json);
            for (int i=0;i<jsonArray.length();i++){
                country.add(jsonArray.getJSONObject(i).getString("country_name"));
            }
//            Log.e("TAG", "onCreatecountries: "+jsonObject);
            Log.e("TAG", "onCreatecountries: "+jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "onCreateex: "+e.getMessage());
        }


        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,height);
        spinner_height.setAdapter(customSpinnerAdapterr);
        for (int i=0;i<height.size();i++){
            try {
                if (jsonObjectProfile.getJSONObject("data").getString("height_txt").equalsIgnoreCase(height.get(i))){
                    spinner_height.setSelection(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner_height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                height_int= spinner_height.getSelectedItem().toString();
                if (height_int.equalsIgnoreCase("Height")){

                } else {
                    heigh_ = height_int.split(" ");
                    heighttext=heigh_[0];
                    Log.e("TAG", "onItemSelected: "+heighttext);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,haricolor);
        spinner_hair_color.setAdapter(customSpinnerAdapterr);
        for (int i=0;i<haricolor.size();i++){
            try {
                if (jsonObjectProfile.getJSONObject("data").getString("hair_color_txt").equalsIgnoreCase(haricolor.get(i))){
                    spinner_hair_color.setSelection(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        spinner_hair_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                hair_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,gender);
        spinner_gender.setAdapter(customSpinnerAdapterr);
        for (int i=0;i<gender.size();i++){
            try {
                if (jsonObjectProfile.getJSONObject("data").getString("gender_txt").equalsIgnoreCase(gender.get(i))){
                    spinner_gender.setSelection(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==1){
                    gender_int="4525";
                }else if (position==2){
                    gender_int="4526";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,work);
        spinner_work.setAdapter(customSpinnerAdapterr);
        spinner_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                work_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,education);
        spinner_education_level.setAdapter(customSpinnerAdapterr);
        spinner_education_level.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                education_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,language);
        spinner_language.setAdapter(customSpinnerAdapterr);
        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                language_int= language.get(position).toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,ethnicity);
        spinner_ethnicity.setAdapter(customSpinnerAdapterr);
        spinner_ethnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                ethnicity_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,body);
        spinner_body_type.setAdapter(customSpinnerAdapterr);
        spinner_body_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                body_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,character);
        spinner_character.setAdapter(customSpinnerAdapterr);
        spinner_character.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                character_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,children);
        spinner_children.setAdapter(customSpinnerAdapterr);
        spinner_children.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                children_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,friends);
        spinner_friends.setAdapter(customSpinnerAdapterr);
        spinner_friends.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                friends_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,pets);
        spinner_pets.setAdapter(customSpinnerAdapterr);
        spinner_pets.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pets_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,live);
        spinner_live.setAdapter(customSpinnerAdapterr);
        spinner_live.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                live_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,car);
        spinner_car.setAdapter(customSpinnerAdapterr);
        spinner_car.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                car_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,smoke);
        spinner_smoke.setAdapter(customSpinnerAdapterr);
        spinner_smoke.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                smoke_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,drink);
        spinner_drink.setAdapter(customSpinnerAdapterr);
        spinner_drink.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                drink_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,travel);
        spinner_travel.setAdapter(customSpinnerAdapterr);
        spinner_travel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                travel_int= String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        for (int i=0;i<gender.size();i++){
//            try {
//                if (jsonObjectProfile.getJSONObject("data").getString("gender_txt").equalsIgnoreCase(gender.get(i))){
//                    spinner_gender.setSelection(i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        spinner_work.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position==1){
                    gender_int="4525";
                }else if (position==2){
                    gender_int="4526";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,country);
        spinner_country.setAdapter(customSpinnerAdapterr);
        for (int i=0;i<country.size();i++){
            try {
                if (jsonObjectProfile.getJSONObject("data").getString("country_txt").equalsIgnoreCase(country.get(i))){
                    spinner_country.setSelection(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        spinner_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                try {
                    country_int= jsonArray.getJSONObject(position).getString("value");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date;
        try {
            date = originalFormat.parse(jsonObjectProfile.getJSONObject("data").getString("birthday"));
            dob=targetFormat.format(date);
            text_birthday.setText(dob);
            edt_phone_number.setText(jsonObjectProfile.getJSONObject("data").getString("phone_number"));
//            Glide.with(this)
//                    .load(jsonObjectProfile.getJSONObject("data").getString("avater"))
//                    //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                    //.placeholder(R.color.gray_color)
//                    //.error(R.color.gray_color)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(profile_pic);
        } catch (Exception ex) {
            // Handle Exception.
        }




//        apiurl=getResources().getString(R.string.base_url)+getResources().getString(R.string.profileapi);
//        getProfile(apiurl);
    }

    private void setImageViewCount() {
        String[] count = this.getResources().getStringArray(R.array.photo_count);
        CustomTextView tvImageCount1 = rltProfileImageOne.findViewById(R.id.tv_count);
        tvImageCount1.setText(count[0]);
        CustomTextView tvImageCount2 = rltProfileImageTwo.findViewById(R.id.tv_count);
        tvImageCount2.setText(count[1]);
        CustomTextView tvImageCount3 = rltProfileImageThree.findViewById(R.id.tv_count);
        tvImageCount3.setText(count[2]);
        CustomTextView tvImageCount4 = rltProfileImageFour.findViewById(R.id.tv_count);
        tvImageCount4.setText(count[3]);
        CustomTextView tvImageCount5 = rltProfileImageFive.findViewById(R.id.tv_count);
        tvImageCount5.setText(count[4]);
        CustomTextView tvImageCount6 = rltProfileImageSix.findViewById(R.id.tv_count);
        tvImageCount6.setText(count[5]);
    }


    public void uploadimageapi(String filePath) {
        this.imagePath=filePath;
        Log.e(TAG, "uploadimageapi: "+imagePath);
        uploadReceiver.register(EditProfile.this);
        try
        {
            loading.showDialog();
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);
            {
                uploadRequest=new MultipartUploadRequest(EditProfile.this, uploadId,getResources().getString(R.string.base_url)+getResources().getString(R.string.uploadavaterapi));
                uploadRequest.addFileToUpload(imagePath, "avater");
                uploadRequest.addParameter("access_token", sessionManager.getToken());
                uploadRequest.setMaxRetries(2);
                uploadRequest.startUpload();
                Log.e(TAG, "uploadhomeworkapi:2 " );
            }
        } catch (Exception exc)
        {
            loading.hideDialog();
            Log.e(TAG, "uploadhomeworkapi: "+exc.getMessage());
        }
    }

    private void checkAllPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (requiredPermissions.Camera_storage_PermissionIsEnabledOrNot()) {
                pickProfileImg();
            }
            else {
                requiredPermissions.RequestMultiplePermission_Camera_storage_PermissionIsEnabledOrNot();
            }
        } else {
            pickProfileImg();
        }
    }

    private void showEnablePermissionDailog(final int type, String message) {
        if (!customDialog.isVisible()) {
            customDialog = new CustomDialog(message, getString(R.string.okay), new CustomDialog.btnAllowClick() {
                @Override
                public void clicked() {
                    if (type == 0)
                        callPermissionSettings();
                    else
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
                }
            });
            customDialog.show(this.getSupportFragmentManager(), "");
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {
                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_externalpermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_externalpermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (CameraPermission && read_externalpermission && write_externalpermission) {
                        pickProfileImg();
                    }
                    else {
                        checkAllPermission();
                    }
                }
                break;
        }
    }



    public void pickProfileImg() {
        View view = getLayoutInflater().inflate(R.layout.camera_dialog_layout, null);
        LinearLayout lltCamera = (LinearLayout) view.findViewById(R.id.llt_camera);
        LinearLayout lltLibrary = (LinearLayout) view.findViewById(R.id.llt_library);

        final Dialog bottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        if (bottomSheetDialog.getWindow() == null) return;
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.show();

        lltCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFile = commonMethods.cameraFilePath();
                imageUri = FileProvider.getUriForFile(EditProfile.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);

                try {
                    List<ResolveInfo> resolvedIntentActivities = getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;
                        grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    cameraIntent.putExtra("return-data", true);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                cameraIntent.putExtra("type","profile");
                startActivityForResult(cameraIntent, 1);
                commonMethods.refreshGallery(EditProfile.this, imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(EditProfile.this);

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                i.putExtra("type","profile");
                startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 300:
                    checkAllPermission();
                    break;
                case 1:
//                    Log.e(TAG, "onActivityResult: "+data.getStringExtra("type"));
                    startCropImage();
                    break;
                case Constants.REQUEST_CODE_GALLERY:
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
                        copyStream(inputStream, fileOutputStream);
                        fileOutputStream.close();
                        if (inputStream != null) inputStream.close();
                        startCropImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:

                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    try {
                        imagePath = result.getUri().getPath();
                        Log.e(TAG, "onActivityResultimagePath: "+imagePath);

                        if (!TextUtils.isEmpty(imagePath)) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
//                            profile_pic.setImageBitmap(bitmap);

                            if (clickPos==1) {
                                ivUserImageOne.setImageBitmap(bitmap);
                            }
                            else if (clickPos==2){
                                ivUserImageTwo.setImageBitmap(bitmap);
                            }else if (clickPos==3){
                                ivUserImageThree.setImageBitmap(bitmap);
                            }else if (clickPos==4){
                                ivUserImageFour.setImageBitmap(bitmap);
                            }else if (clickPos==5){
                                ivUserImageFive.setImageBitmap(bitmap);
                            }else if (clickPos==6){
                                ivUserImageSix.setImageBitmap(bitmap);
                            }
                            uploadimageapi(imagePath);
                           /* Bitmap mbitmap = BitmapFactory.decodeFile(imagePath);
                            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                            Canvas canvas = new Canvas(imageRounded);
                            Paint mpaint = new Paint();
                            mpaint.setAntiAlias(true);
                            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 30, 30, mpaint);// Round Image Corner 100 100 100 100
                            mimageView.setImageBitmap(imageRounded);
                            ivProfileImage.setImageBitmap(imageRounded);*/
                        }
                    } catch (OutOfMemoryError | Exception e) {
                        e.printStackTrace();
                        Log.e(TAG, "onActivityResult: "+e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void copyStream(InputStream input, FileOutputStream output) throws IOException {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    private void startCropImage() {
        Log.e(TAG, "startCropImage: ");
        if (imageFile == null) return;
        int[] minimumSquareDimen = ImageMinimumSizeCalculator.getMinSquarDimension(Uri.fromFile(imageFile), this);
        CropImage.activity(Uri.fromFile(imageFile))
                .setDefaultlyCropEnabled(true)
                .setAspectRatio(10, 10)
                .setOutputCompressQuality(100)
                .setMinCropResultSize(minimumSquareDimen[0], minimumSquareDimen[1])
                .start(this);
    }


    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.text_birthday){
            datepicker();
        }
//        else if (v.getId()==R.id.profile_pic){
//            checkAllPermission();
//        }
        else if (v.getId()==R.id.back_button){
            onBackPressed();
        }
        else if (v.getId()==R.id.save_button){
            validations();
        }
        else if (v.getId()==R.id.tv_add_icon){
            clickPos = (int) v.getTag();
            Log.e(TAG, "onClick: "+clickPos);
            //pickProfileImg(false);
            isDelete = false;
            checkAllPermission();
        }
    }

    private void checkAllPermission(String[] permission, boolean isDelete) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(this, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(this, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {
                ActivityCompat.requestPermissions(this, permission, 300);
            }
        } else {
            pickProfileImg(isDelete);
            //checkGpsEnable();

        }
    }



    public void pickProfileImg(boolean isDelete) {
        this.isDelete = isDelete;
        View view = getLayoutInflater().inflate(R.layout.camera_dialog_layout, null);
        LinearLayout lltCamera = (LinearLayout) view.findViewById(R.id.llt_camera);
        LinearLayout lltLibrary = (LinearLayout) view.findViewById(R.id.llt_library);

        final Dialog bottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        if (bottomSheetDialog.getWindow() == null) return;
        bottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomSheetDialog.show();

        lltCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                imageFile = commonMethods.cameraFilePath();
                imageUri = FileProvider.getUriForFile(EditProfile.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);


                try {
                    List<ResolveInfo> resolvedIntentActivities = EditProfile.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolvedIntentInfo : resolvedIntentActivities) {
                        String packageName = resolvedIntentInfo.activityInfo.packageName;
                        grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                    cameraIntent.putExtra("return-data", true);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(cameraIntent, 1);
                commonMethods.refreshGallery(EditProfile.this, imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(EditProfile.this);

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, Constants.REQUEST_CODE_GALLERY);
            }
        });
    }

    public void datepicker(){
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(EditProfile.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        text_birthday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
        picker.show();

    }

    private void validations() {
        if (spinner_height.getSelectedItem().toString().equalsIgnoreCase("Height")){
            messageToast.showDialog("Please Select Height");
        }
        else if (spinner_hair_color.getSelectedItem().toString().equalsIgnoreCase("Choose your Hair Color")){
            messageToast.showDialog("Please Select Hair Color");
        }
        else if (spinner_gender.getSelectedItem().toString().equalsIgnoreCase("Choose your Gender")){
            messageToast.showDialog("Please Select Gender");
        }
        else if (spinner_country.getSelectedItem().toString().equalsIgnoreCase("Choose your country")){
            messageToast.showDialog("Please Select Country");
        }
        else if (edt_phone_number.getText().toString().trim().isEmpty()){
            messageToast.showDialog("Please Enter Phone Number");
        }
        else if (edt_phone_number.getText().toString().trim().length()<10){
            messageToast.showDialog("Please Enter Valid Phone Number");
        }
        else if (text_birthday.getText().toString().trim().equalsIgnoreCase("Enter DOB")){
            messageToast.showDialog("Please Enter DOB");
        }
        else {
            new ImageCompressAsyncTask(EditProfile.this, imagePath, this, "").execute();
            SimpleDateFormat originalFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date;
            try {
                date = originalFormat.parse(text_birthday.getText().toString());
                dob=targetFormat.format(date);
            } catch (ParseException ex) {
                // Handle Exception.
            }
            updateuser_api(getResources().getString(R.string.base_url)+getResources().getString(R.string.updateprofileapi));
        }
    }

    private void updateuser_api(String apiurl) {
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    messageToast.showDialog("Profile Updated Successfully");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent=new Intent(EditProfile.this, HomeActivity.class);
                            startActivity(intent);
                        }
                    },2500);

                }
                else
                {
                    loading.hideDialog();

                }
            }
            catch(Exception e) {
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
                Log.e("TAG", "getParams: "+edt_phone_number.getText().toString());
                Log.e("TAG", "getParams: "+dob);
                Log.e("TAG", "getParams: "+hair_int);
                Log.e("TAG", "getParams: "+country_int);
                Log.e("TAG", "getParams: "+gender_int);
                Log.e("TAG", "getParams: "+heighttext);
                Log.e("TAG", "getParams: "+work_int);
                params.put("phone_number", edt_phone_number.getText().toString());
                params.put("birthday", dob);
                params.put("hair_color", hair_int);
                params.put("country", country_int);
                params.put("gender", gender_int);
                params.put("work_status", work_int);
                params.put("education", education_int);
                params.put("language", language_int);
                params.put("ethnicity", ethnicity_int);
                params.put("body", body_int);
                params.put("character", character_int);
                params.put("children", children_int);
                params.put("friends", friends_int);
                params.put("pets", pets_int);
                params.put("live_with", live_int);
                params.put("car", car_int);
                params.put("smoke", smoke_int);
                params.put("drink", drink_int);
                params.put("travel", travel_int);
                params.put("location", edt_location.getText().toString());
                params.put("about", edt_about_me.getText().toString());
                params.put("height", heighttext);
                params.put("access_token", sessionManager.getToken());
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {

    }

    @Override
    public void onError(Exception exception) {

    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        loading.hideDialog();
        messageToast.showDialog("Image Updated Successfully");
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onImageCompress(String filePath, RequestBody requestBody) {
        if (!TextUtils.isEmpty(filePath) && requestBody != null) {
            try {
//                commonMethods.showProgressDialog((AppCompatActivity) getContext(), customDialog);
                Log.e(TAG, "onImageCompressdone: ");
            } catch (Exception e) {
                Log.e("TAG", "onImageCompress: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
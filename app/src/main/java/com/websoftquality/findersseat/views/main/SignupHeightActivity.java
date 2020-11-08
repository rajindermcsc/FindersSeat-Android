package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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

public class SignupHeightActivity extends AppCompatActivity implements View.OnClickListener, SingleUploadBroadcastReceiver.Delegate, ImageListener {
    Spinner spinner_height,spinner_hair_color,spinner_country,spinner_gender;
    EditText edt_phone_number;
    ArrayList<String> height=new ArrayList<>();
    ArrayList<String> haricolor=new ArrayList<>();
    ArrayList<String> country= new ArrayList<>();
    ArrayList<String> gender=new ArrayList<>();
    CustomSpinnerAdapterr customSpinnerAdapterr;
    CircleImageView profile_pic;
    TextView tv_login_phone,text_birthday;
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
    SignupHeightActivity mActivity;
    RequiredPermissions requiredPermissions;
    public static final int RequestPermissionCode = 7;
    private AlertDialog dialog;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_height);
        AppController.getAppComponent().inject(this);
        spinner_height=findViewById(R.id.spinner_height);
        spinner_hair_color=findViewById(R.id.spinner_hair_color);
        spinner_country=findViewById(R.id.spinner_country);
        spinner_gender=findViewById(R.id.spinner_gender);
        tv_login_phone=findViewById(R.id.tv_login_phone);
        text_birthday=findViewById(R.id.text_birthday);
        edt_phone_number=findViewById(R.id.edt_phone_number);
        profile_pic=findViewById(R.id.profile_pic);
        text_birthday.setOnClickListener(this);
        tv_login_phone.setOnClickListener(this);
        profile_pic.setOnClickListener(this);
        loading=new Loading(this);
        requiredPermissions = new RequiredPermissions(this);
        apierror_handle=new Apierror_handle(this);
        messageToast=new MessageToast(this);
        dialog = commonMethods.getAlertDialog(SignupHeightActivity.this);
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



        customSpinnerAdapterr=new CustomSpinnerAdapterr(this,country);
        spinner_country.setAdapter(customSpinnerAdapterr);
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

    }


    public void uploadimageapi(String filePath) {
        this.imagePath=filePath;
        Log.e(TAG, "uploadimageapi: "+imagePath);
        uploadReceiver.register(SignupHeightActivity.this);
        try
        {
            loading.showDialog();
            String uploadId = UUID.randomUUID().toString();
            uploadReceiver.setDelegate(this);
            uploadReceiver.setUploadID(uploadId);
            {
                uploadRequest=new MultipartUploadRequest(SignupHeightActivity.this, uploadId,getResources().getString(R.string.base_url)+getResources().getString(R.string.updateavaterapi));
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
                imageUri = FileProvider.getUriForFile((AppCompatActivity) getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", imageFile);

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
                startActivityForResult(cameraIntent, 1);
                commonMethods.refreshGallery((AppCompatActivity) getApplicationContext(), imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(SignupHeightActivity.this);

                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
                           /* Bitmap mbitmap = BitmapFactory.decodeFile(imagePath);
                            Bitmap imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig());
                            Canvas canvas = new Canvas(imageRounded);
                            Paint mpaint = new Paint();
                            mpaint.setAntiAlias(true);
                            mpaint.setShader(new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                            canvas.drawRoundRect((new RectF(0, 0, mbitmap.getWidth(), mbitmap.getHeight())), 30, 30, mpaint);// Round Image Corner 100 100 100 100
                            //mimageView.setImageBitmap(imageRounded);
                            ivProfileImage.setImageBitmap(imageRounded);*/
                            profile_pic.setImageBitmap(bitmap);
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
        if (v.getId()==R.id.tv_login_phone){
            validations();
        }
        else if ((v.getId()==R.id.text_birthday)){
            datepicker();
        }
        else if (v.getId()==R.id.profile_pic){
            checkAllPermission();
//            checkAllPermission(Constants.PERMISSIONS_PHOTO,false);
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
                imageUri = FileProvider.getUriForFile(SignupHeightActivity.this, BuildConfig.APPLICATION_ID + ".provider", imageFile);


                try {
                    List<ResolveInfo> resolvedIntentActivities = SignupHeightActivity.this.getPackageManager().queryIntentActivities(cameraIntent, PackageManager.MATCH_DEFAULT_ONLY);
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
                commonMethods.refreshGallery(SignupHeightActivity.this, imageFile);
            }
        });

        lltLibrary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                imageFile = commonMethods.getDefaultFileName(SignupHeightActivity.this);

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
        picker = new DatePickerDialog(SignupHeightActivity.this,
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
        }else if (imagePath.isEmpty()){
            messageToast.showDialog("Please Upload Profile Pic");
        }
        else {
            new ImageCompressAsyncTask(SignupHeightActivity.this, imagePath, this, "").execute();
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
                    messageToast.showDialog(jsonObject.getString("data"));
                    Intent intent=new Intent(SignupHeightActivity.this,HomeActivity.class);
//                    sessionManager.setToken(jsonObject.getJSONObject("data").getString("access_token"));
//                    sessionManager.setUserName(jsonObject.getJSONObject("data").getJSONObject("user_info").getString("username"));
//                    sessionManager.setUserId(jsonObject.getJSONObject("data").getInt("user_id"));
                    sessionManager.setMinAge("18");
                    sessionManager.setMaxAge("40");
                    sessionManager.setDistance("100");
                    startActivity(intent);
                }
                else
                {
                    loading.hideDialog();
                    messageToast.showDialog(jsonObject.getString("message"));

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
                Map<String, String> params = new HashMap<>();
                Log.e("TAG", "getParams: "+edt_phone_number.getText().toString());
                Log.e("TAG", "getParams: "+dob);
                Log.e("TAG", "getParams: "+hair_int);
                Log.e("TAG", "getParams: "+country_int);
                Log.e("TAG", "getParams: "+gender_int);
                Log.e("TAG", "getParams: "+heighttext);
                params.put("phone_number", edt_phone_number.getText().toString());
                params.put("birthday", dob);
                params.put("hair_color", hair_int);
                params.put("country", country_int);
                params.put("gender", gender_int);
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
                uploadimageapi(filePath);
            } catch (Exception e) {
                Log.e("TAG", "onImageCompress: "+e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
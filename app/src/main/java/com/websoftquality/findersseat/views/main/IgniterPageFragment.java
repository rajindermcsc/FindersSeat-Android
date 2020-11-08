package com.websoftquality.findersseat.views.main;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.obs.CustomTextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import de.hdodenhof.circleimageview.CircleImageView;
import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.adapters.matches.MatchesSwipeAdapter;
import com.websoftquality.findersseat.configs.AppController;
import com.websoftquality.findersseat.configs.Constants;
import com.websoftquality.findersseat.configs.RunTimePermission;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.datamodels.main.JsonResponse;
import com.websoftquality.findersseat.datamodels.main.ProfileSuccessModel;
import com.websoftquality.findersseat.datamodels.main.UserDetailModel;
import com.websoftquality.findersseat.datamodels.matches.MatchingProfile;
import com.websoftquality.findersseat.iaputils.IabBroadcastReceiver;
import com.websoftquality.findersseat.iaputils.IabHelper;
import com.websoftquality.findersseat.iaputils.IabResult;
import com.websoftquality.findersseat.iaputils.Inventory;
import com.websoftquality.findersseat.iaputils.Purchase;
import com.websoftquality.findersseat.interfaces.ActivityListener;
import com.websoftquality.findersseat.interfaces.ApiService;
import com.websoftquality.findersseat.interfaces.ServiceListener;
import com.websoftquality.findersseat.swipedeck.SwipeDeck;
import com.websoftquality.findersseat.swipedeck.layouts.SwipeRelativeLayout;
import com.websoftquality.findersseat.utils.Apierror_handle;
import com.websoftquality.findersseat.utils.CommonMethods;
import com.websoftquality.findersseat.utils.ImageUtils;
import com.websoftquality.findersseat.utils.Loading;
import com.websoftquality.findersseat.utils.MessageToast;
import com.websoftquality.findersseat.utils.MyLocation;
import com.websoftquality.findersseat.utils.RequestCallback;
import com.websoftquality.findersseat.views.chat.ChatConversationActivity;
import com.websoftquality.findersseat.views.chat.MatchUsersActivity;
import com.websoftquality.findersseat.views.customize.CustomDialog;
import com.websoftquality.findersseat.views.customize.RippleBackground;

import static com.websoftquality.findersseat.utils.Enums.MATCH_LIKE;
import static com.websoftquality.findersseat.utils.Enums.MATCH_REWIND;
import static com.websoftquality.findersseat.utils.Enums.MATCH_SUPER_LIKE;
import static com.websoftquality.findersseat.utils.Enums.REQ_GET_HOME;
import static com.websoftquality.findersseat.utils.Enums.REQ_SHOW_ALL_MATCHES;
import static com.websoftquality.findersseat.utils.Enums.REQ_SWIPE_MATCH;
import static com.websoftquality.findersseat.utils.Enums.REQ_UPDATE_BOOST_USER;
import static com.websoftquality.findersseat.utils.Enums.REQ_UPDATE_LOCATION;

public class IgniterPageFragment extends Fragment implements View.OnClickListener, ServiceListener, IabBroadcastReceiver.IabBroadcastListener {

    static final String TAG = "Boost In App Purchase";

    static String SKU_INFINITE_ONE_BOOST = "";
    static String SKU_INFINITE_FIVE_BOOST = "";
    static String SKU_INFINITE_TEN_BOOST = "";
    static String SKU_INFINITE_5_SL = "";
    static String SKU_INFINITE_25_SL = "";
    static String SKU_INFINITE_60_SL = "";
    static String SKU_INFINITE_1_IP = "";
    static String SKU_INFINITE_6_IP = "";
    static String SKU_INFINITE_12_IP = "";
    CountDownTimer countDownTimer = null;
    @Inject
    ApiService apiService;
    @Inject
    CommonMethods commonMethods;
    @Inject
    CustomDialog customDialog;
    @Inject
    SessionManager sessionManager;
    @Inject
    Gson gson;
    @Inject
    ImageUtils imageUtils;
    @Inject
    RunTimePermission runTimePermission;
    int totalSuperLikeCount = 0, remainingSuperLikeCount = 0, remainingLikeCount = 0, totalLikeCount = 0, subscriptionId = 0;
    String isLikeLimited;
    String remainingSuperLikeHours;
    int totalBoostCount = 0, remainingBoostCount = 0;
    String remainingBoostHours, remainingBoostDay;
    boolean rewind = false;
    // The helper object
    IabHelper mHelper;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    String payload = "";
    // Does the user have an active subscription to the infinite Boost plan?
    boolean mSubscribedToInfiniteBoost = false, mSubscribedToInfiniteSuperLike = false;
    // Does the user have an active subscription to the infinite Igniter Plus plan?
    boolean mSubscribedToPlus = false;
    // Will the subscription auto-renew?
    boolean mAutoRenewEnabled = false;
    // Tracks the currently owned infinite Boost SKU, and the options in the Manage dialog
    String mInfiniteBoostSku = "", mInfiniteSuperLikeSku = "", mInfinitePlusSku = "";
    Purchase oneBoost, fiveBoost, tenBoost;
    Purchase SL5, SL25, SL60;
    Purchase IP1, IP6, IP12;
    Inventory inventory;
    // Listener that's called when we finish querying the items and subscriptions we own
    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventorys) {
            inventory = inventorys;
            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain(getString(R.string.failed_query_inv)+": " + result);
                return;
            }

            /*
             * Check for items we own. Notice that for each purchase, we check
             * the developer payload to see if it's correct! See
             * verifyDeveloperPayload().
             */

            // First find out which subscription is auto renewing
            oneBoost = inventory.getPurchase(SKU_INFINITE_ONE_BOOST);
            fiveBoost = inventory.getPurchase(SKU_INFINITE_FIVE_BOOST);
            tenBoost = inventory.getPurchase(SKU_INFINITE_TEN_BOOST);

            if (oneBoost != null && oneBoost.isAutoRenewing()) {
                mInfiniteBoostSku = SKU_INFINITE_ONE_BOOST;
                mAutoRenewEnabled = true;
            } else if (fiveBoost != null && fiveBoost.isAutoRenewing()) {
                mInfiniteBoostSku = SKU_INFINITE_FIVE_BOOST;
                mAutoRenewEnabled = true;
            } else if (tenBoost != null && tenBoost.isAutoRenewing()) {
                mInfiniteBoostSku = SKU_INFINITE_TEN_BOOST;
                mAutoRenewEnabled = true;
            } else {
                mInfiniteBoostSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteBoost = (oneBoost != null && verifyDeveloperPayload(oneBoost))
                    || (fiveBoost != null && verifyDeveloperPayload(fiveBoost)
                    || (tenBoost != null && verifyDeveloperPayload(tenBoost)));
            Log.e(TAG, "Original JSON 123 " + mSubscribedToInfiniteBoost);
            Log.e(TAG, "User Boost" + (mSubscribedToInfiniteBoost ? "HAS" : "DOES NOT HAVE")
                    + " infinite Boost subscription.");


            SL5 = inventory.getPurchase(SKU_INFINITE_5_SL);
            SL25 = inventory.getPurchase(SKU_INFINITE_25_SL);
            SL60 = inventory.getPurchase(SKU_INFINITE_60_SL);


            if (SL5 != null && SL5.isAutoRenewing()) {
                mInfiniteSuperLikeSku = SKU_INFINITE_5_SL;
                mAutoRenewEnabled = true;
            } else if (SL25 != null && SL25.isAutoRenewing()) {
                mInfiniteSuperLikeSku = SKU_INFINITE_25_SL;
                mAutoRenewEnabled = true;
            } else if (SL60 != null && SL60.isAutoRenewing()) {
                mInfiniteSuperLikeSku = SKU_INFINITE_60_SL;
                mAutoRenewEnabled = true;
            } else {
                mInfiniteSuperLikeSku = "";
                mAutoRenewEnabled = false;
            }


            IP1 = inventory.getPurchase(SKU_INFINITE_1_IP);
            IP6 = inventory.getPurchase(SKU_INFINITE_6_IP);
            IP12 = inventory.getPurchase(SKU_INFINITE_12_IP);

            if (IP1 != null && IP1.isAutoRenewing()) {
                mInfinitePlusSku = SKU_INFINITE_1_IP;
                mAutoRenewEnabled = true;
            } else if (IP6 != null && IP6.isAutoRenewing()) {
                mInfinitePlusSku = SKU_INFINITE_6_IP;
                mAutoRenewEnabled = true;
            } else if (IP12 != null && IP12.isAutoRenewing()) {
                mInfinitePlusSku = SKU_INFINITE_12_IP;
                mAutoRenewEnabled = true;
            } else {
                mInfinitePlusSku = "";
                mAutoRenewEnabled = false;
            }

            // The user is subscribed if either subscription exists, even if neither is auto
            // renewing
            mSubscribedToInfiniteSuperLike = (SL5 != null && verifyDeveloperPayload(SL5))
                    || (SL25 != null && verifyDeveloperPayload(SL25)
                    || (SL60 != null && verifyDeveloperPayload(SL60)));

            mSubscribedToPlus = (IP1 != null && verifyDeveloperPayload(IP1))
                    || (IP6 != null && verifyDeveloperPayload(IP6)
                    || (IP12 != null && verifyDeveloperPayload(IP12)));
        }
    };
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.e(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.e(TAG, "Consumption successful. Provisioning.");
                mSubscribedToInfiniteBoost = false;
                mSubscribedToInfiniteSuperLike = false;
                mSubscribedToPlus = false;

                //alert("You filled 1/4 tank. Your tank is now " + String.valueOf(mTank) + "/4 full!");
            } else {
                complain(mActivity.getString(R.string.er_consume)+": " + result);
            }

            Log.e(TAG, "End consumption flow.");
        }
    };
    private View view;
    private ActivityListener listener;
    private Resources res;
    private HomeActivity mActivity;
    private ImageView  ivLike,  ivUnLike;
    private RelativeLayout rltLike, rltUnLike;
    private CustomTextView tvClose, tvMatch;
    private CircleImageView civProfileImg;
    private RippleBackground rippleBackground;
    private UserDetailModel userDetailModel;
    private AlertDialog dialog;
    private ArrayList<MatchingProfile> matchingProfilesList = new ArrayList<>();
    private ProfileSuccessModel matchProfilesModel;
    private MatchesSwipeAdapter matchesSwipeAdapter;
    private SwipeDeck cardStack;
    private SwipeRelativeLayout swipeLayout;
    private double latitude = 0, longitude = 0;
    /**
     * Get user current location
     */
//    MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
//        @Override
//        public void gotLocation(Location location) {
//            System.out.println("Check4");
//            if (location == null) return;
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
//            System.out.println("Check5");
//            apiService.insertLocation(sessionManager.getToken(), latitude, longitude, "update").enqueue(new RequestCallback(REQ_UPDATE_LOCATION, IgniterPageFragment.this));
//        }
//    };
    private boolean sawLike = true, sawUnLike = true, sawSuperLike = true;
    private String swipeMethod;
    private boolean isPermissionGranted = false;
    JSONObject jsonObject;
    Loading loading;
    MessageToast messageToast;
    String apiurl,apilike;
    Apierror_handle apierror_handle;

    public static void hideKeyboard(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;

        inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            listener = (ActivityListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException("Profile must implement ActivityListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
        AppController.getAppComponent().inject(this);

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        } else {
            view = inflater.inflate(R.layout.igniter_fragment, container, false);
        }


        return view;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState) {

        setupHelper();
        loading=new Loading(getContext());
        apierror_handle=new Apierror_handle(getContext());
        messageToast=new MessageToast(getContext());
        cardStack = view.findViewById(R.id.swipe_deck);
        swipeLayout = view.findViewById(R.id.swipeLayout);
        ivLike = view.findViewById(R.id.iv_like);
        ivUnLike = view.findViewById(R.id.iv_unlike);

        rltLike = view.findViewById(R.id.rlt_like_lay);
        rltUnLike = view.findViewById(R.id.rlt_unlike_lay);

        dialog = commonMethods.getAlertDialog(mActivity);
        civProfileImg = view.findViewById(R.id.civ_profile_image);
        tvMatch = view.findViewById(R.id.tv_match);


        rltUnLike.setEnabled(false);
        rltLike.setEnabled(false);
        ivUnLike.setEnabled(false);
        ivLike.setEnabled(false);
        rltLike.setOnClickListener(this);
        rltUnLike.setOnClickListener(this);

        cardStack.setClickable(true);
        cardStack.setVisibility(View.INVISIBLE);
        cardStack.setLeftImage(R.id.like_tv);
        cardStack.setRightImage(R.id.nope_tv);

        cardStack.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void cardSwipedLeft(long itemId) {
                try {
                    String uid=jsonObject.getJSONArray("data").getJSONObject(Math.toIntExact(itemId)).getString("id");
                    apilike(getResources().getString(R.string.base_url)+getResources().getString(R.string.addlikesapi),uid,"dislikes");
                    Log.e(TAG, "cardSwipedRight: "+uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void cardSwipedRight(long itemId) {
                try {
                    String uid=jsonObject.getJSONArray("data").getJSONObject(Math.toIntExact(itemId)).getString("id");
                    apilike(getResources().getString(R.string.base_url)+getResources().getString(R.string.addlikesapi),uid,"likes");
                    Log.e(TAG, "cardSwipedRight: "+uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void cardSwipedUp(long itemId) {
                try {
                    String uid=jsonObject.getJSONArray("data").getJSONObject(Math.toIntExact(itemId)).getString("id");
                    apilike(getResources().getString(R.string.base_url)+getResources().getString(R.string.addlikesapi),uid,"likes");
                    Log.e(TAG, "cardSwipedRight: "+uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void cardSwipedDown(long itemId) {
                try {
                    String uid=jsonObject.getJSONArray("data").getJSONObject(Math.toIntExact(itemId)).getString("id");
                    apilike(getResources().getString(R.string.base_url)+getResources().getString(R.string.addlikesapi),uid,"dislikes");
                    Log.e(TAG, "cardSwipedRight: "+uid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public boolean isDragEnabled(long itemId) {
                return true;
            }
        });

        rippleBackground = view.findViewById(R.id.rb_background);
        rippleBackground.startRippleAnimation();

        getHomePage(getResources().getString(R.string.base_url)+getResources().getString(R.string.randomusersapi));
//        getHomePage();
//        updateDeviceId();
    }

    private void apilike(String apilike, String uid, String likes) {
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apilike);
        StringRequest strReq = new StringRequest(Request.Method.POST, apilike, response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    if (likes.equalsIgnoreCase("likes")){
                        messageToast.showDialog("Likes Successfully");
                    }
                    else {
                        messageToast.showDialog("Rejected");
                    }
                }
                else
                {
                    loading.hideDialog();
                    jsonObject.getString("message");

                }
            }
            catch(Exception e)
            {
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
                Log.e(TAG, "getParams: "+sessionManager.getToken());
                Log.e(TAG, "getParams: "+uid);
                params.put("access_token", sessionManager.getToken());
                if (likes.equalsIgnoreCase("likes")) {
                    params.put("likes", uid);
                    params.put("dislikes", "");
                    Log.e(TAG, "getParamslike: "+uid);
                }
                else {
                    Log.e(TAG, "getParamsdis: "+uid);
                    params.put("likes", "");
                    params.put("dislikes", uid);
                }
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void apiaddfriend(String url, String uid) {
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apilike);
        StringRequest strReq = new StringRequest(Request.Method.POST, url, response -> {
            try{
                loading.hideDialog();
                final JSONObject jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {
                    jsonObject.getString("message");
                }
                else
                {
                    loading.hideDialog();
                    jsonObject.getString("message");

                }
            }
            catch(Exception e)
            {
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
                params.put("access_token", sessionManager.getToken());
                params.put("uid", uid);

                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    /**
     * Enable disable rewind view
     */
    public void rewind(boolean rewindc, int type) {

        // type 0 for rewind click action
        // type 1 for rewind disable action
        if (type == 0) {
            cardStack.unSwipeCard();
//            swipeProfile(getUserId((int) cardStack.getTopCardItemId()), MATCH_REWIND, "");
        } else if (type == 2) {
            cardStack.unSwipeCard();
            reloadSwipe();
        }
    }

    /**
     * Declare variable for layout views
     */
    private void init() {
        if (listener == null) return;

        res = (listener.getRes() != null) ? listener.getRes() : getActivity().getResources();
        mActivity = (listener.getInstance() != null) ? listener.getInstance() : (HomeActivity) getActivity();
    }

    /**
     * Get swipe user name using position
     */
    private String getUserName(int pos) {

        String Name = "";
        if (pos < matchingProfilesList.size() && matchingProfilesList.get(pos) != null) {
            Name = matchingProfilesList.get(pos).getName();
        }
        if (pos == matchingProfilesList.size() - 1) {
            noPeople();
        }
        return Name;
    }


    private void getHomePage(String apiurl) {
        Log.e(TAG, "getHomePage: "+apiurl);
        loading.showDialog();
        String tag_string_req = "req_login";
        Log.e("TAG", "attendance_webservice: "+apiurl);
        StringRequest strReq = new StringRequest(Request.Method.POST, apiurl, response -> {
            try{
                loading.hideDialog();
                jsonObject = new JSONObject(response);
                Log.e("TAG", "onResponse: "+jsonObject);
                if (jsonObject.getString("code").equals("200"))
                {

                    onSuccessShowAllMatches(jsonObject);
//
////                        Intent intent=new Intent(LoginEmailActivity.this,HomeActivity.class);
//                    sessionManager.setToken(jsonObject.getJSONObject("data").getString("access_token"));
//                    sessionManager.setUserName(jsonObject.getString("user_name"));
//                    sessionManager.setUserId(jsonObject.getJSONObject("data").getInt("user_id"));
////                        startActivity(intent);
                }
                else
                {
                    loading.hideDialog();

                }
            }
            catch(Exception e)
            {
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
                params.put("access_token", sessionManager.getToken());
                params.put("limit", "20");
                return params;
            }
        };
        strReq.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    /**
     * Default on click function
     */
    @Override
    public void onClick(View v) {
        Intent intent = null;
        String msg = "", btnText = "", title = "";

        switch (v.getId()) {


            case R.id.rlt_unlike_lay:
                if (sessionManager.getIsSawUnLike()) {
                    String name = getUserName((int) cardStack.getTopCardItemId());
                    msg = String.format(res.getString(R.string.alert_not_interest_msg), name);
                    title = res.getString(R.string.alert_not_interest_title);
                    btnText = res.getString(R.string.not_interest);
                    showDialog(0, "", "", "click", title, msg, btnText, 0);
                    sessionManager.setIsSawUnLike(false);
                    sawUnLike = false;
                } else {
                    sawUnLike = false;
                    cardStack.swipeTopCardLeft(SwipeDeck.ANIMATION_DURATION);
                }
                break;

            case R.id.rlt_like_lay:
                if (!"yes".equalsIgnoreCase(sessionManager.getIsRemainingLikeLimited())) {
                    likeSwipeCall();
                } else if (sessionManager.getRemainingLikes() > 0) {
                    sessionManager.setRemainingLikes(sessionManager.getRemainingLikes() - 1);
                    likeSwipeCall();
                } else {
                    Log.e(TAG, "Original JSON " + mSubscribedToInfiniteSuperLike);
                    Log.e(TAG, "Original JSON " + mSubscribedToInfiniteSuperLike);
                    // if (!mSubscribedToPlus) {
                    //rewind(false, 2);
                    intent = new Intent(mActivity, IgniterPlusDialogActivity.class);
                    intent.putExtra("startwith", "");
                    intent.putExtra("type", "plus");
                    startActivity(intent);
                    return;
                    // }
                }
                break;
            default:
                break;
        }
    }

    public void likeSwipeCall() {
        Intent intent = null;
        String msg = "", btnText = "", title = "";
        if (sessionManager.getIsSawLike()) {
            String name = getUserName((int) cardStack.getTopCardItemId());
            msg = String.format(res.getString(R.string.alert_like_msg), name);
            title = res.getString(R.string.alert_like_title);
            btnText = res.getString(R.string.like);
            showDialog(0, "", "", "click", title, msg, btnText, 1);
            sessionManager.setIsSawLike(false);
            sawLike = false;
        } else {
            sawLike = false;
            cardStack.swipeTopCardRight(SwipeDeck.ANIMATION_DURATION);
        }
    }

    /**
     * Show dialog while first swipe or click the bottom images
     */
    private void showDialog(final Integer userId, final String matchType, final String json, final String type, String title, String msg, String buttonText, final int index) {
        customDialog = new CustomDialog(index, title, msg, buttonText, res.getString(R.string.cancel), new CustomDialog.btnAllowClick() {
            @Override
            public void clicked() {
                switch (index) {
                    case 0:
                        if (!type.equals("swipe"))
                            cardStack.swipeTopCardLeft(SwipeDeck.ANIMATION_DURATION);
                        //cardStack.unSwipeCard();
                        break;
                    case 1:
                        if (!type.equals("swipe"))
                            cardStack.swipeTopCardRight(SwipeDeck.ANIMATION_DURATION);
                        //cardStack.unSwipeCard();
                        break;
                    case 2:
                        if (!type.equals("swipe"))
                            cardStack.swipeTopCardTop(SwipeDeck.ANIMATION_DURATION);
                        //cardStack.unSwipeCard();
                        break;
                    default:
                        break;
                }

                if (type.equals("swipe")) {
                    if (matchType.equals(MATCH_SUPER_LIKE)) {
//                        superLike(userId, -100, json);
                    } else if (matchType.equals(MATCH_LIKE)) {
//                        like(userId, -100, json);
                    } else {
//                        swipeProfile(userId, matchType, json);
                    }
                }
            }
        }, new CustomDialog.btnDenyClick() {
            @Override
            public void clicked() {
                switch (index) {
                    case 0:
                        if (type.equals("swipe"))
                            cardStack.unSwipeCard();
                        break;
                    case 1:
                        if (type.equals("swipe"))
                            cardStack.unSwipeCard();
                        break;
                    case 2:
                        if (type.equals("swipe"))
                            cardStack.unSwipeCard();
                        break;
                    default:
                        break;
                }
            }
        });
        customDialog.show(mActivity.getSupportFragmentManager(), "");
    }

    /**
     * Call swipe profile API
     */
//    private void swipeProfile(Integer userId, String matchType, String json) {
//        swipeMethod = matchType;
//        if(userId!=0)
//            apiService.swipeProfile(sessionManager.getToken(), userId, matchType, subscriptionId).enqueue(new RequestCallback(REQ_SWIPE_MATCH, this));
//    }

    /**
     * Call boost user API
     */
//    private void boostUser() {
//        apiService.boostUser(sessionManager.getToken()).enqueue(new RequestCallback(REQ_UPDATE_BOOST_USER, this));
//    }

    /**
     * Get result for other activity
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) return;
        if (requestCode == 100 && data != null) {
            boolean isKeepSwipe = data.getBooleanExtra("isKeepSwipe", true);
            //if (!isKeepSwipe) mActivity.getViewPager().setCurrentItem(2);
            if (!isKeepSwipe) {
                Intent intent = new Intent(mActivity, ChatConversationActivity.class);
                intent.putExtra("matchId", data.getIntExtra("matchId", 0));
                intent.putExtra("userId", data.getIntExtra("userId", 0));
                startActivity(intent);
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == 300) {
            checkAllPermission(Constants.PERMISSIONS_LOCATION);
        } else if (requestCode == 101) {
            checkGpsEnable();
        }
    }

    /**
     * Call API for get other user profiles
     */
    private void showMatchProfile() {
        if (!rippleBackground.isRippleAnimationRunning()) {
            rippleBackground.setVisibility(View.VISIBLE);

            rippleBackground.startRippleAnimation();
            tvMatch.setText(res.getString(R.string.find_people));
            try{
                ((HomeActivity) getActivity()).changeDirection("all");
            }catch (Exception e){

            }

        }
        if (latitude != 0 && longitude != 0) {
            tvMatch.setText(res.getString(R.string.find_people));

        }
    }

    /**
     * After API call get success response
     */
    @Override
    public void onSuccess(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        Log.e(TAG, "onSuccess: "+jsonResp.getStrResponse());
        Log.e(TAG, "onSuccess: "+data);
        if (!jsonResp.isOnline()) {
//            commonMethods.showMessage(mActivity, dialog, data);
            return;
        }

        switch (jsonResp.getRequestCode()) {
            case REQ_GET_HOME:
                if (jsonResp.isSuccess()) {
                    Log.e(TAG, "onSuccess: "+data);
                    Log.e(TAG, "onSuccess: "+jsonResp.getStrResponse());
                    try {
                        jsonObject=new JSONObject(jsonResp.getStrResponse());
                        Log.e(TAG, "onSuccess: "+jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    onSuccessGetMyHome(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
//                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_SWIPE_MATCH:
                if (jsonResp.isSuccess()) {
                    onSuccessSwipeProfile(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (swipeMethod.equalsIgnoreCase(MATCH_LIKE)) {
                        totalLikeCount = (Integer) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.REMAINING_LIKE, Integer.class);
                        sessionManager.setRemainingLikes(totalLikeCount);
                        cardStack.unSwipeCard();

                        Intent intent = new Intent(mActivity, IgniterPlusDialogActivity.class);
                        intent.putExtra("startwith", "");
                        intent.putExtra("type", "plus");
                        startActivity(intent);
                    } else {
                        commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                    }
                }
                break;
            case REQ_UPDATE_BOOST_USER:
                if (jsonResp.isSuccess()) {
                    onSuccessBoostUser(jsonResp);
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    if (jsonResp.getStatusMsg().equals(getString(R.string.no_boost_count))) {
                        if (oneBoost != null && verifyDeveloperPayload(oneBoost))
                            consumed(SKU_INFINITE_ONE_BOOST, inventory);
                        else if (fiveBoost != null && verifyDeveloperPayload(fiveBoost))
                            consumed(SKU_INFINITE_FIVE_BOOST, inventory);
                        else if (tenBoost != null && verifyDeveloperPayload(tenBoost))
                            consumed(SKU_INFINITE_TEN_BOOST, inventory);

                        Intent intent = new Intent(mActivity, IgniterPlusDialogActivity.class);
                        intent.putExtra("startwith", "");
                        intent.putExtra("type", "boost");
                        startActivity(intent);
                    } else {
                        commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                    }

                }
                break;
            case REQ_UPDATE_LOCATION:
                showMatchProfile();
                if (jsonResp.isSuccess()) {
                    onSuccessUpdateLocation(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                }
                break;
            case REQ_SHOW_ALL_MATCHES:
                if (jsonResp.isSuccess()) {
                    matchProfilesModel = gson.fromJson(jsonResp.getStrResponse(), ProfileSuccessModel.class);
                    Log.e(TAG, "onSuccess: "+matchProfilesModel.getData().toString());
//                    if (!TextUtils.isEmpty(matchProfilesModel.getIsOrder()) && matchProfilesModel.getIsOrder().equalsIgnoreCase("Yes")) {
//                        sessionManager.setIsOrder(true);
//                        sessionManager.setPlanType(matchProfilesModel.getPlanType());
//                    } else {
//                        sessionManager.setIsOrder(false);
//                        sessionManager.setPlanType(matchProfilesModel.getPlanType());
//                    }
//
//                    totalSuperLikeCount = matchProfilesModel.getTotalSuperLikes();
//                    remainingSuperLikeCount = matchProfilesModel.getRemainingSuperLikes();
//                    remainingLikeCount = matchProfilesModel.getRemainingLike();
//                    isLikeLimited = matchProfilesModel.getIsLikeLimited();
//                    remainingSuperLikeHours = matchProfilesModel.getRemainingSuperLikeHours();
//
//                    sessionManager.setRemainingSuperLikes(remainingSuperLikeCount);
//                    sessionManager.setRemainingLikes(remainingLikeCount);
//                    sessionManager.setIsRemainingLikeLimited(isLikeLimited);
//
//                    totalBoostCount = matchProfilesModel.getTotalBoost();
//                    remainingBoostCount = matchProfilesModel.getRemainingBoost();
//                    remainingBoostHours = matchProfilesModel.getRemainingBoostHours();
//                    remainingBoostDay = matchProfilesModel.getRemainingBoostDay();
//                    sessionManager.setRemainingBoost(remainingBoostCount);
//                    Log.e(TAG, "Match Profile " + String.valueOf(sessionManager.getRemainingBoost()));
//
//                    try{
//                        if (matchProfilesModel.getUnReadCount() != null && matchProfilesModel.getUnReadCount() > 0) {
//                            ((HomeActivity) getActivity()).changeChatIcon(1);
//                        } else {
//                            ((HomeActivity) getActivity()).changeChatIcon(0);
//                        }
//                    }catch (Exception e){
//
//                    }

//                    onSuccessShowAllMatches(jsonResp);
                } else if (!TextUtils.isEmpty(jsonResp.getStatusMsg())) {
                    commonMethods.showMessage(mActivity, dialog, jsonResp.getStatusMsg());
                    noPeople();
                    rewind(false, 1);
                }
                break;
            default:
                break;
        }
    }


    private void onSuccessShowAllMatches(JSONObject jsonObject) {
        this.jsonObject=jsonObject;
//        Log.e("Check ", String.valueOf(matchingProfilesList.size()));
//        MatchesResponse matchesResponse = gson.fromJson(jsonResp.getStrResponse(), MatchesResponse.class);
//        if (matchesResponse != null && matchesResponse.getMatchingProfile() != null && !matchesResponse.getMatchingProfile().isEmpty()) {
//            matchingProfilesList.clear();
        cardStack.clear();
        Log.e("Check1 ", String.valueOf(jsonObject));
//            matchingProfilesList.addAll(matchesResponse.getMatchingProfile());
        try {

            if (jsonObject.getJSONArray("data").length() > 0) {
                try {
                    ((HomeActivity) (getActivity())).changeDirection("none");
                } catch (Exception e) {
                }

                Log.e("Check2 ", String.valueOf(jsonObject));
                rippleBackground.stopRippleAnimation();
                tvMatch.setText(res.getString(R.string.no_one));

                rippleBackground.setVisibility(View.GONE);
                cardStack.setVisibility(View.VISIBLE);

                rltUnLike.setEnabled(true);
                rltLike.setEnabled(true);

//                if (!sessionManager.getIsOrder())
//                    rltReload.setEnabled(true);
//                else
//                    rltReload.setEnabled(false);

                ivUnLike.setEnabled(true);
                ivLike.setEnabled(true);
//                if (!sessionManager.getIsOrder())
//                    ivReload.setEnabled(true);
//                else
//                    ivReload.setEnabled(false);

                updateMatchingProfiles(jsonObject);
            }
            else {
                noPeople();
                rewind(false, 1);
            }
        }
        catch (Exception e){
            Log.e(TAG, "onSuccessShowAllMatches: "+e.getMessage());
        }

        }

//        else {
//            noPeople();
//            rewind(false, 1);
//        }
//    }

    /**
     * After API call get failure response
     */
    @Override
    public void onFailure(JsonResponse jsonResp, String data) {
        commonMethods.hideProgressDialog();
        if (!jsonResp.isOnline()) commonMethods.showMessage(mActivity, dialog, data);
        noPeople();
    }

    /**
     * After API call get swipe profile response
     */
    private void onSuccessSwipeProfile(JsonResponse jsonResp) {
        if (jsonResp.isSuccess()) {
            String json = jsonResp.getStrResponse();

            remainingSuperLikeCount = (Integer) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.REMAINING_SUPER_LIKE, Integer.class);
            remainingLikeCount = (Integer) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.REMAINING_LIKE, Integer.class);
            isLikeLimited = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.ISLIKELIMITED, String.class);
            if (swipeMethod.equalsIgnoreCase(MATCH_REWIND)) {
                sessionManager.setRemainingSuperLikes(remainingSuperLikeCount);
                sessionManager.setRemainingLikes(remainingLikeCount);
                sessionManager.setIsRemainingLikeLimited(isLikeLimited);
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(jsonResp.getStrResponse());
                if (jsonObject.has(Constants.SUBSCRIPTION_ID)) {
                    String subscriptionIds = commonMethods.getJsonValueString(jsonResp.getStrResponse(), Constants.SUBSCRIPTION_ID, String.class);
                    if (!TextUtils.isEmpty(subscriptionIds))
                        subscriptionId = Integer.parseInt(subscriptionIds);
                    else
                        subscriptionId = 0;
                } else {
                    subscriptionId = 0;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            if (swipeMethod.equalsIgnoreCase(MATCH_REWIND)) {
                reloadSwipe();
            }

            String matchStatus = (String) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.MATCH_STATUS, String.class);
            System.out.println("JSON " + json);
            System.out.println("JSONs " + jsonResp.getRequestData());
            System.out.println("Match Status " + matchStatus);

            if (!TextUtils.isEmpty(json) && !TextUtils.isEmpty(matchStatus) && matchStatus.equalsIgnoreCase("Yes")) {
                Intent intent = new Intent(mActivity, MatchUsersActivity.class);
                intent.putExtra("json", json);
                startActivityForResult(intent, 100);
            }
        }
    }

    /**
     * After API call get boost response
     */
    private void onSuccessBoostUser(JsonResponse jsonResp) {
        if (jsonResp.isSuccess()) {
            String json = jsonResp.getRequestData();

            remainingBoostCount = (Integer) commonMethods.getJsonValue(jsonResp.getStrResponse(), Constants.REMAINING_BOOST, Integer.class);
            if (remainingBoostCount == 0)
                mSubscribedToInfiniteBoost = true;

            sessionManager.setRemainingBoost(remainingBoostCount);
            Log.e(TAG, "After Boost Applied " + sessionManager.getRemainingBoost());

        }
    }

    /**
     * After swipe profile reset bottom icons
     */
    public void reloadSwipe() {
        try{

            if (cardStack != null && cardStack.getTopCardItemId() >= 0) {
                ((HomeActivity) (getActivity())).changeDirection("none");
            } else {
                ((HomeActivity) (getActivity())).changeDirection("none");
            }
        }catch (Exception e){

        }
        rippleBackground.setVisibility(View.GONE);
        cardStack.setVisibility(View.VISIBLE);

        rltUnLike.setEnabled(true);
        rltLike.setEnabled(true);
        // rltReload.setEnabled(true);

        ivUnLike.setEnabled(true);
        ivLike.setEnabled(true);
    }

    /**
     * After API call get location update status
     */
    private void onSuccessUpdateLocation(JsonResponse jsonResp) {
//        IgniterPageModel igniterPageModel = gson.fromJson(jsonResp.getStrResponse(), IgniterPageModel.class);
//        if (igniterPageModel != null && igniterPageModel.getUserDetails() != null) {
//            userDetailModel = igniterPageModel.getUserDetails();
//            updateView();
//        }
    }

    /**
     * After API call get user details
     */
    private void onSuccessGetMyHome(JsonResponse jsonResp) {
        /*IgniterPageModel igniterPageModel = gson.fromJson(jsonResp.getStrResponse(), IgniterPageModel.class);
        if (igniterPageModel != null && igniterPageModel.getUserDetails() != null) {
            userDetailModel = igniterPageModel.getUserDetails();
            updateView();
        }*/
    }

    /**
     * Set disable and hide view if no profile available
     */
    public void noPeople() {
        //rippleBackground.stopRippleAnimation();
        rippleBackground.startRippleAnimation();
        tvMatch.setText(res.getString(R.string.no_one));
        try{

            ((HomeActivity) (getActivity())).changeDirection("all");
        }catch (Exception e){

        }

        rippleBackground.setVisibility(View.VISIBLE);
        cardStack.setVisibility(View.GONE);

        rltUnLike.setEnabled(false);
        rltLike.setEnabled(false);
        ivUnLike.setEnabled(false);
        ivLike.setEnabled(false);
        //ivBoost.setEnabled(false);
        //ivReload.setEnabled(false);

        if (cardStack != null && cardStack.getTopCardItemId() < 0) {
            // showMatchProfile();
        }
    }

    /**
     * After API call to update view
     */
    private void updateView() {
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(userDetailModel.getFirstName())) {
            sb.append(userDetailModel.getFirstName());
            sessionManager.setUserName(userDetailModel.getFirstName());
        }

        if (!TextUtils.isEmpty(userDetailModel.getLastName())) {
            sb.append(" ");
            sb.append(userDetailModel.getLastName());
        }

        sb.append(", ");
        sb.append(userDetailModel.getAge());

    }

    /**
     * Check user allow all permission and ask permission to allow
     */
    private void checkAllPermission(String[] permission) {
        ArrayList<String> blockedPermission = runTimePermission.checkHasPermission(mActivity, permission);
        if (blockedPermission != null && !blockedPermission.isEmpty()) {
            boolean isBlocked = runTimePermission.isPermissionBlocked(mActivity, blockedPermission.toArray(new String[blockedPermission.size()]));
            if (isBlocked) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    public void run() {
                        //callPermissionSettings();
                        //showEnablePermissionDailog(0, getString(R.string.please_enable_permissions));
                    }
                });
            } else {

                ActivityCompat.requestPermissions(mActivity, permission, 150);
            }
        } else {
            checkGpsEnable();
        }
    }

    /**
     * Get permission result
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println("Never Ask again : ");
        ArrayList<String> permission = runTimePermission.onRequestPermissionsResult(permissions, grantResults);
        if (permission != null && !permission.isEmpty()) {
            runTimePermission.setFirstTimePermission(true);
            String[] dsf = new String[permission.size()];
            permission.toArray(dsf);
            checkAllPermission(dsf);
        } else {
            checkGpsEnable();
        }
    }

    /**
     * If any one or more permission is deny or block show the enable permission dialog
     */
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
            customDialog.show(mActivity.getSupportFragmentManager(), "");
        }
    }

    /**
     * Open permission dialog
     */
    private void callPermissionSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mActivity.getApplicationContext().getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 300);
    }

    /**
     * Check GPS enable or not
     */
    private void checkGpsEnable() {
        boolean isGpsEnabled = MyLocation.defaultHandler().isLocationAvailable(mActivity);
        if (!isGpsEnabled) {
            //startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 101);
            showEnablePermissionDailog(1, getString(R.string.please_enable_location));
        } else {
            isPermissionGranted = true;
            System.out.println("Check1");
//            MyLocation.defaultHandler().getLocation(mActivity, locationResult);
        }
    }

    /**
     * show other user profile in swipedeck
     * @param jsonObject
     */
    private void updateMatchingProfiles(JSONObject jsonObject) {
        this.jsonObject=jsonObject;

        if(getContext()!=null){
            Log.e(TAG, "updateMatchingProfiles: "+ this.jsonObject);
            matchesSwipeAdapter = new MatchesSwipeAdapter(this.jsonObject, getContext());
            if (cardStack != null) {
//                Log.e("Check ", String.valueOf(matchingProfilesList.size()));
                mActivity.runOnUiThread(new Runnable() {
                    public void run() {
                        cardStack.setAdapter(matchesSwipeAdapter);
                        matchesSwipeAdapter.notifyDataSetChanged();
                    }
                });

                //cardStack.unSwipeCard();
            }
        }

    }

    /**
     * Function call the view is visible to user that time we reload the other user is swipe deck
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (cardStack != null)
            //if (isVisibleToUser && view != null&&cardStack!=null&&cardStack.getTopCardItemId()<0&&sessionManager.getSettingUpdate()) {
            if (isVisibleToUser && view != null && cardStack != null && cardStack.getTopCardItemId() < 0) {
                Log.e(TAG, "setUserVisibleHint: "+cardStack.getTopCardItemId());
//                noPeople();
//                showMatchProfile();
            } else {
//                noPeople();
//                showMatchProfile();

            }

        if (sessionManager!=null&&!TextUtils.isEmpty(sessionManager.getProfileImg())) {
            Glide.with(mActivity)
                    .load(sessionManager.getProfileImg())
                    //.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    //.placeholder(R.color.gray_color)
                    //.error(R.color.gray_color)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(civProfileImg);
            // imageUtils.loadCircleImage(mActivity, civProfileImg, sessionManager.getProfileImg());
        }


        if (cardStack != null && cardStack.getTopCardItemId() >= 0) {
            try{
                ((HomeActivity) getActivity()).changeDirection("none");
            }catch (Exception e){

            }

        } else {
//           ((HomeActivity)getActivity()).changeDirection("all");
        }

        if (isVisibleToUser && view != null) {
            hideKeyboard(getContext());
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        hideKeyboard(getContext());

        if(sessionManager.isPurchased()){
            sessionManager.setPurchased(false);
        }




        if (cardStack != null && cardStack.getTopCardItemId() < 0 && !isPermissionGranted)
            checkAllPermission(Constants.PERMISSIONS_LOCATION);

        if (sessionManager.getSwipeType() != null && cardStack != null) {
            if (sessionManager.getSwipeType().equals("UnLike"))
                cardStack.swipeTopCardLeft(SwipeDeck.ANIMATION_DURATION);
            else if (sessionManager.getSwipeType().equals("Like"))
                cardStack.swipeTopCardRight(SwipeDeck.ANIMATION_DURATION);

            sessionManager.setSwipeType("");
        }


        if (sessionManager.getRemainingBoost() == 0)
            mSubscribedToInfiniteBoost = true;
    }

    /**
     * Setup In App Purchase
     */
    void setupHelper() {
        /* base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY
         * (that you got from the Google Play developer console). This is not your
         * developer public key, it's the *app-specific* public key.
         *
         * Instead of just storing the entire literal string here embedded in the
         * program,  construct the key at runtime from pieces or
         * use bit manipulation (for example, XOR with some other string) to hide
         * the actual key.  The key itself is not secret information, but we don't
         * want to make it easy for an attacker to replace the public key with one
         * of their own and then fake messages from the server.
         */
        String base64EncodedPublicKey = getResources().getString(R.string.google_play_publish_key);

        // Some sanity checks to see if the developer (that's you!) really followed the
        // instructions to run this sample (don't put these checks on your app!)
        if (base64EncodedPublicKey.contains("CONSTRUCT_YOUR")) {
            throw new RuntimeException("Please put your app's public key in MainActivity.java. See README.");
        }


        // Create the helper, passing it our context and the public key to verify signatures with
        Log.e(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(getContext(), base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.e(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.e(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain(getString(R.string.pbm_inapp_set)+": " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(IgniterPageFragment.this);
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                mActivity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.e(TAG, "Setup successful. Querying inventory.");
                try {
                    mHelper.queryInventoryAsync(mGotInventoryListener);
                } catch (IabHelper.IabAsyncInProgressException e) {
                    complain(getString(R.string.er_query_inventry));
                }
            }
        });
    }

    /**
     * Receive data while purchase or call In App products
     */
    @Override
    public void receivedBroadcast() {
        // Received a broadcast notification that the inventory of items has changed
        Log.d("Boost Dialog", "Received broadcast notification. Querying inventory.");
        try {
            mHelper.queryInventoryAsync(mGotInventoryListener);
        } catch (IabHelper.IabAsyncInProgressException e) {
            complain(getString(R.string.er_query_inventry));

        }
    }

    /**
     * Verifies the developer payload of a purchase.
     */
    boolean verifyDeveloperPayload(Purchase p) {
        payload = p.getDeveloperPayload();
        /*Log.e(TAG, "Original JSON " +  p.getDeveloperPayload());
        Log.e(TAG, "Original JSON " +  p.getOriginalJson());
        Log.e(TAG, "Original JSON " +  p.getItemType());
        Log.e(TAG, "Original JSON " +  p.getPurchaseState());
        Log.e(TAG, "Original JSON " +  p.getPurchaseTime());
        Log.e(TAG, "Original JSON " +  p.getToken());
        Log.e(TAG, "Original JSON " +  p.getSku());
        Log.e(TAG, "Original JSON " +  p.getSignature());*/

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    /**
     * Show dialog while In App Purchase messages
     */
    void complain(String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        //alert("Error: " + message);
        alert("" + message);
    }

    void alert(String message) {
        android.app.AlertDialog.Builder bld = new android.app.AlertDialog.Builder(getContext());
        bld.setMessage(message);
        bld.setNeutralButton(getString(R.string.ok), null);
        Log.e(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    // We're being destroyed. It's important to dispose of the helper here!
    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        Log.d(TAG, "Destroying helper.");
        if (mHelper != null) {
            mHelper.disposeWhenFinished();
            mHelper = null;
        }

        // very important:
        if (mBroadcastReceiver != null) {
            mActivity.unregisterReceiver(mBroadcastReceiver);
        }
    }

    /**
     * After user used the plan call consumed function
     */
    void consumed(String plan, Inventory inventory) {
        // Check for Boost delivery -- if we own Boost, we should fill up the tank immediately
        Purchase BoostPurchase = inventory.getPurchase(plan);
        if (BoostPurchase != null && verifyDeveloperPayload(BoostPurchase)) {
            Log.e(TAG, "We have Boost. Consuming it.");
            try {
                mHelper.consumeAsync(BoostPurchase, mConsumeFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                complain(getString(R.string.er_consume_boost));
            }
            return;
        }
        Log.e(TAG, "Initial inventory query finished; enabling main UI.");

    }
}
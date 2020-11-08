package com.websoftquality.findersseat.dependencies.component;
/**
 * @package com.trioangle.igniter
 * @subpackage dependencies.component
 * @category AppComponent
 * @author Trioangle Product Team
 * @version 1.0
 **/

import javax.inject.Singleton;

import dagger.Component;

import com.ChatActivity;
import com.websoftquality.ChatDemoFragment;
import com.websoftquality.NotificationsFragment;
import com.websoftquality.ProfileDemoFragment;
import com.websoftquality.findersseat.adapters.chat.ChatConversationListAdapter;
import com.websoftquality.findersseat.adapters.chat.MessageUserListAdapter;
import com.websoftquality.findersseat.adapters.chat.NewMatchesListAdapter;
import com.websoftquality.findersseat.adapters.chat.UnmatchReasonListAdapter;
import com.websoftquality.findersseat.adapters.chat.UserListAdapter;
import com.websoftquality.findersseat.adapters.main.ProfileSliderAdapter;
import com.websoftquality.findersseat.adapters.matches.MatchesSwipeAdapter;
import com.websoftquality.findersseat.adapters.profile.EditProfileImageListAdapter;
import com.websoftquality.findersseat.adapters.profile.EnlargeSliderAdapter;
import com.websoftquality.findersseat.adapters.profile.IgniterSliderAdapter;
import com.websoftquality.findersseat.adapters.profile.LocationListAdapter;
import com.websoftquality.findersseat.backgroundtask.ImageCompressAsyncTask;
import com.websoftquality.findersseat.configs.RunTimePermission;
import com.websoftquality.findersseat.configs.SessionManager;
import com.websoftquality.findersseat.dependencies.module.AppContainerModule;
import com.websoftquality.findersseat.dependencies.module.ApplicationModule;
import com.websoftquality.findersseat.dependencies.module.NetworkModule;
import com.websoftquality.findersseat.layoutmanager.SwipeableTouchHelperCallback;
import com.websoftquality.findersseat.likedusers.LikedUserAdapter;
import com.websoftquality.findersseat.likedusers.LikedUsersActivity;
import com.websoftquality.findersseat.pushnotification.MyFirebaseInstanceIDService;
import com.websoftquality.findersseat.pushnotification.MyFirebaseMessagingService;
import com.websoftquality.findersseat.pushnotification.NotificationUtils;
import com.websoftquality.findersseat.swipedeck.Utility.SwipeListener;
import com.websoftquality.findersseat.utils.CommonMethods;
import com.websoftquality.findersseat.utils.DateTimeUtility;
import com.websoftquality.findersseat.utils.ImageUtils;
import com.websoftquality.findersseat.utils.RequestCallback;
import com.websoftquality.findersseat.utils.WebServiceUtils;
import com.websoftquality.findersseat.views.chat.ChatConversationActivity;
import com.websoftquality.findersseat.views.chat.ChatFragment;
import com.websoftquality.findersseat.views.chat.CreateGroupActivity;
import com.websoftquality.findersseat.views.chat.LikesUserActivity;
import com.websoftquality.findersseat.views.chat.MatchUsersActivity;
import com.websoftquality.findersseat.views.main.BoostDialogActivity;
import com.websoftquality.findersseat.views.main.HomeActivity;
import com.websoftquality.findersseat.views.main.IgniterGoldActivity;
import com.websoftquality.findersseat.views.main.IgniterPageFragment;
import com.websoftquality.findersseat.views.main.IgniterPlusDialogActivity;
import com.websoftquality.findersseat.views.main.IgniterPlusSliderFragment;
import com.websoftquality.findersseat.views.main.LoginActivity;
import com.websoftquality.findersseat.views.main.LoginEmailActivity;
import com.websoftquality.findersseat.views.main.OtherProfileView;
import com.websoftquality.findersseat.views.main.SelectPaymentActivity;
import com.websoftquality.findersseat.views.main.SettingActivity;
import com.websoftquality.findersseat.views.main.SignupEmailActivity;
import com.websoftquality.findersseat.views.main.SignupHeightActivity;
import com.websoftquality.findersseat.views.main.SplashActivity;
import com.websoftquality.findersseat.views.main.TutorialFragment;
import com.websoftquality.findersseat.views.main.UserNameActivity;
import com.websoftquality.findersseat.views.main.VerificationActivity;
import com.websoftquality.findersseat.views.main.AccountKit.FacebookAccountKitActivity;
import com.websoftquality.findersseat.views.main.AccountKit.TwilioAccountKitActivity;
import com.websoftquality.findersseat.views.profile.AddLocationActivity;
import com.websoftquality.findersseat.views.profile.EditProfile;
import com.websoftquality.findersseat.views.profile.EditProfileActivity;
import com.websoftquality.findersseat.views.profile.EnlargeProfileActivity;
import com.websoftquality.findersseat.views.profile.GetIgniterPlusActivity;
import com.websoftquality.findersseat.views.profile.ProfileFragment;
import com.websoftquality.findersseat.views.profile.SettingsActivity;
import com.websoftquality.findersseat.views.signup.BirthdayFragment;
import com.websoftquality.findersseat.views.signup.EmailFragment;
import com.websoftquality.findersseat.views.signup.GenderFragment;
import com.websoftquality.findersseat.views.signup.OneTimePwdFragment;
import com.websoftquality.findersseat.views.signup.PasswordFragment;
import com.websoftquality.findersseat.views.signup.PhoneNumberFragment;
import com.websoftquality.findersseat.views.signup.ProfilePickFragment;
import com.websoftquality.findersseat.views.signup.SignUpActivity;

/*****************************************************************
 App Component
 ****************************************************************/
@Singleton
@Component(modules = {NetworkModule.class, ApplicationModule.class, AppContainerModule.class})
public interface AppComponent {
    // ACTIVITY

    void inject(SplashActivity splashActivity);

    void inject(HomeActivity homeActivity);

    void inject(SettingsActivity settingsActivity);

    void inject(EditProfileActivity editProfileActivity);

    void inject(GetIgniterPlusActivity getIgniterPlusActivity);

    void inject(SignUpActivity signUpActivity);

    void inject(EnlargeProfileActivity enlargeProfileActivity);

    void inject(MatchUsersActivity matchUsersActivity);

    void inject(ChatConversationActivity chatConversationActivity);

    void inject(CreateGroupActivity createGroupActivity);

    void inject(LoginActivity loginActivity);

    void inject(VerificationActivity verificationActivity);

    void inject(UserNameActivity userNameActivity);

    void inject(AddLocationActivity addLocationActivity);

    void inject(FacebookAccountKitActivity facebookAccountKitActivity);

    void inject(TwilioAccountKitActivity facebookAccountKitActivity1);

    void inject(IgniterGoldActivity igniterGoldActivity);

    void inject(LikedUsersActivity likedUsersActivity);


    // Fragments
    void inject(ProfileFragment profileFragment);

    void inject(IgniterPageFragment igniterPageFragment);

    void inject(ChatFragment chatFragment);

    void inject(ProfilePickFragment profilePickFragment);

    void inject(EmailFragment emailFragment);

    void inject(PasswordFragment passwordFragment);

    void inject(BirthdayFragment birthdayFragment);

    void inject(TutorialFragment tutorialFragment);

    void inject(PhoneNumberFragment phoneNumberFragment);

    void inject(OneTimePwdFragment oneTimePwdFragment);

    void inject(GenderFragment genderFragment);

    void inject(IgniterPlusDialogActivity igniterPlusDialogActivity);

    void inject(BoostDialogActivity boostDialogActivity);

    void inject(IgniterPlusSliderFragment igniterPlusSliderFragment);

    // Utilities
    void inject(RunTimePermission runTimePermission);

    void inject(SessionManager sessionManager);

    void inject(ImageUtils imageUtils);

    void inject(CommonMethods commonMethods);

    void inject(ProfileSliderAdapter profileSliderAdapter);

    void inject(RequestCallback requestCallback);

    void inject(DateTimeUtility dateTimeUtility);

    void inject(WebServiceUtils webServiceUtils);

    // Adapters
    void inject(IgniterSliderAdapter igniterSliderAdapter);

    void inject(NewMatchesListAdapter newMatchesListAdapter);

    void inject(MessageUserListAdapter messageUserListAdapter);

    void inject(EnlargeSliderAdapter enlargeSliderAdapter);

    void inject(EditProfileImageListAdapter editProfileImageListAdapter);

    void inject(ChatConversationListAdapter chatConversationListAdapter);

    void inject(UnmatchReasonListAdapter unmatchReasonListAdapter);

    void inject(UserListAdapter chatUserListAdapter);

    void inject(MatchesSwipeAdapter matchesSwipeAdapter);

    void inject(SwipeListener swipeListener);

    void inject(LocationListAdapter locationListAdapter);

    void inject(LikedUserAdapter likedUserAdapter);

    void inject(MyFirebaseMessagingService myFirebaseMessagingService);

    void inject(MyFirebaseInstanceIDService myFirebaseInstanceIDService);


    // AsyncTask
    void inject(ImageCompressAsyncTask imageCompressAsyncTask);

    void inject(NotificationUtils notificationUtils);

    void inject(SwipeableTouchHelperCallback swipeableTouchHelperCallback);


    void inject(LoginEmailActivity loginEmailActivity);

    void inject(SignupEmailActivity signupEmailActivity);

    void inject(SignupHeightActivity signupHeightActivity);

    void inject(ProfileDemoFragment profileDemoFragment);

    void inject(EditProfile editProfile);

    void inject(SettingActivity settingActivity);

    void inject(ChatDemoFragment chatDemoFragment);

    void inject(LikesUserActivity likesUserActivity);

    void inject(ChatActivity chatActivity);

    void inject(NotificationsFragment notificationsFragment);

    void inject(OtherProfileView otherProfileView);

    void inject(SelectPaymentActivity selectPaymentActivity);
}

package com.websoftquality.findersseat.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.websoftquality.findersseat.R;


public class MessageToast {
    Dialog dialog;
    Context context;
    Animation slideup,slidedown;
    TextView textView;
    WindowManager.LayoutParams layoutParams;
    public MessageToast(Context context)
    {
        this.context = context;
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.message_toast);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        layoutParams=new  WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width= WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height= WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        textView= dialog. findViewById(R.id.textmessage);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setWindowAnimations(R.style.animationName);
        slideup = AnimationUtils.loadAnimation(context, R.anim.slide_uptoast);
        slidedown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
    }

    public void showDialog(String message)
    {
        textView.setText(message);
        //dialog.getWindow().setWindowAnimations(R.style.animationName);
        dialog.show();
        Log.e("click",message);

        hideDialog();
    }

    public void hideDialog()
    {
        if(dialog!=null)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            },2500);
        }
//        dialog.dismiss();
    }
}


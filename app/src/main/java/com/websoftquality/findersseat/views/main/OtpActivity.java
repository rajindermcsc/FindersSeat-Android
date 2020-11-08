package com.websoftquality.findersseat.views.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.websoftquality.findersseat.R;
import com.websoftquality.findersseat.utils.MessageToast;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tv_proceed;
    Intent intent;
    EditText edt_otp1,edt_otp2,edt_otp3,edt_otp4;
    String otp,email;
    MessageToast messageToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        messageToast=new MessageToast(this);
        tv_proceed=findViewById(R.id.tv_proceed);
        edt_otp1=findViewById(R.id.edt_otp1);
        edt_otp2=findViewById(R.id.edt_otp2);
        edt_otp3=findViewById(R.id.edt_otp3);
        edt_otp4=findViewById(R.id.edt_otp4);
        tv_proceed.setOnClickListener(this);
        edt_otp1.addTextChangedListener(new GenericTextWatcher(edt_otp1));
        edt_otp2.addTextChangedListener(new GenericTextWatcher(edt_otp2));
        edt_otp3.addTextChangedListener(new GenericTextWatcher(edt_otp3));
        edt_otp4.addTextChangedListener(new GenericTextWatcher(edt_otp4));
        intent=getIntent();
        otp=intent.getStringExtra("otp");
        email=intent.getStringExtra("email");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_proceed:
                validations();

                break;
        }
    }

    private void validations() {
        if ((edt_otp1.getText().toString()+edt_otp2.getText().toString()+edt_otp3.getText().toString()+edt_otp4.getText().toString()).equalsIgnoreCase(otp)){
            Intent intent=new Intent(OtpActivity.this,ResetPasswordActivity.class);
            intent.putExtra("otp",otp);
            intent.putExtra("email",email);
            startActivity(intent);
        }
        else {
            messageToast.showDialog("Invalid OTP");
        }
    }

    public class GenericTextWatcher implements TextWatcher
    {
        private View view;
        private GenericTextWatcher(View view)
        {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch(view.getId())
            {

                case R.id.edt_otp1:
                    if(text.length()==1)
                        edt_otp2.requestFocus();
                    break;
                case R.id.edt_otp2:
                    if(text.length()==1)
                        edt_otp3.requestFocus();
                    else if(text.length()==0)
                        edt_otp1.requestFocus();
                    break;
                case R.id.edt_otp3:
                    if(text.length()==1)
                        edt_otp4.requestFocus();
                    else if(text.length()==0)
                        edt_otp2.requestFocus();
                    break;
                case R.id.edt_otp4:
                    if(text.length()==0)
                        edt_otp3.requestFocus();
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

}
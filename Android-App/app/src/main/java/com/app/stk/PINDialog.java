package com.app.stk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import static com.app.stk.FPVerify.cryptoObject;
import static com.app.stk.FPVerify.fpcansig;
import static com.app.stk.FPVerify.fpm;
import static com.app.stk.FPVerify.fpvtxt;

public class PINDialog extends Dialog  implements View.OnClickListener {
    private Context cx;
    private DBManager dbman;
    private AppCompatButton canbtttn;
    private TextInputEditText pined;
    private AppCompatButton okbttn;
    private Activity nxtActivity;
    private Intent intent;
    public PINDialog(Context cx, Intent i, Activity nxtActivity){
        super(cx);
        this.cx=cx;
        this.nxtActivity=nxtActivity;
        this.intent=i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pin_dialog);
        dbman=new DBManager(getContext());
        pined=findViewById(R.id.PINED);
        canbtttn=findViewById(R.id.canbttn);
        okbttn=findViewById(R.id.okbttn);
        canbtttn.setOnClickListener(this);
        okbttn.setOnClickListener(this);
        this.getWindow().getAttributes().width=700;
        pined.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (pined.getText().toString().length()==4){
                    okbttn.setEnabled(true);
                }else okbttn.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.okbttn:
                if(Integer.parseInt(pined.getText().toString())==dbman.getPIN()){
                    nxtActivity.startActivity(intent);
                    Toast.makeText(cx, "Verified", Toast.LENGTH_SHORT).show();
                }else Toast.makeText(cx, "PIN is Incorrect", Toast.LENGTH_SHORT).show();
                break;
            case R.id.canbttn:
                Toast.makeText(cx, "Canceling", Toast.LENGTH_SHORT).show();
                fpm=FingerprintManagerCompat.from(cx);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        fpm.authenticate(cryptoObject,0,fpcansig,new FingerprintManagerCompat.AuthenticationCallback(){
                            @Override
                            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                                super.onAuthenticationError(errMsgId, errString);
                                fpvtxt.setTextColor(nxtActivity.getResources().getColor(android.R.color.holo_red_dark));
                                fpvtxt.setText("Fingerprint Authentication Error");
                                //Toast.makeText(getApplicationContext(), "Authentication Cancelled", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);
                                Toast.makeText(getContext(), "Authentication Suceeded", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(getContext(),MainActivity.class);
                                nxtActivity.startActivity(i);
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                fpvtxt.setTextColor(nxtActivity.getResources().getColor(android.R.color.holo_red_dark));
                                fpvtxt.setText("Fingerprint not Authorized");
                                //Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();

                            }
                        },null);
                    }
                }).start();
                dismiss();
        }
    }
}

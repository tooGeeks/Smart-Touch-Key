package com.app.stk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class FPVerify extends AppCompatActivity implements View.OnClickListener {
    public static CancellationSignal fpcansig;
    public static AppCompatTextView fpvtxt;
    public static FingerprintManagerCompat.CryptoObject cryptoObject;
    public static FingerprintManagerCompat fpm;
    private SharedPreferences settings;
    private AppCompatButton pinbttn;
    private PINDialog pd;
    private Activity cav;
    final String PREFS_NAME = "MyPrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings.getBoolean("my_first_time", true)) {
            Intent i=new Intent(getApplicationContext(),FirstTimeActivity.class);
            i.putExtra("isBack",false);
            startActivity(i);
            finish();
            //setContentView(R.layout.activity_first_time);
        }
        cav=this;
        setContentView(R.layout.activity_fpverify);
        pd=new PINDialog(this,new Intent(getApplicationContext(),MainActivity.class),cav);
        fpvtxt=findViewById(R.id.fpvtxt);
        pinbttn=findViewById(R.id.pbttn);
        fpcansig=new CancellationSignal();
        fpm= FingerprintManagerCompat.from(this);
        pinbttn.setOnClickListener(this);
        fpm.authenticate(cryptoObject,0,fpcansig,
                new FingerprintManagerCompat.AuthenticationCallback(){
                    @Override
                    public void onAuthenticationError(int errMsgId, CharSequence errString) {
                        super.onAuthenticationError(errMsgId, errString);
                        fpvtxt.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        fpvtxt.setText("Fingerprint Authentication Error");
                        //Toast.makeText(getApplicationContext(), "Authentication Cancelled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(), "Authentication Suceeded", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        fpvtxt.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                        fpvtxt.setText("Fingerprint not Authorized");
                        //Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();

                    }
                },null);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Exiting", Toast.LENGTH_SHORT).show();
        System.exit(1);
    }

    @Override
    public void onClick(View v) {
        fpcansig.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                fpvtxt.setVisibility(View.GONE);
            }
        });
        fpcansig.cancel();
        fpcansig.setOnCancelListener(null);
        pd.show();
    }
}

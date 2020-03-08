package com.app.stk;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.net.Socket;
import java.security.KeyStore;
import javax.crypto.KeyGenerator;


import static com.app.stk.commTh.fpcansig;
import static com.app.stk.commTh.newWeb;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    public static Toolbar tb;
    public static AppCompatTextView mainor;
    public static LinearLayoutCompat mainlinear;
    private AppCompatButton enterbutton;
    public static AppCompatButton connbttn;
    public static AppCompatImageButton scnbttn;
    public static TextInputEditText editip;
    public static TextInputLayout editipholder;
    public static AppCompatTextView conntv;
    public static Thread Sthread;
    public static Thread commThread;
    public static String IP;
    public static DBManager dbman;
    public static Socket s;
    public static FingerprintManagerCompat.CryptoObject cryptoObject;
    public static Context maincx;
    public static Activity mact;
    private static final String TAG = MainActivity.class.getSimpleName();
    static final String KEY_NAME = "default_key";
    private static KeyStore keyStore;
    private static KeyGenerator keyGenerator;
    public static FPDialog fpd;
    public static AlertDialog addentrydialog;
    private FingerprintManagerCompat fpm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        //Scene sc=Scene.getSceneForLayout(null,R.layout.activity_fill_creds,this);
        //TransitionManager.go(sc,new Slide(Slide.MODE_IN));
        setContentView(R.layout.activity_main);
        tb=findViewById(R.id.mtbar);
        setSupportActionBar(tb);
        mact=MainActivity.this;
        s=new Socket();
        maincx=getApplicationContext();
        dbman=new DBManager(this);
        mainor=findViewById(R.id.mainor);
        mainlinear=findViewById(R.id.mainlinear);
        connbttn = findViewById(R.id.connectb);
        scnbttn=findViewById(R.id.scnbttn);
        enterbutton = findViewById(R.id.enterbutton);
        editip = findViewById(R.id.ipedit);
        editipholder=findViewById(R.id.editipholder);
        conntv=findViewById(R.id.conntv);
        scnbttn.setOnClickListener(this);
        enterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ManageCreds.class);
                startActivity(i);
                //setContentView(R.layout.activity_manage_creds);

            }
        });
        connbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //IP="192.168.43.146";
                IP=editip.getText().toString();
                Sthread = new Thread(new SThread(getApplicationContext()));
                Sthread.start();
            }
        });
        AlertDialog.Builder addentrydialogbuilder=new AlertDialog.Builder(this);
        addentrydialogbuilder.setTitle("Add This Entry");
        addentrydialogbuilder.setMessage("No entry found. Do you want to add this entry to the Database?");
        addentrydialogbuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        addentrydialogbuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i=new Intent(getApplicationContext(),FillCreds.class);
                i.putExtra("isUpdate",false);
                i.putExtra("isWebKnown",true);
                i.putExtra("webs",newWeb);
                startActivity(i);
                //setContentView(R.layout.activity_fill_creds);
            }
        });
        addentrydialog=addentrydialogbuilder.create();
        addentrydialog.setInverseBackgroundForced(true);
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null && result.getFormatName().equalsIgnoreCase("QR_CODE")) {
                //llSearch.setVisibility(View.VISIBLE);
                editip.setText(result.getContents());
                IP = result.getContents();
                Sthread = new Thread(new SThread(getApplicationContext()));
                Sthread.start();
                scnbttn.setVisibility(View.GONE);
                connbttn.setVisibility(View.GONE);
                editipholder.setVisibility(View.GONE);
                mainor.setVisibility(View.GONE);
                conntv.setVisibility(View.VISIBLE);



            } else {
                //llSearch.setVisibility(View.GONE);
                if (result==null){
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                }else Toast.makeText(this, "Please scan a QR Code", Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator=new IntentIntegrator(this);
        integrator.setPrompt("Scan the QR Code generated on your PC");
        integrator.setTimeout(10000);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

}

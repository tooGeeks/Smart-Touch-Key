package com.app.stk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class FillCreds extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText awname;
    private TextInputEditText awurl;
    private TextInputEditText auname;
    private TextInputEditText apwd;
    private AppCompatButton addentrybttn;
    private AppCompatButton canbttn;
    private DBManager dbman;
    private boolean isUpdate;
    private boolean isWebKnown;
    private String oldwurl;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_fill_creds);
        Bundle b=getIntent().getExtras();
        isUpdate=b.getBoolean("isUpdate");
        isWebKnown=b.getBoolean("isWebKnown");
        setContentView(R.layout.activity_fill_creds);
        dbman = new DBManager(this);
        awname=findViewById(R.id.addwname);
        awurl=findViewById(R.id.addwurl);
        auname=findViewById(R.id.adduname);
        apwd=findViewById(R.id.addpwd);
        addentrybttn=findViewById(R.id.addentrybttn);
        canbttn=findViewById(R.id.addcanbttn);
        addentrybttn.setOnClickListener(this);
        canbttn.setOnClickListener(this);
        if (isUpdate){
            oldwurl=b.getString("wurl");
            pos=b.getInt("pos");
            DataObject d=dbman.getObject(pos);
            awname.setText(d.getWname());
            awurl.setText(d.getWurl());
            auname.setText(Crypto.decrypt(d.getUname(),DBManager.CRYPTO_PASSWORD));
            apwd.setText(Crypto.decrypt(d.getPwd(),DBManager.CRYPTO_PASSWORD));
            addentrybttn.setText("Update Entry");
        }
        if (isWebKnown){
            awurl.setText(b.getString("webs"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addentrybttn:
                boolean b;
                if (!isUpdate){
                    b=dbman.insertEntry(awname.getText().toString(),awurl.getText().toString(),auname.getText().toString(),apwd.getText().toString());
                    if (b){
                        Toast.makeText(this,"Entry Added",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),ManageCreds.class);
                        setContentView(R.layout.activity_manage_creds);
                        startActivity(i);
                    }else Toast.makeText(this,"Entry Not Added",Toast.LENGTH_SHORT).show();
                }else {
                    b=dbman.updateEntry(pos,oldwurl,awname.getText().toString(),awurl.getText().toString(),auname.getText().toString(),apwd.getText().toString());
                    if (b){
                        Toast.makeText(this,"Entry Updated",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(),ManageCreds.class);
                        setContentView(R.layout.activity_manage_creds);
                        startActivity(i);
                    }else Toast.makeText(this,"Entry Not Updated",Toast.LENGTH_SHORT).show();
                }
            case R.id.addcanbttn:
                    Intent i=new Intent(getApplicationContext(),ManageCreds.class);
                    startActivity(i);
                    setContentView(R.layout.activity_fill_creds);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(getApplicationContext(),ManageCreds.class);
        startActivity(i);
        setContentView(R.layout.activity_fill_creds);
    }
}

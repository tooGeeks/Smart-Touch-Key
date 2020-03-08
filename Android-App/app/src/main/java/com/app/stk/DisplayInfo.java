package com.app.stk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

public class DisplayInfo extends AppCompatActivity {
    private AppCompatTextView displaywname;
    private AppCompatTextView displaywURL;
    private AppCompatTextView displayUname;
    private AppCompatTextView displayPwd;
    private DBManager dbman;
    private SwitchCompat uSwitch;
    private SwitchCompat pSwitch;
    static String duname;
    static String dpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_display_info);
        dbman=new DBManager(this);
        displaywname=findViewById(R.id.displaywname);
        displaywURL=findViewById(R.id.displaywurl);
        displayUname=findViewById(R.id.displayuname);
        displayPwd=findViewById(R.id.displaypwd);
        uSwitch=findViewById(R.id.uSwitch);
        pSwitch=findViewById(R.id.pSwitch);
        Bundle b=getIntent().getExtras();
        String sitename=b.getString("sitename");
        int pos=b.getInt("pos")+1;
        //DataObject dataObject=dbman.getObject(sitename);
        final DataObject dataObject=dbman.getObject(pos);
        duname=Crypto.decrypt(dataObject.getUname(),DBManager.CRYPTO_PASSWORD);
        dpwd=Crypto.decrypt(dataObject.getPwd(),DBManager.CRYPTO_PASSWORD);
        displaywname.setText(dataObject.getWname());
        displaywURL.setText(dataObject.getWurl());
        displayUname.setText("******");
        displayPwd.setText("******");
        uSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    displayUname.setText(duname);
                }else {
                    displayUname.setText("");
                    displayUname.setText("******");
                }
            }
        });
        pSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    displayPwd.setText(dpwd);
                }else {
                    displayPwd.setText("******");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(getApplicationContext(),ManageCreds.class);
        startActivity(i);
        setContentView(R.layout.activity_manage_creds);
    }
}

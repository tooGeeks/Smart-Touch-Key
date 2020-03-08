package com.app.stk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class FirstTimeActivity2 extends AppCompatActivity implements View.OnClickListener {
    private SharedPreferences settings;
    final String PREFS_NAME = "MyPrefsFile";
    private TextInputEditText mno;
    private TextInputEditText email;
    private TextInputEditText add;
    private AppCompatButton nxtbttn;
    private AppCompatButton prevbttn;
    private AppCompatTextView errtxt;
    private Bundle b;
    private int flag;
    private DBManager dbman;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_first_time2);
        b=getIntent().getExtras();
        dbman=new DBManager(this);
        flag=0;
        mno=findViewById(R.id.addmno);
        email=findViewById(R.id.addemail);
        add=findViewById(R.id.addadd);
        nxtbttn=findViewById(R.id.nxtbttn);
        prevbttn=findViewById(R.id.prevbttn);
        errtxt=findViewById(R.id.errtxt);
        nxtbttn.setOnClickListener(this);
        prevbttn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nxtbttn:
                if (email.getText().toString().equalsIgnoreCase("")){
                    email.setTextColor(getResources().getColor(R.color.textred));
                    errtxt.setVisibility(View.VISIBLE);
                    flag=1;
                } else flag=0;
                if (add.getText().toString().equalsIgnoreCase("")){
                    add.setTextColor(getResources().getColor(R.color.textred));
                    errtxt.setVisibility(View.VISIBLE);
                    flag=1;
                }else flag=0;
                if(mno.getText().toString().equalsIgnoreCase("")){
                    mno.setTextColor(getResources().getColor(R.color.textred));
                    errtxt.setVisibility(View.VISIBLE);
                    flag=1;
                }else if(mno.getText().toString().length()!=10){
                    mno.setTextColor(getResources().getColor(R.color.textred));
                    errtxt.setText("Mobile Number should be of 10 digits only");
                    errtxt.setVisibility(View.VISIBLE);
                    flag=1;
                }else flag=0;
                if (flag!=1){
                    String fname=b.getString("fname");
                    String mname=b.getString("mname");
                    String lname=b.getString("lname");
                    Long mob=Long.parseLong(mno.getText().toString());
                    String em=email.getText().toString();
                    String ad=add.getText().toString();
                    boolean b=dbman.insertUserInfo(fname,mname,lname,ad,mob,em);
                    if (b){
                        Toast.makeText(this, "Welcome to Smart Touch Key", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),PINActivity.class);
                        startActivity(i);
                        finish();
                    }else {
                        Toast.makeText(this, "Something's wrong. Please try again", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(getApplicationContext(),FirstTimeActivity.class);
                        startActivity(i);
                    }
                }
                break;
            case R.id.prevbttn:
                Intent i=new Intent(getApplicationContext(),FirstTimeActivity.class);
                i.putExtra("isBack",true);
                i.putExtra("fname",b.getString("fname"));
                i.putExtra("mname",b.getString("mname"));
                i.putExtra("lname",b.getString("lname"));
                startActivity(i);


        }
    }
}

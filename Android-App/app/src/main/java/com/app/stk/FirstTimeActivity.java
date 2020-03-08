package com.app.stk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.TextViewCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;

import static com.app.stk.FPVerify.fpcansig;

public class FirstTimeActivity extends AppCompatActivity implements View.OnClickListener {
    private TextInputEditText fname;
    private TextInputEditText mname;
    private TextInputEditText lname;
    private AppCompatTextView errtxt;
    private AppCompatButton nxtbttn;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_first_time);
        flag=0;
        fname=findViewById(R.id.addfname);
        mname=findViewById(R.id.addmname);
        lname=findViewById(R.id.addlname);
        errtxt=findViewById(R.id.errtxt);
        nxtbttn=findViewById(R.id.nxtbttn);
        Bundle b=getIntent().getExtras();
        boolean back=b.getBoolean("isBack");
        if (back){
            String rfname=b.getString("fname");
            String rmname=b.getString("mname");
            String rlname=b.getString("lname");
            fname.setText(rfname);
            mname.setText(rmname);
            lname.setText(rlname);
        }
        nxtbttn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (fname.getText().toString().equalsIgnoreCase("")){
            fname.setTextColor(getResources().getColor(R.color.textred));
            errtxt.setVisibility(View.VISIBLE);
            flag=1;
        } else flag=0;
        if (mname.getText().toString().equalsIgnoreCase("")){
            mname.setTextColor(getResources().getColor(R.color.textred));
            errtxt.setVisibility(View.VISIBLE);
            flag=1;
        }else flag=0;
        if (lname.getText().toString().equalsIgnoreCase("")){
            lname.setTextColor(getResources().getColor(R.color.textred));
            errtxt.setVisibility(View.VISIBLE);
            flag=1;
        }else flag=0;
        if (flag!=1){
            Intent i=new Intent(getApplicationContext(),FirstTimeActivity2.class);
            i.putExtra("fname",fname.getText().toString());
            i.putExtra("mname",mname.getText().toString());
            i.putExtra("lname",lname.getText().toString());
            finish();
            startActivity(i);
        }
    }
}

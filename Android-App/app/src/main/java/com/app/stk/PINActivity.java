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

public class PINActivity extends AppCompatActivity implements View.OnClickListener {
    private AppCompatTextView errtxt;
    final String PREFS_NAME = "MyPrefsFile";
    private TextInputEditText addpin;
    private TextInputEditText conpin;
    private AppCompatButton nxtbttn;
    //private AppCompatButton prevbttn;
    private DBManager dbman;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        setContentView(R.layout.activity_pin);
        dbman=new DBManager(this);
        settings = getSharedPreferences(PREFS_NAME, 0);
        errtxt=findViewById(R.id.errtxt);
        addpin=findViewById(R.id.addpin);
        conpin=findViewById(R.id.conpin);
        nxtbttn=findViewById(R.id.nxtbttn);
        //prevbttn=findViewById(R.id.prevbttn);
        //prevbttn.setOnClickListener(this);
        nxtbttn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nxtbttn:
                if (addpin.getText().toString().length()==4){
                    if (addpin.getText().toString().equalsIgnoreCase(conpin.getText().toString())){
                        boolean b=dbman.setPIN(addpin.getText().toString());
                        if (b) {
                            Toast.makeText(this, "Welcome to Smart Touch Key", Toast.LENGTH_LONG).show();
                            settings.edit().putBoolean("my_first_time", false).apply();
                            Intent i=new Intent(getApplicationContext(),FPVerify.class);
                            startActivity(i);
                            finish();
                        }
                        else {
                            Toast.makeText(this, "Something went wrong. Start over", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getApplicationContext(),FirstTimeActivity.class);
                            i.putExtra("isBack",false);
                            startActivity(i);
                            finish();
                        }
                    }else {
                        errtxt.setText("PINs do not match");
                        errtxt.setVisibility(View.VISIBLE);
                    }
                }else {
                    errtxt.setVisibility(View.VISIBLE);
                    errtxt.setText("PIN should be of 4 digits");
                }
                break;
        }
    }
}

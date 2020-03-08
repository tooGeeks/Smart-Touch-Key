package com.app.stk;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.stk.R;

import java.io.DataOutputStream;
import java.io.IOException;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import static com.app.stk.MainActivity.dbman;
import static com.app.stk.commTh.fpcansig;
import static com.app.stk.commTh.fpm;

public class FPDialog extends Dialog implements View.OnClickListener{
    public AppCompatTextView fprtxt;
    public AppCompatTextView webtxt;
    public AppCompatImageView fpricon;
    public AppCompatButton fpcbttn;
    public Activity c;
    private String web;
    private DataOutputStream out;
    public FPDialog(Activity a, String web, DataOutputStream out){
        super(a);
        c=a;
        this.web=web;
        this.out=out;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fprint_dialog);
        webtxt=findViewById(R.id.webtxt);
        fprtxt=findViewById(R.id.fprtxt);
        fpricon=findViewById(R.id.fpricon);
        fpcbttn=findViewById(R.id.fpcbttn);
        fpcbttn.setOnClickListener(this);
        this.getWindow().getAttributes().width=900;
        webtxt.setText(web+" has requested to access your credentials");
        setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        fpcansig.cancel();
        dismiss();
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   out.writeUTF("");
                   out.writeUTF("Website : "+web);
                   out.write(dbman.getCount());
                   out.writeUTF("N/A");
                   out.writeUTF("N/A");
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fpcansig.cancel();
        dismiss();
    }
}
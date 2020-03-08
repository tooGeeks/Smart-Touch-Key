package com.app.stk;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

public class EditDeleteDialog extends Dialog implements View.OnClickListener{
    private String wurl;
    private String wname;
    private AppCompatButton eddcb;
    private AppCompatButton eddeb;
    private AppCompatButton edddb;
    private AppCompatTextView wnametxt;
    private Activity mac;
    private DBManager dbman;
    private int pos;
    public EditDeleteDialog(Activity a,String wname,String wurl, int pos){
        super(a);
        mac=a;
        this.wurl=wurl;
        this.wname=wname;
        this.pos=pos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_delete_dialog);
        dbman=new DBManager(mac);
        wnametxt=findViewById(R.id.eddtxt);
        eddcb=findViewById(R.id.eddcb);
        edddb=findViewById(R.id.edddb);
        eddeb=findViewById(R.id.eddeb);
        this.getWindow().getAttributes().width=900;
        setCanceledOnTouchOutside(false);
        eddcb.setOnClickListener(this);
        edddb.setOnClickListener(this);
        eddeb.setOnClickListener(this);
        wnametxt.setText(wname);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.eddcb:
                Toast.makeText(getContext(), "Canceled", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.edddb:
                boolean b=dbman.deleteEntry(wurl);
                if (b){
                    mac.finish();
                    mac.startActivity(mac.getIntent());
                    Toast.makeText(getContext(), "Deleted"+wurl, Toast.LENGTH_SHORT).show();
                }else Toast.makeText(getContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.eddeb:
                Intent i=new Intent(getContext(),FillCreds.class);
                i.putExtra("isUpdate",true);
                i.putExtra("wurl",wurl);
                i.putExtra("pos",pos);
                i.putExtra("isWebKnown",false);
                setContentView(R.layout.activity_fill_creds);
                mac.startActivity(i);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(mac, "Cancelled", Toast.LENGTH_SHORT).show();
        dismiss();
    }
}

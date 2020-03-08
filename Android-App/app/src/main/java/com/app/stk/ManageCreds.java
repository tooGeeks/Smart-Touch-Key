package com.app.stk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Scene;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Vector;

public class ManageCreds extends AppCompatActivity implements View.OnClickListener {
    public static Context cx;
    private EditDeleteDialog edd;
    public static LinearLayoutCompat tbll;
    public static Toolbar tb;
    public static String[] sx;
    public Object ol;
    private EditText ed1;
    private Button mbttn;
    private static Cursor data;
    protected static DBManager dbman;
    private ListView lv1;
    private ArrayAdapter<String> adapter;
    public static Activity act;

    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
        //getWindow().setWindowAnimations(R.style.WindowAnimationTransition);
        cx=getApplicationContext();
        act=ManageCreds.this;
        super.onCreate(savedInstanceState);
        tb=findViewById(R.id.tbar);
        setSupportActionBar(tb);
        dbman=new DBManager(this);
        data=dbman.getAllCursor();
        setContentView(R.layout.activity_manage_creds);
        lv1=findViewById(R.id.lv1);
        mbttn=findViewById(R.id.mbttn);
        mbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),FillCreds.class);
                i.putExtra("isUpdate",false);
                //setContentView(R.layout.activity_fill_creds);
                startActivity(i);
            }
        });
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item=parent.getItemAtPosition(position).toString();
                Intent i=new Intent(getApplicationContext(),DisplayInfo.class);
                i.putExtra("sitename",item);
                i.putExtra("pos",position);
                startActivity(i);
                setContentView(R.layout.activity_display_info);
            }
        });
        lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                int pos=position+1;
                String wurl=dbman.getObject(pos).getWurl();
                String wname=dbman.getObject(pos).getWname();
                edd=new EditDeleteDialog(act,wname,wurl,pos);
                edd.show();
                return true;
            }
        });
        getList();
        MyAdapter ma=new MyAdapter(this,sx);
        //adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,sx);
        lv1.setAdapter(ma);
    }

    public static boolean getList(){
        SQLiteDatabase db=dbman.getReadableDatabase();
        Cursor s=db.rawQuery("SELECT * FROM pwds",null);
        if (s!=null){
            Vector<String> v=new Vector<>(7,2);
            while (s.moveToNext()) v.addElement(s.getString(1));
            toStr(v);
            return true;
        }
        else return false;

    }
    public static void toStr(Vector<String> v){
        sx=new String[v.size()];
        int i;
        for (i=0;i<v.size();i++)
            sx[i]=v.elementAt(i);

    }

    @Override
    public void onBackPressed() {
        //overridePendingTransition(android.R.anim.cycle_interpolator,android.R.anim.cycle_interpolator);
        Intent i=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        //setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        Intent i=new Intent(cx,FillCreds.class);
        i.putExtra("isUpdate",true);
        i.putExtra("isWebKnown",false);
        startActivity(i);
        //setContentView(R.layout.activity_fill_creds);
    }
}


class MyAdapter extends ArrayAdapter{
    public MyAdapter(Context cx,String[] sx){
        super(cx,android.R.layout.simple_list_item_1,sx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView tv =view.findViewById(android.R.id.text1);
        tv.setTextColor(Color.WHITE);
        return view;
    }
}

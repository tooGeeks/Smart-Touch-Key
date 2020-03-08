package com.app.stk;

import android.content.Context;
import android.widget.Toast;

public class Toaster implements Runnable{
    private Context cx;
    private String msg;
    private int size;
    public Toaster(Context cx,String msg,int len){
        this.cx=cx;
        this.msg=msg;
        size=len;
    }
    @Override
    public void run() {
        Toast.makeText(cx,msg,size).show();
    }
}

package com.app.stk;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.net.Socket;

public class SThread implements Runnable{
    private static Socket s;
    private final static String sr=MainActivity.IP;
    private final static int pt=5900;
    private Context context;
    private Handler mth;
    public SThread(Context ctx){
        context=ctx;
    }
    public void run(){
        try {
            MainActivity.s=new Socket(MainActivity.IP,pt);
            s=MainActivity.s;
            if (s.isConnected()){ ;
                Looper.prepare();
                mth=new Handler(Looper.getMainLooper());
                mth.post(new Toaster(context,"Connected with PC", Toast.LENGTH_LONG));
                new Thread(new commTh(s,context,mth)).start();
            }else {
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
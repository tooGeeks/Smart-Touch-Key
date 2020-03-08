package com.app.stk;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;

import static com.app.stk.MainActivity.addentrydialog;
import static com.app.stk.MainActivity.conntv;
import static com.app.stk.MainActivity.cryptoObject;
import static com.app.stk.MainActivity.dbman;
import static com.app.stk.MainActivity.mact;
import static com.app.stk.MainActivity.mainor;

public class commTh implements Runnable{
    public static CancellationSignal fpcansig;
    public static FingerprintManagerCompat fpm;
    private Socket s;
    private String[] sites;
    public static DataOutputStream out;
    private DataInputStream in;
    private static DataObject d;
    private Handler mth;
    private Context cx;
    public static String newWeb;
    commTh(Socket sc,Context cax,Handler th){
        s=sc;
        try {
            fpcansig=new CancellationSignal();
            cx=cax;
            MainActivity.commThread=Thread.currentThread();
            Vector<String> v=new Vector<>(5,3);
            in=new DataInputStream(s.getInputStream());
            out=new DataOutputStream(s.getOutputStream());
            dbman= dbman;
            //Looper.prepare();
            mth=th;
            Cursor s=dbman.getAllCursor();
            while (s.moveToNext()) v.addElement(s.getString(2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            //out.writeUTF("Hello");
            //out.writeInt(4);

            while (MainActivity.s.isConnected()){
                switch (in.readInt()){
                    case 2:
                        final String site=in.readUTF();
                        Cursor sx=dbman.getAllCursor();
                        sx.moveToFirst();
                        int i=0;
                        int p=dbman.getid(site);
                        if (p!=-1 && p<dbman.getCount()){
                            final DataObject d=dbman.getObject(p+1);
                            Vector<DataObject> dObj=dbman.getAllDObject();
                            if (site!=null) {
                                out.writeUTF("Name : "+d.getWname());
                                if (d.getWurl().equalsIgnoreCase(site)){
                                    out.writeUTF("Website : "+d.getWurl());
                                    out.write(dbman.getCount());
                                    mth.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainActivity.fpd=new FPDialog(mact,d.getWname(),out);
                                            MainActivity.fpd.show();
                                        }
                                    });
                                    fpcansig=new CancellationSignal();
                                    fpm= FingerprintManagerCompat.from(cx);
                                    fpm.authenticate(cryptoObject,0,fpcansig,
                                            new FPCallBack(out,d),null);
                                }else out.writeUTF("Website Not Received");
                            }
                            else out.writeUTF("Error");
                        }else{
                            mth.post(new Toaster(cx,"Entry does not exist", Toast.LENGTH_LONG));
                            out.writeUTF("");
                            out.writeUTF("Website : "+site);
                            out.write(dbman.getCount());
                            out.writeUTF("N/A");
                            out.writeUTF("N/A");
                            newWeb=site;
                            mth.post(new Runnable() {
                                @Override
                                public void run() {
                                    addentrydialog.show();
                                }
                            });
                        }
                        break;
                    case 9:
                        mth.post(new Toaster(cx,"Connection Closed",Toast.LENGTH_SHORT));
                        mth.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity.scnbttn.setVisibility(View.VISIBLE);
                                MainActivity.connbttn.setVisibility(View.VISIBLE);
                                MainActivity.editipholder.setVisibility(View.VISIBLE);
                                conntv.setVisibility(View.GONE);
                                mainor.setVisibility(View.VISIBLE);
                            }
                        });
                        MainActivity.s.close();
                        MainActivity.Sthread.interrupt();
                        Thread.currentThread().interrupt();
                        break;
                    case 3:
                        String checkmsg=in.readUTF();

                    default:
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
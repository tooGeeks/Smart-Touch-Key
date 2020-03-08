package com.app.stk;


import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;

import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import static com.app.stk.MainActivity.maincx;

public class FPCallBack extends FingerprintManagerCompat.AuthenticationCallback{
    private DataObject d;
    private DataOutputStream out;
    public FPCallBack(DataOutputStream os,DataObject dx){super();out=os; d=dx;}
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Toast.makeText(maincx, "Authentication Cancelled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
        Toast.makeText(maincx, "Authentication Help", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        Toast.makeText(maincx, "Authentication Suceeded", Toast.LENGTH_SHORT).show();
        //commTh.sendData(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeUTF(Crypto.decrypt(d.getUname(),DBManager.CRYPTO_PASSWORD));
                    out.writeUTF(Crypto.decrypt(d.getPwd(),DBManager.CRYPTO_PASSWORD));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        MainActivity.fpd.dismiss();
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    out.writeUTF("N/A");
                    out.writeUTF("N/A");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Toast.makeText(maincx, "Authentication Failed", Toast.LENGTH_SHORT).show();
    }
}

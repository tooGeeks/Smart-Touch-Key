package com.app.stk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABAse_NAME = "PwdMan.db";
    public static final String PWD_TABLE_NAME = "pwds";
    public static final String PWD_COLUMN_ID = "_id";
    public static final String PWD_COLUMN_NAME = "name";
    public static final String PWD_COLUMN_DOMAIN = "domain";
    public static final String PWD_COLUMN_USERNAME = "username";
    public static final String PWD_COLUMN_PASSWORD = "password";
    public static int entrycount = 0;
    public static final String PWD_TABLE_USER_INFO = "userinfo";
    public static final String USER_INFO_ID = "_id";
    public static final String USER_INFO_FIELD = "field";
    public static final String USER_INFO_VALUE = "value";
    public static final String CRYPTO_PASSWORD = "53544b";
    private Context cx;

    private HashMap hp;

    public DBManager(Context cx) {
        super(cx, DATABAse_NAME, null, 1);
        this.cx = cx;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PWD_TABLE_USER_INFO + "(_id integer primary key,field text,value text)");
        db.execSQL("create table " + PWD_TABLE_NAME + "(_id integer primary key, name text, domain text, username text,password text,PIN text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PWD_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PWD_TABLE_USER_INFO);
        onCreate(db);
    }

    public boolean insertEntry(String wname, String wurl, String uname, String pwd) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (!wname.equalsIgnoreCase("") && !wurl.equalsIgnoreCase("") && !uname.equalsIgnoreCase("") && !pwd.equalsIgnoreCase("")) {
            ContentValues cv = new ContentValues();
            cv.put("name", wname);
            cv.put("domain", wurl);
            String cryptuname = Crypto.encrypt(uname, CRYPTO_PASSWORD);
            cv.put("username", cryptuname);
            String cryptpwd = Crypto.encrypt(pwd, CRYPTO_PASSWORD);
            cv.put("password", cryptpwd);
            db.insert(PWD_TABLE_NAME, null, cv);
            return true;
        } else {
            return false;
        }
    }

    public Cursor getdata(String dname) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("SELECT * FROM " + PWD_TABLE_NAME + " WHERE domain=" + dname, null);
        return r;
    }

    public int getid(String website) {
        Cursor cs = getAllCursor();
        Vector<DataObject> v = getAllDObject();
        int i = 0;
        String sx;
        DataObject d = null;
        cs.moveToFirst();
        do {
            d = new DataObject(cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));
            if (d == null) return -1;
            sx = d.getWurl();
            if (sx.equalsIgnoreCase(website)) {
                return i;
            }
            i++;
        } while (cs.moveToNext());
        return -1;
    }


    public ArrayList<String> getAllContacts() {
        ArrayList<String> arr_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor r = db.rawQuery("SELECT * FROM " + PWD_TABLE_NAME, null);
        r.moveToFirst();
        while (r.moveToNext()) {
            arr_list.add(r.getString(r.getColumnIndex(PWD_COLUMN_DOMAIN)));
        }
        return arr_list;
    }

    public Cursor getAllCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM pwds", null);
        return cs;
    }

    public boolean getAllString() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor s = getAllCursor();
        if (s != null) {
            String[] str = new String[s.getCount()];
            int i = 0;
            //for (i=0;s.moveToNext();i++){
            // PwdMan.sx[i]=s.getString(s.getColumnIndex(PWD_COLUMN_NAME));
            //}
            return true;
        } else return false;
    }

    public DataObject getObject(int pos) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cs = db.rawQuery("SELECT * FROM pwds WHERE _id=" + pos, null);
        cs.moveToFirst();
        DataObject d = new DataObject(cs.getString(1), cs.getString(2), cs.getString(3), cs.getString(4));
        return d;
    }

    public int getCount() {
        return getAllDObject().size();
    }

    public boolean entryexists(String dnane) {
        Cursor s = getReadableDatabase().rawQuery("SELECT * FROM pwds", null);
        s.moveToFirst();
        while (s.moveToNext()) {
            if (dnane == s.getString(s.getColumnIndex(PWD_COLUMN_DOMAIN))) {
                return true;
            }
        }
        return false;
    }

    public boolean isDBEMpty() {
        if (entrycount == 0) return true;
        else return false;
    }

    public Vector<DataObject> getAllDObject() {
        Cursor cs = getAllCursor();
        cs.moveToFirst();
        int i = 0;
        //int x=getCount();
        DataObject d = new DataObject();
        Vector<DataObject> dObj = new Vector<>(7, 5);
        do {
            d.setWname(cs.getString(1));
            d.setWurl(cs.getString(2));
            d.setUname(cs.getString(3));
            d.setPwd(cs.getString(4));
            dObj.addElement(d);
        } while (cs.moveToNext());
        return dObj;
    }

    public boolean isTableCreated() {
        Cursor cs = getReadableDatabase().rawQuery("SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + PWD_TABLE_NAME + "';", null);
        return cs.getCount() > 0;
    }

    public boolean updateEntry(int pos, String oldwurl, String newwname, String newwurl, String newuname, String newpwd) {
        SQLiteDatabase db = getWritableDatabase();
        int id = getid(oldwurl);
        ContentValues cv = new ContentValues();
        cv.put("name", newwname);
        cv.put("domain", newwurl);
        String cryptuname = Crypto.encrypt(newuname, CRYPTO_PASSWORD);
        cv.put("username", cryptuname);
        String cryptpwd = Crypto.encrypt(newpwd, CRYPTO_PASSWORD);
        cv.put("password", cryptpwd);
        db.update(PWD_TABLE_NAME, cv, PWD_COLUMN_DOMAIN + "=?", new String[]{oldwurl});
        return true;

    }

    public boolean deleteEntry(String wurl) {
        SQLiteDatabase db = getWritableDatabase();
        int id = getid(wurl);
        if (id != -1) {
            db.delete(PWD_TABLE_NAME, PWD_COLUMN_DOMAIN + "=?", new String[]{wurl});
            return true;
        } else {
            Toast.makeText(cx, "Entry Not Found", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean insertUserInfo(String fname,String mname, String lname, String address, long mno, String email) {
        SQLiteDatabase db = getWritableDatabase();
        String crypt;
        if (!fname.equalsIgnoreCase("") && !lname.equalsIgnoreCase("") && !address.equalsIgnoreCase("") && !email.equalsIgnoreCase("") && mno != 0) {
            ContentValues cv = new ContentValues();
            crypt = Crypto.encrypt(fname, CRYPTO_PASSWORD);
            cv.put("field", "fname");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            cv = new ContentValues();
            crypt = Crypto.encrypt(mname, CRYPTO_PASSWORD);
            cv.put("field", "mname");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            cv = new ContentValues();
            crypt = Crypto.encrypt(lname, CRYPTO_PASSWORD);
            cv.put("field", "lname");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            cv = new ContentValues();
            crypt = Crypto.encrypt(address, CRYPTO_PASSWORD);
            cv.put("field", "address");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            cv = new ContentValues();
            crypt = Crypto.encrypt(Long.toString(mno), CRYPTO_PASSWORD);
            cv.put("field", "mno");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            cv = new ContentValues();
            crypt = Crypto.encrypt(email, CRYPTO_PASSWORD);
            cv.put("field", "email");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            return true;
        }
        return false;
    }
    public boolean setPIN(String PIN){
        SQLiteDatabase db=getWritableDatabase();
        if (!PIN.equalsIgnoreCase("")){
            ContentValues cv;
            String crypt;
            cv = new ContentValues();
            crypt = Crypto.encrypt(PIN, CRYPTO_PASSWORD);
            cv.put("field", "PIN");
            cv.put("value", crypt);
            db.insert(PWD_TABLE_USER_INFO, null, cv);
            return true;
        }
        return false;
    }
    public String getFName(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor c=db.rawQuery("SELECT "+USER_INFO_VALUE+" FROM "+PWD_TABLE_USER_INFO+" WHERE "+USER_INFO_FIELD+"=?",new String[]{"fname"});
        String s=c.getString(1);
        if (s!=null){
            return Crypto.decrypt(s,CRYPTO_PASSWORD);
        }
        return null;
    }
    public int getPIN(){
        SQLiteDatabase db=getReadableDatabase();
        Cursor cs=db.rawQuery("SELECT * FROM "+PWD_TABLE_USER_INFO+" WHERE "+USER_INFO_FIELD+"=?",new String[]{"PIN"});
        cs.moveToFirst();
        if (cs!=null && cs.getString(1)!=null && !cs.getString(1).equalsIgnoreCase("")){
            return Integer.parseInt(Crypto.decrypt(cs.getString(cs.getColumnIndex(USER_INFO_VALUE)),CRYPTO_PASSWORD));
        }
        return 0;
    }
}
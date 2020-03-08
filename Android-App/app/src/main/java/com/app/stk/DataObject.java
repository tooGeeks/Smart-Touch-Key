package com.app.stk;

public class DataObject {
    private String wname;
    private String wurl;
    private String uname;
    private String pwd;

    public DataObject(String wname, String wurl, String uname, String pwd) {
        this.wname = wname;
        this.wurl = wurl;
        this.uname = uname;
        this.pwd = pwd;
    }
    public DataObject(){
    }

    public String getWname() {
        return wname;
    }

    public void setWname(String wname) {
        this.wname = wname;
    }

    public String getWurl() {
        return wurl;
    }

    public void setWurl(String wurl) {
        this.wurl = wurl;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}

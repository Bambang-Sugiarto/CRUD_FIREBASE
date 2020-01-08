package com.example.firebase;

public class Post {

    private String title,content,ipk,smt;

    public Post(){

    }

    public String getIpk() {
        return ipk;
    }

    public void setIpk(String ipk) {
        this.ipk = ipk;
    }

    public String getSmt() {
        return smt;
    }

    public void setSmt(String smt) {
        this.smt = smt;
    }

    public Post(String title, String content, String ipk, String smt) {
        this.title = title;
        this.content = content;
        this.ipk = ipk;
        this.smt = smt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

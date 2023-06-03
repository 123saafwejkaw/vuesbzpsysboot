package com.it.entity;


public class Member {

    private int id;
    private String upass;
    private String tname;
    private String filename;
    private String role;
    private String email;

    private String tel;
    private String addr;
    private String content;
    private String zzfile;
    private String shstatus;

    private String htmodel;

    public String getHtmodel() {
        return htmodel;
    }

    public void setHtmodel(String htmodel) {
        this.htmodel = htmodel;
    }

    public String getZzfile() {
        return zzfile;
    }

    public void setZzfile(String zzfile) {
        this.zzfile = zzfile;
    }

    public String getShstatus() {
        return shstatus;
    }

    public void setShstatus(String shstatus) {
        this.shstatus = shstatus;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", upass='" + upass + '\'' +
                ", tname='" + tname + '\'' +
                ", filename='" + filename + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", addr='" + addr + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}

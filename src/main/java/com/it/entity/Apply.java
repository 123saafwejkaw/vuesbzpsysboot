package com.it.entity;


public class Apply {

    private int id;
    private int memberid;
    private int companyid;
    private int vitaeid;
    private int jobid;
    private String savetime;
    private String status;

    private Member member;
    private Member company;
    private Job job;



    private int tdcs;

    private String ht;

    public String getHt() {
        return ht;
    }

    public void setHt(String ht) {
        this.ht = ht;
    }

    public int getTdcs() {
        return tdcs;
    }

    public void setTdcs(int tdcs) {
        this.tdcs = tdcs;
    }

    public Member getCompany() {
        return company;
    }

    public void setCompany(Member company) {
        this.company = company;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getJobid() {
        return jobid;
    }

    public void setJobid(int jobid) {
        this.jobid = jobid;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public int getVitaeid() {
        return vitaeid;
    }

    public void setVitaeid(int vitaeid) {
        this.vitaeid = vitaeid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

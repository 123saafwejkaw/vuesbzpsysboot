package com.it.entity;


public class Job {

    private int id;
    private int companyid;
    private String title;
    private int areaid;
    private int categoryid;
    private String welfarearr;
    private String salary;
    private String education;//学历
    private String experience;
    private String num;
    private String savetime;
    private String content;
    private int lookcs;
    private String status;

    private int znum;
    private int cnum;

    private Area area;
    private Category category;
    private Member company;

    public int getZnum() {
        return znum;
    }

    public void setZnum(int znum) {
        this.znum = znum;
    }

    public int getCnum() {
        return cnum;
    }

    public void setCnum(int cnum) {
        this.cnum = cnum;
    }

    public int getLookcs() {
        return lookcs;
    }

    public void setLookcs(int lookcs) {
        this.lookcs = lookcs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Member getCompany() {
        return company;
    }

    public void setCompany(Member company) {
        this.company = company;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAreaid() {
        return areaid;
    }

    public void setAreaid(int areaid) {
        this.areaid = areaid;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }

    public String getWelfarearr() {
        return welfarearr;
    }

    public void setWelfarearr(String welfarearr) {
        this.welfarearr = welfarearr;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getSavetime() {
        return savetime;
    }

    public void setSavetime(String savetime) {
        this.savetime = savetime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}

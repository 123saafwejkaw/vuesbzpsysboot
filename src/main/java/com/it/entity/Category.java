package com.it.entity;

import java.io.Serializable;
import java.util.*;

public class Category implements Serializable {
	private int id;
	private String name;

    private List<Job> jobs;


    public List<Job> getJobs() {
        return jobs;
    }

    public void setJobs(List<Job> jobs) {
        this.jobs = jobs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

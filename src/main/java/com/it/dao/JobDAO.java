package com.it.dao;

import com.it.entity.Job;

import java.util.HashMap;
import java.util.List;


public interface JobDAO {
	List<Job> selectAll(HashMap map);
	void add(Job job);
	void update(Job job);
    Job findById(int id);
	void delete(int id);
}

package com.it.dao;

import com.it.entity.Welfare;

import java.util.HashMap;
import java.util.List;


public interface WelfareDAO {
	List<Welfare> selectAll(HashMap map);
	void add(Welfare welfare);
	void update(Welfare welfare);
    Welfare findById(int id);
	void delete(int id);
}

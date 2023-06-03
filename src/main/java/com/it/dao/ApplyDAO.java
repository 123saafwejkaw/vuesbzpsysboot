package com.it.dao;

import com.it.entity.Apply;

import java.util.HashMap;
import java.util.List;

public interface ApplyDAO {
	List<Apply> selectAll(HashMap map);
	void add(Apply apply);
    Apply findById(int id);
	void update(Apply apply);
	void delete(int id);
	List<Apply> selectHot(HashMap map);

}

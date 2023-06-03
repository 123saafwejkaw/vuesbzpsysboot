package com.it.dao;

import com.it.entity.Vitae;

import java.util.HashMap;
import java.util.List;

public interface VitaeDAO {
	List<Vitae> selectAll(HashMap map);
	void add(Vitae vitae);
    Vitae findById(int id);
	void update(Vitae vitae);
	void delete(int id);
    Vitae findVitae(int memberid);

}

package com.it.dao;

import com.it.entity.Notice;

import java.util.HashMap;
import java.util.List;

public interface NoticeDAO {
	List<Notice> selectAll(HashMap map);
	void add(Notice notice);
    Notice findById(int id);
	void update(Notice notice);
	void delete(int id);
}

package com.it.dao;

import com.it.entity.Apply;
import com.it.entity.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentDAO {
	List<Comment> selectAll(HashMap map);
	void add(Comment comment);
	Comment findById(int id);
	void update(Comment comment);
	void delete(int id);

}

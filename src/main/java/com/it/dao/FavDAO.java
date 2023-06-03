package com.it.dao;


import com.it.entity.Fav;
import com.it.entity.Message;

import java.util.HashMap;
import java.util.List;

public interface FavDAO {
	List<Fav> selectAll(HashMap map);
	void add(Fav fav);
	Fav findById(int id);
	void delete(int id);
}

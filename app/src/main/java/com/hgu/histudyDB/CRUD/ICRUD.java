package com.hgu.histudyDB.CRUD;

import com.hgu.histudyDB.Info.Students;

public interface ICRUD {
	public int add(Students one);

	public int update(Students one);

	public int delete(Students one);
}

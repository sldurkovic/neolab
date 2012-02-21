package com.neolab.crm.server.persistance.dao;

import java.util.ArrayList;

import com.neolab.crm.shared.domain.News;

public interface NewsDAO {

	ArrayList<News> getNews(int level, ArrayList<Integer> pids, int start, int end);

	int getNewsCount(int level, ArrayList<Integer> pids);
	
	void createNews(int uid, String title, String body, int level, int pid);

	ArrayList<News> getNewsForProject(int level, int pid, int start, int end);

	int getNewsForProjectCount(int level, int pid);
}

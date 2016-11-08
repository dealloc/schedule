package be.dealloc.schedule.contracts.entities.courses;
// Created by dealloc. All rights reserved.

import java.util.Date;

public interface Course
{
	public static final int PRACTICAL = 1;
	public static final int THEORETICAL = 2;
	public static final int OTHER = 3;

	String getName();
	void setName(String name);

	String getLocation();
	void setLocation(String location);

	Date getStart();
	void setStart(Date start);

	Date getEnd();
	void setEnd(Date end);

	int getType();
	void setType(int type);

	String getUrl();
	void setUrl(String url);

	String getCalendar();
	void setCalendar(String calendar);
}

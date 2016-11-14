package be.dealloc.schedule.contracts.entities.courses;
// Created by dealloc. All rights reserved.

import android.graphics.drawable.Drawable;

import java.util.Date;

public interface Course
{
	int PRACTICAL = 1;
	int THEORETICAL = 2;
	int OTHER = 3;

	String getName();

	void setName(String name);

	String getLocation();

	void setLocation(String location);

	String getTeacher();

	void setTeacher(String teacher);

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

	Drawable getIcon();
}

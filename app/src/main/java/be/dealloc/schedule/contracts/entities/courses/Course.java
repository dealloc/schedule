package be.dealloc.schedule.contracts.entities.courses;
// Created by dealloc. All rights reserved.

import java.util.Date;

public interface Course
{
	public static final int PRACTICAL = 1;
	public static final int THEORETICAL = 2;
	public static final int OTHER = 3;

	String getName();

	String getLocation();

	Date getStart();

	Date getEnd();

	int getType();

	String getUrl();

	public void setName(String name);

	public void setLocation(String location);

	public void setStart(Date start);

	public void setEnd(Date end);

	public void setType(int type);

	public void setUrl(String url);
}

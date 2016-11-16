package be.dealloc.schedule.contracts.entities.calendars;
// Created by dealloc. All rights reserved.

public interface Calendar
{
	boolean getActive();

	void setActive(boolean value);

	String getSecurityCode();

	void setSecurityCode(String value);

	void setName(String name);

	String getName();

	String getURl();
}

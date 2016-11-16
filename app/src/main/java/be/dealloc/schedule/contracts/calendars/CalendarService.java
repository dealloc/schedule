package be.dealloc.schedule.contracts.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.courses.Course;

import java.util.List;

public interface CalendarService
{
	void createSystemCalendar(final String name);

	void deleteSystemCalendar(final String name);

	Course addCourseToSystemCalendar(final String name, Course course);

	Course addCourseToSystemCalendar(final SystemCalendar calendar, Course course);

	List<SystemCalendar> getSystemCalendars();

	interface SystemCalendar
	{
		long getId();

		String getName();

		String getOwner();
	}
}

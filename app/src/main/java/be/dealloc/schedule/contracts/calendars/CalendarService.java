package be.dealloc.schedule.contracts.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.courses.Course;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public interface CalendarService
{
	void createSystemCalendar(final String name);

	void deleteSystemCalendar(final String name);

	void deleteScheduleCalendars();

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

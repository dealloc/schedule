package be.dealloc.schedule.contracts.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.courses.Course;

import java.util.List;

public interface CalendarService
{
	void createSystemCalendar(final String name);

	void deleteSystemCalendar(final String name);

	List<String> getSystemCalendars();

	Course addCourseToSystemCalendar(final String name, Course course);
}

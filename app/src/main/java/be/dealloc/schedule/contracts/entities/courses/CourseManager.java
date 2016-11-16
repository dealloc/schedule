package be.dealloc.schedule.contracts.entities.courses;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.EntityManager;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;

import java.util.List;

public interface CourseManager extends EntityManager<Course>
{
	Course fromRaw(Object raw);

	List<Course> forMonth(final int year, final int month);

	List<Course> forCalendar(Calendar calendar);

	List<Course> forCalendar(final String code);

	List<Course> getUpcoming();

	void purge(String code);
}

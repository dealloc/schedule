package be.dealloc.schedule.contracts.entities.courses;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.EntityManager;

import java.util.List;

public interface CourseManager extends EntityManager<Course>
{
	Course fromRaw(Object raw);

	List<Course> forMonth(final int year, final int month);
}

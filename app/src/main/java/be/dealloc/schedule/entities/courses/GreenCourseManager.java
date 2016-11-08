package be.dealloc.schedule.entities.courses;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;

import javax.inject.Inject;

/**
 * GreenCourseManager class.
 *
 * @author dealloc
 */
public class GreenCourseManager implements CourseManager
{
	private final GreenCourseDao dao;

	@Inject
	public GreenCourseManager(DaoSession session)
	{
		this.dao = session.getGreenCourseDao();
	}

	@Override
	public Course create()
	{
		return new GreenCourse();
	}

	@Override
	public Course find(long id)
	{
		return this.dao.load(id);
	}

	@Override
	public void save(Course course)
	{
		this.dao.save((GreenCourse)course);
	}

	@Override
	public void delete(Course course)
	{
		this.dao.delete((GreenCourse)course);
	}

	@Override
	public void delete(long id)
	{
		this.dao.deleteByKey(id);
	}
}

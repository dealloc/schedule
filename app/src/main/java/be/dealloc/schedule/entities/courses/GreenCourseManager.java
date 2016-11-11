package be.dealloc.schedule.entities.courses;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.greendao.DaoSession;
import be.dealloc.schedule.greendao.GreenCourseDao;
import biweekly.component.VEvent;

import javax.inject.Inject;
import java.util.Date;

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
		this.dao.save((GreenCourse) course);
	}

	@Override
	public void delete(Course course)
	{
		this.dao.delete((GreenCourse) course);
	}

	@Override
	public void delete(long id)
	{
		this.dao.deleteByKey(id);
	}

	@Override
	public Course fromRaw(Object raw)
	{
		if (!(raw instanceof VEvent))
			throw new RuntimeException("Invalid event type passed to manager.");

		VEvent event = (VEvent)raw;
		if (!event.getDescription().getValue().startsWith("[H]") || event.getDescription().getValue().startsWith("[W]")) // Drop non courses
			return null; // TODO build support for non-course entries (they're usually deadlines)

		Course course = this.create();

		// Summary contains meta data like [H] and the location. The regex below strips the meta data leaving only the classname
		String name = event.getSummary().getValue().replaceAll("((\\[(W|H)\\])\\s|\\s\\((DT\\/([A-Z0-9]\\.?)*)\\))", "");
		String teacher = event.getDescription().getValue().split("\n")[1].replace("door\\s", "");
		String location = event.getLocation().getValue();
		Date start = event.getDateStart().getValue().getRawComponents().toDate();
		Date end = event.getDateEnd().getValue().getRawComponents().toDate();
		int type = Course.OTHER;
		if (event.getSummary().getValue().startsWith("[H]"))
			type = Course.THEORETICAL;
		else if (event.getSummary().getValue().startsWith("[W]"))
			type = Course.PRACTICAL;

		course.setName(name);
		course.setTeacher(teacher);
		course.setLocation(location);
		course.setStart(start);
		course.setEnd(end);
		course.setType(type);
		return course;
	}
}

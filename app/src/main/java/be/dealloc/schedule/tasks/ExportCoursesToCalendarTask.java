package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.calendars.CalendarService;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;

import javax.inject.Inject;
import java.util.List;

public class ExportCoursesToCalendarTask extends BasicTask<Course>
{
	private static final String CALENDAR_NAME = "calendar.name";
	private final CourseManager manager;
	private final CalendarService service;

	public void execute(BasicTask.TaskCallback callback, List<Course> courses)
	{
		this.setCallback(callback);

		this.execute(courses.toArray(new Course[]{}));
	}

	@Inject
	public ExportCoursesToCalendarTask(CourseManager manager, CalendarService service)
	{
		this.manager = manager;
		this.service = service;
	}

	@Override
	protected Void doInBackground(Course... courses)
	{
		int total = courses.length;
		int current = 0;
		String template = "Processing (%d/%d)";

		this.service.deleteSystemCalendar(CALENDAR_NAME);
		this.service.createSystemCalendar(CALENDAR_NAME);

		for (Course course : courses)
		{
			this.processCourse(course);

			this.publishProgress(String.format(template, ++current, total));
		}

		this.finish();

		return null;
	}

	private void processCourse(Course course)
	{
		course = this.service.addCourseToSystemCalendar(CALENDAR_NAME, course);

		this.manager.save(course);
	}
}

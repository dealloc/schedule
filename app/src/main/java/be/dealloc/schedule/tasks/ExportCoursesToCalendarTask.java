package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import android.os.AsyncTask;
import be.dealloc.schedule.contracts.calendars.CalendarService;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;

import javax.inject.Inject;

public class ExportCoursesToCalendarTask extends AsyncTask<Course, String, Void>
{
	private final CourseManager manager;
	private final CalendarService service;

	@Inject
	public ExportCoursesToCalendarTask(CourseManager manager, CalendarService service)
	{
		this.manager = manager;
		this.service = service;
	}

	@Override
	protected Void doInBackground(Course... courses)
	{
		for (Course course : courses)
			this.processCourse(course);

		return null;
	}

	private void processCourse(Course course)
	{
	}
}

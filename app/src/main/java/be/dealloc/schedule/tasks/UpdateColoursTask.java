package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.facades.Colour;

import javax.inject.Inject;
import java.util.List;

public class UpdateColoursTask extends BasicTask<Calendar>
{
	private final CourseManager manager;
	private int colour;

	@Inject
	public UpdateColoursTask(CourseManager manager)
	{
		this.manager = manager;
	}

	public void execute(Calendar calendar, int colour, BasicTask.TaskCallback callback)
	{
		this.colour = colour;
		this.setCallback(callback);
		this.execute(calendar);
	}

	@Override
	protected Void doInBackground(Calendar... calendars)
	{
		Calendar calendar = calendars[0];
		List<Course> courses = this.manager.forCalendar(calendar);
		int total = courses.size();
		int current = 0;
		String template = "Processing (%d/%d)";

		for (Course course : courses)
		{
			if (course.getType() == Course.PRACTICAL)
				course.setColour(Colour.darken(this.colour));
			else if (course.getType() == Course.THEORETICAL)
				course.setColour(this.colour);
			else
				course.setColour(Colour.inverse(this.colour));

			this.manager.save(course);

			this.publishProgress(String.format(template, ++current, total));
		}

		this.finish();
		return null;
	}
}

package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.facades.Colour;
import be.dealloc.schedule.system.Application;
import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;
import java.io.IOException;

public class ProcessCalendarTask extends BasicTask<Calendar>
{
	private final NetworkService service;
	private final CourseManager courseManager;
	private final String STR_PARSING;
	private final String STR_CONNECTING;

	@Inject
	public ProcessCalendarTask(NetworkService service, CourseManager courseManager)
	{
		this.service = service;
		this.courseManager = courseManager;
		this.STR_PARSING = Application.string(R.string.parsing_data);
		this.STR_CONNECTING = Application.string(R.string.fetching_data);
	}

	public void execute(Calendar calendar, BasicTask.TaskCallback callback)
	{
		this.setCallback(callback);
		this.execute(calendar);
	}

	@Override
	protected Void doInBackground(Calendar... calendars)
	{
		try
		{
			this.publishProgress(STR_CONNECTING);
			Logger.i("Fetching ical file from %s", calendars[0].getURl());
			String body = this.service.downloadSynchronous(calendars[0].getURl());
			publishProgress(STR_PARSING);
			if (body.startsWith("BEGIN:VCALENDAR"))
			{
				int colour = Application.color(R.color.primary);
				int darker = Colour.darken(colour);
				int inverse = Colour.inverse(colour);

				Logger.i("Purging old entries from calendar %s", calendars[0].getName());
				courseManager.purge(calendars[0].getSecurityCode()); // Purge entries when the new ones have arrived.
				ICalendar calendar = Biweekly.parse(body).first();

				for (VEvent event : calendar.getEvents())
				{
					Course course = courseManager.fromRaw(event);
					if (course != null) // some events may be dropped.
					{
						course.setCalendar(calendars[0].getSecurityCode());
						if (course.getType() == Course.PRACTICAL)
							course.setColour(darker);
						else if (course.getType() == Course.THEORETICAL)
							course.setColour(colour);
						else if (course.getType() == Course.MEETING)
							course.setColour(colour);
						else
							course.setColour(inverse);

						courseManager.save(course);
					}
				}

				System.gc(); // Call a garbage collection to collect all dangling event objects.
				finish();
			}
			else
			{
				fail(new Throwable("Invalid ical response returned."));
			}
		}
		catch (IOException exception)
		{
			fail(exception);
		}

		return null;
	}

	@Override
	protected void fail(Throwable error)
	{
		Logger.e(error, "Failed to process calendar.");
		super.fail(error);
	}
}

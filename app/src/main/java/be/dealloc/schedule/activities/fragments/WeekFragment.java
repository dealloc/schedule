package be.dealloc.schedule.activities.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.system.Application;
import be.dealloc.schedule.system.Fragment;
import butterknife.BindView;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekViewEvent;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class WeekFragment extends Fragment implements MonthLoader.MonthChangeListener
{
	@BindView(R.id.week_calendar) WeekView calendar;
	@Inject CourseManager manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View layout = this.setLayout(inflater, container, R.layout.fragment_week);

		this.calendar.setMonthChangeListener(this);

		return layout;
	}

	@Override
	public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth)
	{
		List<Course> courses = this.manager.forMonth(newYear, newMonth);
		List<WeekViewEvent> events = new ArrayList<>();

		Logger.i("Loading %d courses for %d/%d", courses.size(), newMonth, newYear);
		for (Course course : courses)
		{
			WeekViewEvent event = new WeekViewEvent();
			Calendar start = Calendar.getInstance();
			start.setTime(course.getStart());
			Calendar end = Calendar.getInstance();
			end.setTime(course.getEnd());

			if (course.getType() == Course.PRACTICAL)
				event.setColor(Application.color(R.color.primary_dark));
			else if (course.getType() == Course.THEORETICAL)
				event.setColor(Application.color(R.color.primary));
			else
				event.setColor(Application.color(R.color.accent));
			event.setName(course.getName());
			event.setStartTime(start);
			event.setEndTime(end);
			event.setLocation(course.getLocation());

			events.add(event);
		}

		return events;
	}
}

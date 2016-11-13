package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.system.Activity;

import javax.inject.Inject;
import java.util.List;

public class MainActivity extends Activity
{
	@Inject CalendarManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		List<Calendar> active = this.manager.getActiveCalendars();
		if (active.size() == 0)
			this.navigate(RegistrationActivity.class);
		else
			this.navigate(CalendarActivity.class);
	}
}

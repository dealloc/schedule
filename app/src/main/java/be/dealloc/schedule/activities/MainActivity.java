package be.dealloc.schedule.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.facades.Dialog;
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
		{
			Dialog.confirm(this, R.string.no_calendar, R.string.no_calendar_dialog, (DialogInterface dialogInterface, int i) ->
			{
				this.navigate(RegistrationActivity.class);
			}, (dialogInterface, i) -> this.finish()).show();
		}
		else
		{
			this.navigate(CalendarActivity.class);
		}
	}
}

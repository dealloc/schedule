package be.dealloc.schedule.activities;

import android.os.Bundle;
import android.widget.Toast;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.calendars.CalendarService;
import be.dealloc.schedule.providers.EntityProvider;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Application;
import butterknife.OnClick;

import javax.inject.Inject;

public class SettingsActivity extends Activity
{
	@Inject
	CalendarService service;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_settings);
		this.getSupportActionBar().setHomeButtonEnabled(true);
	}

	@OnClick(R.id.settings_btnReset)
	public void onResetClicked()
	{
		Application.provider().context().deleteDatabase(EntityProvider.DB_NAME);
		// Restart the app
		Application.restart();
	}

	@OnClick(R.id.settings_btnDeleteCalendar)
	public void onDeleteCalendarClicked()
	{
		String feedback = this.getResources().getString(R.string.calendars_deleted);
		this.service.deleteScheduleCalendars();
		Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show();
	}
}

package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;
import butterknife.OnClick;

import javax.inject.Inject;

public class RegistrationActivity extends Activity
{
	@Inject CalendarManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_registration);
	}

	@OnClick(R.id.registration_lblHelp)
	public void onHelpClicked()
	{
		Dialog.msgbox(this, R.string.app_name, R.string.todo_calendar_help).show();
	}

	@OnClick(R.id.registration_btnEnterCode)
	public void onRegisterClicked()
	{
		Dialog.input(this, R.string.app_name, R.string.enter_security_code, (d, content) ->
		{
			Calendar calendar = this.manager.create();
			calendar.setSecurityCode(content);
			calendar.setActive(true);
			this.manager.save(calendar);
			this.navigate(MainActivity.class);
			this.finish();
		}, null).show();
	}
}

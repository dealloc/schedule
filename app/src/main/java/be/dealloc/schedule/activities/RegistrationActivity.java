package be.dealloc.schedule.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.ViewFlipper;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Application;
import be.dealloc.schedule.tasks.ProcessCalendarTask;
import butterknife.BindView;
import butterknife.OnClick;

import javax.inject.Inject;

public class RegistrationActivity extends Activity implements ProcessCalendarTask.ProcessCallback
{
	@Inject CalendarManager manager;
	@BindView(R.id.activity_registration) ViewFlipper flipper;
	@BindView(R.id.register_txtLoadingStatus) TextView lblStatus;
	Calendar calendar;

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
		Dialog.input(this, R.string.app_name, R.string.enter_security_code, (d, code) ->
		{
			this.flipper.showNext(); // Show the loading part of the view
			this.createCalendar(code);
		}, null).show();
	}

	private void createCalendar(String code)
	{
		this.calendar = this.manager.create();
		this.calendar.setSecurityCode(code);
		Application.provider().calendarProcessor().execute(this.calendar, this);
	}

	@Override
	public void onProgress(String status)
	{
		this.lblStatus.setText(status);
	}

	@Override
	public void onFailure(Throwable error)
	{
		Dialog.msgbox(this, error.getMessage()).show();
		this.flipper.showNext();
	}

	@Override
	public void onSucces()
	{
		this.calendar.setActive(true);
		this.manager.save(this.calendar);
		this.navigate(MainActivity.class);
	}
}

package be.dealloc.schedule.activities;

import android.content.Intent;
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
	public static final String SECURITYCODE_INTENT = "be.dealloc.schedule.activities.RegistrationActivity.SECURITY_CODE";
	@Inject CalendarManager manager;
	@BindView(R.id.activity_registration) ViewFlipper flipper;
	@BindView(R.id.register_txtLoadingStatus) TextView lblStatus;
	Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_registration);

		Intent intention = this.getIntent();
		if (intention.hasExtra(SECURITYCODE_INTENT))
		{
			// The security code has been sent along!
			this.flipper.showNext();
			this.createCalendar(intention.getStringExtra(SECURITYCODE_INTENT));
		}
	}

	@OnClick(R.id.registration_lblHelp)
	public void onHelpClicked()
	{
		Dialog.msgbox(this, R.string.app_name, R.string.todo_calendar_help);
	}

	@OnClick(R.id.registration_btnEnterCode)
	public void onRegisterClicked()
	{
		Dialog.input(this, R.string.app_name, R.string.enter_security_code, (d, code) ->
		{
			this.flipper.showNext(); // Show the loading part of the web
			this.createCalendar(code);
		}, null);
	}

	@OnClick(R.id.registration_btnDesiderius)
	public void onDesideriusClicked()
	{
		Dialog.warning(this, R.string.desiderius_dialog, (d, i) -> {
			navigate(DesideriusActivity.class);
		});
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
		Dialog.msgbox(this, error.getMessage()); // TODO convert to error dialog
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

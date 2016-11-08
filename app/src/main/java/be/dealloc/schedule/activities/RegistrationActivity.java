package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;
import butterknife.OnClick;

public class RegistrationActivity extends Activity
{
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
}

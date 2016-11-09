package be.dealloc.schedule.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;

public class MainActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_main);

		Dialog.confirm(this, R.string.no_calendar, R.string.no_calendar_dialog, (DialogInterface dialogInterface, int i) ->
		{
			this.navigate(RegistrationActivity.class);
			this.finish();
		}, (dialogInterface, i) -> this.finish()).show();
	}
}

package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.providers.EntityProvider;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Application;
import butterknife.OnClick;

public class SettingsActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_settings);
	}

	@OnClick(R.id.settings_btnReset)
	public void onResetClicked()
	{
		Application.provider().context().deleteDatabase(EntityProvider.DB_NAME);
		// Restart the app
		Application.restart();
	}
}

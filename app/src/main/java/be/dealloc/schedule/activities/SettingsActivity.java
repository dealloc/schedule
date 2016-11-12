package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.system.Activity;

public class SettingsActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_settings);
	}
}

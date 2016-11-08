package be.dealloc.schedule.activities;

import android.os.Bundle;
import android.widget.Toast;
import be.dealloc.schedule.R;
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
		Toast.makeText(this, "TODO explain how to get security code", Toast.LENGTH_SHORT).show();
	}
}

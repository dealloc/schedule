package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.system.Activity;
import butterknife.OnClick;

public class DesideriusActivity extends Activity
{
	private static final String CAS_URL = "https://cas.ehb.be/login?service=https%3A%2F%2Fdesiderius.ehb.be%2F";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_desiderius);
	}

	@OnClick(R.id.desiderius_btnLogin)
	public void onLoginClicked()
	{
	}
}

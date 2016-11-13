package be.dealloc.schedule.system;
// Created by dealloc. All rights reserved.

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

public class Activity extends AppCompatActivity
{
	private boolean injected = false;

	@Override
	public void onCreate(Bundle saved, PersistableBundle persisted)
	{
		super.onCreate(saved, persisted);
		this.inject();
	}

	@Override
	protected void onCreate(Bundle saved)
	{
		super.onCreate(saved);
		this.inject();
	}

	private synchronized void inject()
	{
		if (!this.injected)
		{
			Application.inject(this);
			this.injected = true;
		}
	}

	protected void setLayout(final int layout)
	{
		this.setContentView(layout);
		ButterKnife.bind(this);
	}

	protected void navigate(Class<? extends Activity> activity)
	{
		this.navigate(activity, true);
	}

	protected void navigate(Class<? extends Activity> activity, boolean finish)
	{
		startActivity(new Intent(this, activity));
		if (finish)
			this.finish();
	}

	protected void swap(final int container, Fragment fragment)
	{
		getSupportFragmentManager()
				.beginTransaction()
				.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
				.replace(container, fragment)
				.commit();
	}
}

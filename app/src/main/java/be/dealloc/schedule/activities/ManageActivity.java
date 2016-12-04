package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.activities.fragments.CalendarManagerFragment;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Fragment;

public class ManageActivity extends Activity
{
	private static final String FRAGMENT_KEY = ManageActivity.class.getName();
	protected Fragment current;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setLayout(R.layout.activity_share);
		this.getSupportActionBar().setHomeButtonEnabled(true);

		if (bundle == null)
			this.swap(R.id.activity_share, new CalendarManagerFragment());
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		Fragment fragment = (Fragment) this.getSupportFragmentManager()
				.getFragment(bundle, FRAGMENT_KEY);

		if (fragment != null)
			this.swap(R.id.activity_share, fragment);
	}

	@Override
	public synchronized void swap(int container, Fragment fragment)
	{
		if (this.current != null)
			this.current.setRetainInstance(false);

		super.swap(container, fragment, FRAGMENT_KEY);

		this.current = fragment;
		this.current.setRetainInstance(true);
	}
}

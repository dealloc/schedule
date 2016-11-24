package be.dealloc.schedule.activities;

import android.os.Bundle;
import android.widget.Toast;
import be.dealloc.schedule.R;
import be.dealloc.schedule.activities.fragments.RegistrationFragment;
import be.dealloc.schedule.activities.fragments.UpdateCalendarFragment;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Fragment;

import javax.inject.Inject;

public class RegistrationActivity extends Activity implements RegistrationFragment.RegistrationHost, UpdateCalendarFragment.CalendarUpdateCallback
{
	private static final String FRAGMENT_KEY = "be.dealloc.schedule.activities.RegistrationActivity.FRAGMENT_KEY";
	private static final String PROCESSING_KEY = "be.dealloc.schedule.activities.RegistrationActivity.PROCESSING_KEY";

	protected Fragment current;
	@Inject CalendarManager manager;
	private String processingCode;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setLayout(R.layout.activity_registration);
		if (bundle == null)
		{
			RegistrationFragment fragment = new RegistrationFragment();
			fragment.setHost(this);
			this.swap(fragment);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		this.getSupportFragmentManager()
				.putFragment(bundle, FRAGMENT_KEY, this.current);

		if (this.current instanceof UpdateCalendarFragment)
			((UpdateCalendarFragment) this.current).setCallback(null);

		bundle.putString(PROCESSING_KEY, processingCode);
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		Fragment fragment = (Fragment) this.getSupportFragmentManager().getFragment(bundle, FRAGMENT_KEY);
		if (fragment != null)
		{
			this.swap(fragment);
			if (fragment instanceof UpdateCalendarFragment)
				((UpdateCalendarFragment) fragment).setCallback(this);
		}

		this.processingCode = bundle.getString(PROCESSING_KEY);
	}

	public synchronized void swap(Fragment fragment)
	{
		if (this.current != null)
			this.current.setRetainInstance(false);

		this.current = fragment;
		this.current.setRetainInstance(true);

		super.swap(R.id.registration_layout, fragment);
	}

	@Override
	public void startDesiderius()
	{
		this.navigate(DesideriusActivity.class);
	}

	@Override
	public void initCalendar(String code)
	{
		Bundle bundle = new Bundle();
		bundle.putString(UpdateCalendarFragment.SECURITY_CODE, code);
		UpdateCalendarFragment fragment = new UpdateCalendarFragment();
		fragment.setCallback(this);
		fragment.setArguments(bundle);

		Calendar calendar = this.manager.create();
		calendar.setName("EHB calendar"); // TODO fetch from user?
		calendar.setSecurityCode(code);
		calendar.setActive(true);
		this.processingCode = code;
		this.manager.save(calendar);

		this.swap(fragment);
	}

	@Override
	public void onFailure(Throwable error)
	{
		Toast.makeText(this, R.string.token_error, Toast.LENGTH_SHORT).show();
		this.swap(new RegistrationFragment());
		if (this.processingCode != null)
		{
			Calendar calendar = this.manager.findBySecurityCode(this.processingCode);
			this.manager.delete(calendar);
		}
	}

	@Override
	public void onSuccess()
	{
		this.navigate(MainActivity.class);
	}
}

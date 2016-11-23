package be.dealloc.schedule.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import be.dealloc.schedule.R;
import be.dealloc.schedule.activities.dispatchers.CalendarNavigationDispatcher;
import be.dealloc.schedule.activities.fragments.ListFragment;
import be.dealloc.schedule.activities.fragments.WeekFragment;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Application;
import be.dealloc.schedule.system.Fragment;
import be.dealloc.schedule.tasks.BasicTask;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import static butterknife.ButterKnife.findById;

public class CalendarActivity extends Activity implements CalendarNavigationDispatcher.DispatcherTarget
{
	private static final String FRAGMENT_KEY = CalendarActivity.class.getCanonicalName();

	@Inject CalendarManager manager;
	@BindView(R.id.calendar_drawer) DrawerLayout drawer;
	private Fragment current = null;
	private Snackbar snackbar = null;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setLayout(R.layout.activity_calendar);
		Toolbar toolbar = findById(this, R.id.calendar_toolbar);
		setSupportActionBar(toolbar);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		this.drawer.addDrawerListener(toggle);
		toggle.syncState();

		ButterKnife.<NavigationView>findById(this, R.id.calendar_navview).setNavigationItemSelectedListener(new CalendarNavigationDispatcher(this, drawer));

		if (bundle == null)
			this.swap(R.id.calendar_content, new WeekFragment());
	}

	@Override
	public void onBackPressed()
	{
		if (this.drawer.isDrawerOpen(GravityCompat.START))
			this.drawer.closeDrawer(GravityCompat.START);
		else
			super.onBackPressed();
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		Fragment fragment = (Fragment) this.getSupportFragmentManager()
				.getFragment(bundle, FRAGMENT_KEY);

		if (fragment != null)
			this.swap(R.id.activity_share, fragment);
	}

	@OnClick(R.id.calendar_fab)
	public void onFloatingButtonClicked(FloatingActionButton button)
	{
		button.setEnabled(false);
		Calendar calendar = this.manager.getActiveCalendars().get(0);

		Application.provider().calendarProcessor().execute(calendar, new BasicTask.TaskCallback()
		{
			@Override
			public void onProgress(String status)
			{
				if (snackbar != null)
					snackbar.dismiss();

				snackbar = Snackbar.make(button, status, Snackbar.LENGTH_INDEFINITE);
				snackbar.show();
			}

			@Override
			public void onFailure(Throwable error)
			{
				if (snackbar != null)
					snackbar.dismiss();
				button.setEnabled(true);
				Logger.e(error, "Failed to refresh %s", calendar.getSecurityCode());
				Dialog.error(CalendarActivity.this, R.string.generic_web_error);
			}

			@Override
			public void onSucces()
			{
				recreate(); // Restart the acivity. Easiest way to reload everything
			}
		});
	}

	@Override
	public void onCalendarClicked()
	{
		if (!(this.current instanceof WeekFragment))
		{
			this.current = new WeekFragment();
			this.swap(R.id.calendar_content, this.current);
		}
	}

	@Override
	public void onListClicked()
	{
		if (!(this.current instanceof ListFragment))
		{
			this.current = new ListFragment();
			this.swap(R.id.calendar_content, this.current);
		}
	}

	@Override
	public void onSettingsClicked()
	{
		this.navigate(SettingsActivity.class, false);
	}

	@Override
	public void onShareClicked()
	{
		this.navigate(ShareActivity.class, false);
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

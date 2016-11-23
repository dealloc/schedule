package be.dealloc.schedule.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;
import be.dealloc.schedule.R;
import be.dealloc.schedule.activities.dispatchers.CalendarNavigationDispatcher;
import be.dealloc.schedule.activities.fragments.ListFragment;
import be.dealloc.schedule.activities.fragments.UpdateCalendarFragment;
import be.dealloc.schedule.activities.fragments.WeekFragment;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Application;
import be.dealloc.schedule.system.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

import static butterknife.ButterKnife.findById;

public class CalendarActivity extends Activity implements CalendarNavigationDispatcher.DispatcherTarget, UpdateCalendarFragment.CalendarUpdateCallback
{
	private static final String FRAGMENT_KEY = CalendarActivity.class.getCanonicalName();
	private static final String BUTTON_STATE = "be.dealloc.schedule.activities.CalendarActivity.BUTTON_STATE";

	@Inject CalendarManager manager;
	@BindView(R.id.calendar_drawer) DrawerLayout drawer;
	@BindView(R.id.calendar_fab) FloatingActionButton btnRefresh;
	private Fragment current = null;

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
	protected void onSaveInstanceState(Bundle state)
	{
		super.onSaveInstanceState(state);
		state.putBoolean(BUTTON_STATE, this.btnRefresh.isEnabled());
	}

	@Override
	public void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		Fragment fragment = (Fragment) this.getSupportFragmentManager()
				.getFragment(bundle, FRAGMENT_KEY);

		if (fragment != null)
			this.swap(fragment);

		this.btnRefresh.setEnabled(bundle.getBoolean(BUTTON_STATE, true));
	}

	@OnClick(R.id.calendar_fab)
	public void onFloatingButtonClicked(FloatingActionButton button)
	{
		button.setEnabled(false);
		UpdateCalendarFragment fragment = new UpdateCalendarFragment();
		fragment.setCallback(this);
		this.swap(fragment);
	}

	@Override
	public void onCalendarClicked()
	{
		if (!(this.current instanceof WeekFragment))
		{
			this.swap(new WeekFragment());
		}
	}

	@Override
	public void onListClicked()
	{
		if (!(this.current instanceof ListFragment))
		{
			this.swap(new ListFragment());
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

	public synchronized void swap(Fragment fragment)
	{
		if (this.current != null)
			this.current.setRetainInstance(false);

		this.current = fragment;
		this.current.setRetainInstance(true);

		super.swap(R.id.calendar_content, fragment, FRAGMENT_KEY);
	}

	@Override
	public void onFailure(Throwable error)
	{
		this.btnRefresh.setEnabled(true);
		Logger.e(error, "Failed to update calendar.");
		this.swap(new WeekFragment());
	}

	@Override
	public void onSuccess()
	{
		this.btnRefresh.setEnabled(true);
		Toast.makeText(this, Application.string(R.string.calendar_processed), Toast.LENGTH_SHORT).show();
		this.swap(new WeekFragment());
	}
}

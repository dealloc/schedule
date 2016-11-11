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
import be.dealloc.schedule.system.Activity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.findById;

public class CalendarActivity extends Activity implements CalendarNavigationDispatcher.DispatcherTarget
{
	@BindView(R.id.calendar_drawer) DrawerLayout drawer;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_calendar);
		Toolbar toolbar = findById(this, R.id.calendar_toolbar);
		setSupportActionBar(toolbar);

		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		this.drawer.addDrawerListener(toggle);
		toggle.syncState();

		ButterKnife.<NavigationView>findById(this, R.id.calendar_navview).setNavigationItemSelectedListener(new CalendarNavigationDispatcher(this, drawer));
	}

	@Override
	public void onBackPressed()
	{
		if (this.drawer.isDrawerOpen(GravityCompat.START))
			this.drawer.closeDrawer(GravityCompat.START);
		else
			super.onBackPressed();
	}

	@OnClick(R.id.calendar_fab)
	public void onFloatingButtonClicked(FloatingActionButton button)
	{
		Snackbar.make(button, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
	}
}

package be.dealloc.schedule.activities.dispatchers;
// Created by dealloc. All rights reserved.

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import be.dealloc.schedule.R;

public class CalendarNavigationDispatcher implements NavigationView.OnNavigationItemSelectedListener
{
	private final DispatcherTarget target;
	private final DrawerLayout drawer;

	public CalendarNavigationDispatcher(DispatcherTarget target, DrawerLayout drawer)
	{
		this.target = target;
		this.drawer = drawer;
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item)
	{
		boolean handled = false;

		switch (item.getItemId())
		{
			case R.id.nav_calendar:
				handled = true;
				this.target.onCalendarClicked();
				break;
			case R.id.nav_list:
				handled = true;
				this.target.onListClicked();
				break;
			case R.id.nav_settings:
				handled = true;
				this.target.onSettingsClicked();
				break;
			case R.id.nav_share:
				handled = true;
				this.target.onShareClicked();
				break;
		}

		this.drawer.closeDrawer(GravityCompat.START);
		return handled;
	}

	public interface DispatcherTarget
	{
		void onCalendarClicked();

		void onListClicked();

		void onSettingsClicked();

		void onShareClicked();
	}
}

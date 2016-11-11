package be.dealloc.schedule.activities.dispatchers;
// Created by dealloc. All rights reserved.

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;

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
		// Handle navigation clicks.
		this.drawer.closeDrawer(GravityCompat.START);
		return false;
	}

	public interface DispatcherTarget
	{
	}
}

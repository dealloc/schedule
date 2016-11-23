package be.dealloc.schedule.providers;
// Created by dealloc. All rights reserved.

import android.content.Context;
import be.dealloc.schedule.contracts.calendars.CalendarService;
import be.dealloc.schedule.services.calendars.CalendarProviderService;
import be.dealloc.schedule.system.Application;
import dagger.Module;
import dagger.Provides;

@Module
public class SystemProvider
{
	private Application app;

	public SystemProvider(Application app)
	{
		this.app = app;
	}

	@Provides
	public Application providesApplication()
	{
		return this.app;
	}

	@Provides
	public Context providesContext()
	{
		return this.app.getApplicationContext();
	}

	@Provides
	public CalendarService providesCalendarService(CalendarProviderService service)
	{
		return service;
	}
}

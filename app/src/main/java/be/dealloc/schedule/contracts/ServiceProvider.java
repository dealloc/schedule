package be.dealloc.schedule.contracts;
// Created by dealloc. All rights reserved.

import android.content.Context;
import be.dealloc.schedule.activities.CalendarActivity;
import be.dealloc.schedule.activities.MainActivity;
import be.dealloc.schedule.activities.RegistrationActivity;
import be.dealloc.schedule.activities.SettingsActivity;
import be.dealloc.schedule.activities.fragments.*;
import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.providers.EntityProvider;
import be.dealloc.schedule.providers.NetworkProvider;
import be.dealloc.schedule.providers.SystemProvider;
import be.dealloc.schedule.tasks.ExportCoursesToCalendarTask;
import be.dealloc.schedule.tasks.ProcessCalendarTask;
import dagger.Component;

@Component(modules = {
		SystemProvider.class,
		EntityProvider.class,
		NetworkProvider.class,
})
public interface ServiceProvider
{
	void inject(MainActivity activity);

	void inject(RegistrationActivity activity);

	void inject(CalendarActivity activity);

	void inject(WeekFragment activity);

	void inject(SettingsActivity activity);

	void inject(ListFragment fragment);

	void inject(ShareFragment fragment);

	void inject(GoogleCalendarFragment fragment);

	void inject(UpdateCalendarFragment fragment);

	NetworkService network();

	ProcessCalendarTask calendarProcessor();

	ExportCoursesToCalendarTask exportProcessor();

	Context context();
}

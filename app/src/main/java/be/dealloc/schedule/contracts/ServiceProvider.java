package be.dealloc.schedule.contracts;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.activities.MainActivity;
import be.dealloc.schedule.activities.RegistrationActivity;
import be.dealloc.schedule.providers.EntityProvider;
import be.dealloc.schedule.providers.NetworkProvider;
import be.dealloc.schedule.providers.SystemProvider;
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
}

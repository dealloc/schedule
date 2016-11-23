package be.dealloc.schedule.providers;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.services.network.OkNetworkService;
import dagger.Module;
import dagger.Provides;

@Module
public class NetworkProvider
{
	@Provides
	public NetworkService providesNetworkService(OkNetworkService service)
	{
		return service;
	}
}

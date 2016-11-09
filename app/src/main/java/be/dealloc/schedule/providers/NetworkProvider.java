package be.dealloc.schedule.providers;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.services.network.LoopNetworkService;
import dagger.Module;

@Module
public class NetworkProvider
{
	public NetworkService providesNetworkService(LoopNetworkService service)
	{
		return service;
	}
}

package be.dealloc.schedule.providers;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.contracts.network.NfcService;
import be.dealloc.schedule.services.network.Base64NfcService;
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

	@Provides
	public NfcService providesNfcService(Base64NfcService service)
	{
		return service;
	}
}

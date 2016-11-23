package be.dealloc.schedule.contracts.network;
// Created by dealloc. All rights reserved.

import javax.inject.Singleton;

@Singleton
public interface NetworkService
{
	void download(String url, NetworkCallback callback);

	String downloadSynchronous(String url);

	interface NetworkCallback
	{
		void onSucces(int status, String body);

		void onFailure(Throwable error);
	}
}

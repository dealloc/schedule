package be.dealloc.schedule.contracts.network;
// Created by dealloc. All rights reserved.

public interface NetworkService
{
	void download(String url, NetworkCallback callback);

	String downloadSynchronous(String url);

	interface NetworkCallback
	{
		void onSucces(int status, String body);

		void onFailure(int status, String body, Throwable error);
	}
}

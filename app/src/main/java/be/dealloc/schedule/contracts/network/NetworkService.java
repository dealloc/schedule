package be.dealloc.schedule.contracts.network;
// Created by dealloc. All rights reserved.

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public interface NetworkService
{
	void download(String url, NetworkCallback callback);

	String downloadSynchronous(String url) throws IOException;

	interface NetworkCallback
	{
		void onSucces(int status, String body);

		void onFailure(Throwable error);
	}
}

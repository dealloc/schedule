package be.dealloc.schedule.services.network;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.network.NetworkService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import javax.inject.Inject;

public class LoopNetworkService implements NetworkService
{
	private final AsyncHttpClient service;

	@Inject
	public LoopNetworkService()
	{
		this.service = new AsyncHttpClient();
	}

	@Override
	public void download(String url, NetworkCallback callback)
	{
		this.service.get(url, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(int status, Header[] headers, byte[] body)
			{
				callback.onSucces(status, new String(body));
			}

			@Override
			public void onFailure(int status, Header[] headers, byte[] body, Throwable error)
			{
				callback.onFailure(status, new String(body), error);
			}
		});
	}

	@Override
	public String downloadSynchronous(String url)
	{
		return null;
	}
}

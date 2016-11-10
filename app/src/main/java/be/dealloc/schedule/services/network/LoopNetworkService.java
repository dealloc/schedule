package be.dealloc.schedule.services.network;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.network.NetworkService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;
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
		this.service.get(url, this.wrap(callback));
	}

	@Override
	public void downloadSynchronous(String url, NetworkCallback callback)
	{
		(new SyncHttpClient()).get(url, this.wrap(callback));
	}

	private AsyncHttpResponseHandler wrap(NetworkCallback callback)
	{
		return (new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(int status, Header[] headers, byte[] body)
			{
				String response = (body == null ? "" : new String(body));
				callback.onSucces(status, response);
			}

			@Override
			public void onFailure(int status, Header[] headers, byte[] body, Throwable error)
			{
				String response = (body == null ? "" : new String(body));
				callback.onFailure(status, response, error);
			}
		});
	}
}

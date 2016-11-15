package be.dealloc.schedule.services.network;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.network.NetworkService;
import com.orhanobut.logger.Logger;
import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;

public class OkNetworkService implements NetworkService
{
	private OkHttpClient client;

	@Inject
	public OkNetworkService()
	{
		this.client = new OkHttpClient();
	}

	@Override
	public void download(String url, NetworkCallback callback)
	{
		Request request = new Request.Builder().url(url).build();
		Call call = this.client.newCall(request);

		call.enqueue(new Callback()
		{
			@Override
			public void onFailure(Call call, IOException e)
			{
				callback.onFailure(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				callback.onSucces(response.code(), response.body().string());
			}
		});
	}

	@Override
	public String downloadSynchronous(String url)
	{
		Request request = new Request.Builder().url(url).build();
		Call call = this.client.newCall(request);

		try
		{
			return call.execute().body().string();
		}
		catch (IOException exception)
		{
			Logger.e(exception, "Failed to download %s", url);
			return "";
		}
	}
}

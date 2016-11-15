package be.dealloc.schedule.services.network;
// Created by dealloc. All rights reserved.

import android.os.Looper;
import be.dealloc.schedule.contracts.network.NetworkService;
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
		try
		{
			Request request = new Request.Builder().url(url).build();
			Call call = this.client.newCall(request);

			if (Looper.getMainLooper().getThread() == Thread.currentThread())
			{
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
			else
			{
				Response response = call.execute();
				callback.onSucces(response.code(), response.body().string());
			}
		}
		catch (IOException e)
		{
			callback.onFailure(e);
		}
	}
}

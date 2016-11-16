package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

public abstract class BasicTask<T> extends AsyncTask<T, String, Void>
{
	private TaskCallback callback;

	void setCallback(TaskCallback callback)
	{
		this.callback = callback;
	}

	@Override
	protected void onProgressUpdate(String... values)
	{
		if (this.callback != null)
			this.callback.onProgress(values[0]);
	}

	protected void fail(Throwable error)
	{
		new Handler(Looper.getMainLooper()).post(() -> this.callback.onFailure(error));
	}

	void finish()
	{
		(new Handler(Looper.getMainLooper())).post(this.callback::onSucces);
	}

	public interface TaskCallback
	{
		void onProgress(String status);

		void onFailure(Throwable error);

		void onSucces();
	}
}

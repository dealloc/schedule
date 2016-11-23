package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import com.orhanobut.logger.Logger;

public abstract class BasicTask<T> extends AsyncTask<T, String, Void>
{
	private TaskCallback callback;

	public void setCallback(TaskCallback callback)
	{
		this.callback = callback;
		if (this.getStatus() == Status.FINISHED)
			this.finish();
	}

	@Override
	protected void onProgressUpdate(String... values)
	{
		if (this.callback != null)
			this.callback.onProgress(values[0]);
	}

	protected void fail(Throwable error)
	{
		if (this.callback != null)
			new Handler(Looper.getMainLooper()).post(() -> this.callback.onFailure(error));
		else
			Logger.e(error, "Uncaught task failure.");
	}

	void finish()
	{
		if (this.callback != null)
			(new Handler(Looper.getMainLooper())).post(this.callback::onSucces);
	}

	public interface TaskCallback
	{
		void onProgress(String status);

		void onFailure(Throwable error);

		void onSucces();
	}
}

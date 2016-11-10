package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.system.Application;
import biweekly.Biweekly;
import biweekly.ICalendar;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class ProcessCalendarTask extends AsyncTask<Calendar, String, Void>
{
	private final NetworkService service;
	private ProcessCallback callback;
	private final String STR_PARSING;

	@Inject
	public ProcessCalendarTask(NetworkService service)
	{
		this.service = service;
		this.STR_PARSING = Application.string(R.string.parsing_data);
	}

	public void execute(Calendar calendar, ProcessCallback callback)
	{
		this.callback = callback;
		this.execute(calendar);
	}

	@Override
	protected Void doInBackground(Calendar... calendars)
	{
		this.service.downloadSynchronous(calendars[0].getURl(), new NetworkService.NetworkCallback()
		{
			@Override
			public void onSucces(int status, String body)
			{
				publishProgress(STR_PARSING);
				if (body.startsWith("BEGIN:VCALENDAR"))
				{
					ICalendar calendar = Biweekly.parse(body).first();
					publishProgress(String.format("Parsed calendar with %d events in!", calendar.getEvents().size()));
					callback.onSucces();
				}
				else
				{
					fail(new Throwable("Invalid ical response returned."));
				}
			}

			@Override
			public void onFailure(int status, String body, Throwable error)
			{
				fail(error);
			}
		});

		return null;
	}

	@Override
	protected void onProgressUpdate(String... values)
	{
		this.callback.onProgress(values[0]);
	}

	private void fail(Throwable error)
	{
		Logger.e(error, "Failed to process calendar.");
		new Handler(Looper.getMainLooper()).post(() -> this.callback.onFailure(error));
	}

	public interface ProcessCallback
	{
		void onProgress(String status);

		void onFailure(Throwable error);

		void onSucces();
	}
}

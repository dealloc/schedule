package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import android.os.AsyncTask;
import android.widget.TextView;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.system.Application;

public class FetchCalendarTask extends AsyncTask<Calendar, String, Void>
{
	private final TextView lblStatus;
	private final String STR_PARSING;

	public FetchCalendarTask(TextView lblStatus)
	{
		this.lblStatus = lblStatus;
		this.STR_PARSING = Application.string(R.string.parsing_data);
	}

	@Override
	protected Void doInBackground(Calendar... calendars)
	{
		try
		{
			Thread.sleep(3000); // pretend we're downloading the iCal file here.
			this.publishProgress(STR_PARSING); // Fetch from strings.
			Thread.sleep(3000); // pretend we're parsing the iCal file here.
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	@Override
	protected void onProgressUpdate(String... values)
	{
		this.lblStatus.setText(values[0]);
	}

	@Override
	protected void onPostExecute(Void aVoid)
	{
		// TODO post results back to caller.
		// TODO refactor this to resource
		this.lblStatus.setText("Persisting data.");
	}
}

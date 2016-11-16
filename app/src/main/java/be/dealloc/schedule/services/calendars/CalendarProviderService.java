package be.dealloc.schedule.services.calendars;
// Created by dealloc. All rights reserved.

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.support.v4.app.ActivityCompat;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.calendars.CalendarService;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.system.Application;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static be.dealloc.schedule.system.Application.provider;

public class CalendarProviderService implements CalendarService
{
	private static final String CALENDAR_OWNER = "EHB calendars";
	private List<String> systemCalendars;

	@Inject
	public CalendarProviderService()
	{
	}

	@Override
	public void createSystemCalendar(final String name)
	{
		if (this.getSystemCalendars().contains(name))
			return;

		Uri.Builder builder = this.buildQuery();
		ContentValues values = this.buildValues(name);

		Uri ui = provider().context().getContentResolver().insert(builder.build(), values);
	}

	@Override
	public void deleteSystemCalendar(String name)
	{
		Uri.Builder builder = this.buildQuery();

		provider().context().getContentResolver().delete(builder.build(), null, null);
	}

	@Override
	public List<String> getSystemCalendars()
	{
		if (this.systemCalendars != null && !this.systemCalendars.isEmpty())
			return this.systemCalendars;

		this.systemCalendars = new ArrayList<>();

		String[] projection = new String[]{
				Calendars._ID,
				Calendars.NAME,
				Calendars.OWNER_ACCOUNT,
				Calendars.ACCOUNT_NAME,
				Calendars.ACCOUNT_TYPE,
		};

		if (ActivityCompat.checkSelfPermission(provider().context(), Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED)
		{
			Cursor cursor = provider().context().getContentResolver().query(Calendars.CONTENT_URI,
					projection,
					Calendars.VISIBLE + " = 1",
					null,
					Calendars._ID + " ASC");

			if (cursor.moveToFirst())
			{
				do
				{
					long id = cursor.getLong(0);
					String name = cursor.getString(1);
					String owner = cursor.getString(2);

					Logger.i("Retrieved calendar %s with ID %d (owned by %s)", name, id, owner);
					this.systemCalendars.add(name);
				} while (cursor.moveToNext());
			}

			cursor.close();
		}

		return this.systemCalendars;
	}

	@Override
	public Course addCourseToSystemCalendar(final String name, Course course)
	{
		return null;
	}

	private Uri.Builder buildQuery()
	{
		Uri.Builder builder = Calendars.CONTENT_URI.buildUpon();
		builder.appendQueryParameter(Calendars.ACCOUNT_NAME, CALENDAR_OWNER);
		builder.appendQueryParameter(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");

		return builder;
	}

	private ContentValues buildValues(String name)
	{
		ContentValues values = new ContentValues();
		values.put(Calendars.ACCOUNT_NAME, CALENDAR_OWNER);
		values.put(Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
		values.put(Calendars.NAME, name);
		values.put(Calendars.CALENDAR_DISPLAY_NAME, name);
		values.put(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_OWNER);
		values.put(Calendars.OWNER_ACCOUNT, CALENDAR_OWNER);
		values.put(Calendars.CALENDAR_TIME_ZONE, "Europe/Brussels");
		values.put(Calendars.CALENDAR_COLOR, Application.color(R.color.primary));
		values.put(Calendars.SYNC_EVENTS, 1);

		return values;
	}
}

package be.dealloc.schedule.providers;
// Created by dealloc. All rights reserved.

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.entities.calendars.GreenCalendarManager;
import be.dealloc.schedule.entities.courses.GreenCourseManager;
import be.dealloc.schedule.greendao.DaoMaster;
import be.dealloc.schedule.greendao.DaoSession;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class EntityProvider
{
	public static final String DB_NAME = "schedule-db";
	private final DaoSession session;

	@Inject
	public EntityProvider(Context context)
	{
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
		SQLiteDatabase db = helper.getWritableDatabase();
		this.session = new DaoMaster(db).newSession();
	}

	@Provides
	@Singleton
	public CourseManager providesCourseManager()
	{
		return new GreenCourseManager(this.session);
	}

	@Provides
	@Singleton
	public CalendarManager providesCalendarManager()
	{
		return new GreenCalendarManager(this.session);
	}

}

package be.dealloc.schedule.providers;
// Created by dealloc. All rights reserved.

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.entities.courses.DaoMaster;
import be.dealloc.schedule.entities.courses.DaoSession;
import be.dealloc.schedule.entities.courses.GreenCourseManager;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;

@Module
public class EntityProvider
{
	private static final String DB_NAME = "schedule-db";
	private final DaoSession session;

	@Inject
	public EntityProvider(Context context)
	{
		DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME);
		SQLiteDatabase db = helper.getWritableDatabase();
		this.session = new DaoMaster(db).newSession();
	}

	@Provides
	public CourseManager providesCourseManager()
	{
		return new GreenCourseManager(this.session);
	}
}

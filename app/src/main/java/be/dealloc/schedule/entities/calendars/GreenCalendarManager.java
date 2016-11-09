package be.dealloc.schedule.entities.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.greendao.DaoSession;
import be.dealloc.schedule.greendao.GreenCalendarDao;

import java.util.ArrayList;
import java.util.List;

/**
 * GreenCalendarManager class.
 *
 * @author dealloc
 */
public class GreenCalendarManager implements CalendarManager
{
	private GreenCalendarDao dao;

	public GreenCalendarManager(DaoSession session)
	{
		this.dao = session.getGreenCalendarDao();
	}

	@Override
	public Calendar create()
	{
		return new GreenCalendar();
	}

	@Override
	public Calendar find(long id)
	{
		return this.dao.load(id);
	}

	@Override
	public void save(Calendar calendar)
	{
		this.dao.save((GreenCalendar)calendar);
	}

	@Override
	public void delete(Calendar calendar)
	{
		this.dao.delete((GreenCalendar)calendar);
	}

	@Override
	public void delete(long id)
	{
		this.dao.deleteByKey(id);
	}

	public List<Calendar> getActiveCalendars()
	{
		List<GreenCalendar> calendars = this.dao.queryBuilder()
				.where(GreenCalendarDao.Properties.Active.eq(true))
				.list();

		return new ArrayList<>(calendars); // Downcast the collection
	}
}

package be.dealloc.schedule.contracts.entities.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.EntityManager;

import java.util.List;

public interface CalendarManager extends EntityManager<Calendar>
{
	List<Calendar> getActiveCalendars();
}

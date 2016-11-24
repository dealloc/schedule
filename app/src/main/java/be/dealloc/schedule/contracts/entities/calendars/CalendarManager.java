package be.dealloc.schedule.contracts.entities.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.EntityManager;

import javax.inject.Singleton;
import java.util.List;

@Singleton
public interface CalendarManager extends EntityManager<Calendar>
{
	List<Calendar> getActiveCalendars();

	Calendar findBySecurityCode(String code);
}

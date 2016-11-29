package be.dealloc.schedule.contracts.network;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.calendars.Calendar;

public interface NfcService
{
	String encode(Calendar calendar);

	NfcResult decode(String result);

	public interface NfcResult
	{
		String getName();

		String getCode();
	}
}

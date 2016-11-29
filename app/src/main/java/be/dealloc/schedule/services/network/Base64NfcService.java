package be.dealloc.schedule.services.network;
// Created by dealloc. All rights reserved.

import android.util.Base64;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.network.NfcService;

import javax.inject.Inject;

public class Base64NfcService implements NfcService
{
	private static final String SPLIT_CHAR = "#";

	@Inject
	public Base64NfcService()
	{
	}

	@Override
	public String encode(Calendar calendar)
	{
		String encodedName = Base64.encodeToString(calendar.getName().getBytes(), Base64.DEFAULT);
		String encodedCode = Base64.encodeToString(calendar.getSecurityCode().getBytes(), Base64.DEFAULT);

		return encodedName + SPLIT_CHAR + encodedCode;
	}

	@Override
	public NfcResult decode(String result)
	{
		String[] parts = result.split(SPLIT_CHAR);

		String name = new String(Base64.decode(parts[0], Base64.DEFAULT));
		String code = new String(Base64.decode(parts[1], Base64.DEFAULT));

		return new NfcResult()
		{
			@Override
			public String getName()
			{
				return name;
			}

			@Override
			public String getCode()
			{
				return code;
			}
		};
	}
}

package be.dealloc.schedule.entities.calendars;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

@Entity
public class GreenCalendar implements Calendar
{
	@Id private long id;
	private boolean active;
	@Index(unique = true) String securityCode;
	private static final String DESIDERIUS_URL = "https://desiderius.ehb.be/index.php?application=Chamilo%5CApplication%5CCalendar&go=ICal&security_token=";

	@Generated(hash = 1065246825)
	public GreenCalendar(long id, boolean active, String securityCode)
	{
		this.id = id;
		this.active = active;
		this.securityCode = securityCode;
	}

	@Generated(hash = 2024624522)
	public GreenCalendar()
	{
	}

	public long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public boolean getActive()
	{
		return this.active;
	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public String getSecurityCode()
	{
		return this.securityCode;
	}

	public void setSecurityCode(String securityCode)
	{
		this.securityCode = securityCode;
	}

	@Override
	public String getURl()
	{
		return DESIDERIUS_URL + this.getSecurityCode();
	}
}

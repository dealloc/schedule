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
	private static final String DESIDERIUS_URL = "https://desiderius.ehb.be/index.php?application=Chamilo%5CApplication%5CCalendar&go=ICal&security_token=";
	@Id private Long id;
	private boolean active = false;
	@Index(unique = true) String securityCode;
	@Index(unique = true) String name;

	@Generated(hash = 601717691)
	public GreenCalendar(Long id, boolean active, String securityCode, String name)
	{
		this.id = id;
		this.active = active;
		this.securityCode = securityCode;
		this.name = name;
	}

	@Generated(hash = 2024624522)
	public GreenCalendar()
	{
	}

	public Long getId()
	{
		return this.id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	@Override
	public boolean getActive()
	{
		return this.active;
	}

	@Override
	public void setActive(boolean active)
	{
		this.active = active;
	}

	@Override
	public String getSecurityCode()
	{
		return this.securityCode;
	}

	@Override
	public void setSecurityCode(String securityCode)
	{
		this.securityCode = securityCode;
	}

	@Override
	public String getURl()
	{
		return DESIDERIUS_URL + this.getSecurityCode();
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
}

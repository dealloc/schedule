package be.dealloc.schedule.entities.courses;
// Created by dealloc. All rights reserved.

import android.graphics.drawable.Drawable;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.system.Application;
import com.amulyakhare.textdrawable.TextDrawable;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

@Entity
public class GreenCourse implements Course
{
	@Id
	private Long id;
	private String name;
	private String teacher;
	private String location;
	private Date start;
	private Date end;
	private int type;
	private String url;
	private String calendar;
	private Long systemId;
	private Integer colour;

	@Generated(hash = 1213956852)
	public GreenCourse(Long id, String name, String teacher, String location, Date start, Date end, int type, String url,
					   String calendar, Long systemId, Integer colour)
	{
		this.id = id;
		this.name = name;
		this.teacher = teacher;
		this.location = location;
		this.start = start;
		this.end = end;
		this.type = type;
		this.url = url;
		this.calendar = calendar;
		this.systemId = systemId;
		this.colour = colour;
	}

	@Generated(hash = 2103233940)
	public GreenCourse()
	{
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public String getLocation()
	{
		return this.location;
	}

	@Override
	public Date getStart()
	{
		return this.start;
	}

	@Override
	public Date getEnd()
	{
		return this.end;
	}

	@Override
	public int getType()
	{
		return this.type;
	}

	@Override
	public String getUrl()
	{
		return this.url;
	}

	public Long getId()
	{
		return this.id;
	}

	@Override
	public Long systemId()
	{
		return this.systemId;
	}

	@Override
	public void setSystemId(Long systemId)
	{
		this.systemId = systemId;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	@Override
	public void setName(String name)
	{
		this.name = name;
	}

	@Override
	public void setLocation(String location)
	{
		this.location = location;
	}

	@Override
	public String getTeacher()
	{
		return this.teacher;
	}

	@Override
	public void setTeacher(String teacher)
	{
		this.teacher = teacher;
	}

	@Override
	public void setStart(Date start)
	{
		this.start = start;
	}

	@Override
	public void setEnd(Date end)
	{
		this.end = end;
	}

	@Override
	public void setType(int type)
	{
		this.type = type;
	}

	@Override
	public void setUrl(String url)
	{
		this.url = url;
	}

	@Override
	public String getCalendar()
	{
		return this.calendar;
	}

	@Override
	public void setCalendar(String calendar)
	{
		this.calendar = calendar;
	}

	@Override
	public Drawable getIcon()
	{
		if (this.getType() == Course.PRACTICAL)
			return TextDrawable.builder().buildRound(Application.string(R.string.practical_letter), this.getColour());
		else if (this.getType() == Course.THEORETICAL)
			return TextDrawable.builder().buildRound(Application.string(R.string.theoretical_letter), this.getColour());
		else
			return TextDrawable.builder().buildRound(Application.string(R.string.other_letter), this.getColour());
	}

	@Override
	public int getColour()
	{
		return this.colour;
	}

	@Override
	public void setColour(int colour)
	{
		this.colour = colour;
	}

	public Long getSystemId()
	{
		return this.systemId;
	}

	public void setColour(Integer colour)
	{
		this.colour = colour;
	}
}

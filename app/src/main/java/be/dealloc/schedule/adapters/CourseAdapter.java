package be.dealloc.schedule.adapters;
// Created by dealloc. All rights reserved.

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.courses.Course;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static butterknife.ButterKnife.findById;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
{
	private List<Course> courses;
	private Fragment fragment;

	public CourseAdapter(Fragment fragment, List<Course> courses)
	{
		this.fragment = fragment;
		this.courses = courses;
	}

	@Override
	public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_list_item, parent, false);

		return new CourseViewHolder(view);
	}

	@Override
	public void onBindViewHolder(CourseViewHolder holder, int position)
	{
		Course course = this.courses.get(position);
		holder.setName(course.getName());
		holder.setTeachers(course.getTeacher());
		holder.setLocation(course.getLocation());
		holder.setImage(course.getIcon());
		holder.setWhen(course.getStart(), course.getEnd());
		holder.setCourse(course);
	}

	@Override
	public int getItemCount()
	{
		return this.courses.size();
	}

	class CourseViewHolder extends RecyclerView.ViewHolder
	{
		private ImageView imgType;
		private TextView txtName;
		private TextView txtTeachers;
		private TextView txtLocation;
		private TextView txtWhen;
		private Course course;

		public CourseViewHolder(View view)
		{
			super(view);
			this.imgType = findById(view, R.id.list_imgType);
			this.txtName = findById(view, R.id.list_txtName);
			this.txtTeachers = findById(view, R.id.list_txtTeachers);
			this.txtLocation = findById(view, R.id.list_txtLocation);
			this.txtWhen = findById(view, R.id.list_txtWhen);
			view.setOnClickListener(view1 -> Dialog.course(fragment, this.course));
		}

		public void setImage(Drawable image)
		{
			this.imgType.setImageDrawable(image);
		}

		public void setName(String name)
		{
			this.txtName.setText(name);
		}

		public void setTeachers(String teacher)
		{
			this.txtTeachers.setText(teacher);
		}

		public void setLocation(String location)
		{
			this.txtLocation.setText(location);
		}

		public void setWhen(Date start, Date end)
		{
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
			String when = String.format("%s-%s", dateFormat.format(start), dateFormat.format(end));

			this.txtWhen.setText(when);
		}

		public void setCourse(Course course)
		{
			this.course = course;
		}
	}
}

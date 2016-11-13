package be.dealloc.schedule.adapters;
// Created by dealloc. All rights reserved.

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.courses.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
{
	private List<Course> courses;

	public CourseAdapter(List<Course> courses)
	{
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
		holder.setName(this.courses.get(position).getName());
	}

	@Override
	public int getItemCount()
	{
		return this.courses.size();
	}

	class CourseViewHolder extends RecyclerView.ViewHolder
	{
		private TextView txtName;

		public CourseViewHolder(View view)
		{
			super(view);
			this.txtName = (TextView) view.findViewById(R.id.list_txtName);
		}

		public void setName(String name)
		{
			this.txtName.setText(name);
		}
	}
}

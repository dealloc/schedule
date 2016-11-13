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
import be.dealloc.schedule.system.Application;
import com.amulyakhare.textdrawable.TextDrawable;

import java.util.List;

import static butterknife.ButterKnife.findById;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder>
{
	private List<Course> courses;
	private final Drawable DRAWABLE_PRACTICAL;
	private final Drawable DRAWABLE_THEORETICAL;
	private final Drawable DRAWABLE_OTHER;

	public CourseAdapter(List<Course> courses)
	{
		this.courses = courses;
		DRAWABLE_PRACTICAL = TextDrawable.builder().buildRound(Application.string(R.string.practical_letter), Application.color(R.color.primary_dark));
		DRAWABLE_THEORETICAL = TextDrawable.builder().buildRound(Application.string(R.string.theoretical_letter), Application.color(R.color.primary));
		DRAWABLE_OTHER = TextDrawable.builder().buildRound(Application.string(R.string.other_letter), Application.color(R.color.accent));
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

		if (course.getType() == Course.PRACTICAL)
			holder.setImage(DRAWABLE_PRACTICAL);
		else if (course.getType() == Course.THEORETICAL)
			holder.setImage(DRAWABLE_THEORETICAL);
		else
			holder.setImage(DRAWABLE_OTHER);
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

		public CourseViewHolder(View view)
		{
			super(view);
			this.imgType = findById(view, R.id.list_imgType);
			this.txtName = findById(view, R.id.list_txtName);
			this.txtTeachers = findById(view, R.id.list_txtTeachers);
			this.txtLocation = findById(view, R.id.list_txtLocation);
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
	}
}

package be.dealloc.schedule.activities.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.contracts.network.NetworkService;
import be.dealloc.schedule.system.Fragment;
import be.dealloc.schedule.tasks.ProcessCalendarTask;
import butterknife.BindView;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class UpdateCalendarFragment extends Fragment implements ProcessCalendarTask.TaskCallback
{
	private static final String STATUS_KEY = "be.dealloc.schedule.activities.fragments.UpdateCalendarFragment.STATUS_KEY";

	@BindView(R.id.update_txtStatus) TextView txtStatus;

	@Inject NetworkService service;
	@Inject CourseManager courseManager;
	@Inject CalendarManager calendarManager;

	private ProcessCalendarTask task;
	private CalendarUpdateCallback callback;

	@Override
	public void onCreate(@Nullable Bundle bundle)
	{
		super.onCreate(bundle);

		this.task = new ProcessCalendarTask(this.service, this.courseManager);
		this.task.execute(this.calendarManager.getActiveCalendars().get(0), this);
		Logger.i("Starting execution of calendar processing.");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
	{
		return this.setLayout(inflater, container, R.layout.fragment_update_calendar);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		this.task.setCallback(null);
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (this.task != null)
			this.task.setCallback(this);
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		if (this.txtStatus != null)
			bundle.putString(STATUS_KEY, this.txtStatus.getText().toString());
	}

	@Override
	public void onViewStateRestored(@Nullable Bundle bundle)
	{
		super.onViewStateRestored(bundle);
		if (bundle != null && this.txtStatus != null)
			this.txtStatus.setText(bundle.getString(STATUS_KEY));
	}

	@Override
	public void onProgress(String status)
	{
		if (this.txtStatus != null)
			this.txtStatus.setText(status);
	}

	@Override
	public void onFailure(Throwable error)
	{
		if (this.callback != null)
			this.callback.onFailure(error);
	}

	@Override
	public void onSucces()
	{
		if (this.callback != null)
			this.callback.onSuccess();
	}

	public void setCallback(CalendarUpdateCallback callback)
	{
		this.callback = callback;
	}

	public interface CalendarUpdateCallback
	{
		void onFailure(Throwable error);

		void onSuccess();
	}
}

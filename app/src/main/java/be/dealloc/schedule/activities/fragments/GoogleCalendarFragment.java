package be.dealloc.schedule.activities.fragments;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Fragment;
import be.dealloc.schedule.tasks.BasicTask;
import be.dealloc.schedule.tasks.ExportCoursesToCalendarTask;

import javax.inject.Inject;

import static be.dealloc.schedule.system.Application.provider;

public class GoogleCalendarFragment extends Fragment implements BasicTask.TaskCallback
{
	public static final String BUNDLE_CALENDAR = "be.dealloc.schedule.activities.fragments.GoogleCalendarFragment.BUNDLE_CALENDAR";
	private static final int CALENDAR_PERMISSION_CALLBACK = 0x0001;

	private String code;
	@Inject CourseManager manager;
	private ExportCoursesToCalendarTask task;
	private ProgressDialog dialog;

	@Override
	public void onCreate(@Nullable Bundle bundle)
	{
		super.onCreate(bundle);

		if (this.getArguments() == null)
			throw new RuntimeException("Invalid bundle options passed!");

		this.code = this.getArguments().getString(BUNDLE_CALENDAR);

		if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission_group.CALENDAR) != PackageManager.PERMISSION_GRANTED)
			this.requestPermissions(new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, CALENDAR_PERMISSION_CALLBACK);
		else
			this.exportCalendar();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
	{
		return this.setLayout(inflater, container, R.layout.fragment_google_calendar);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		if (requestCode == CALENDAR_PERMISSION_CALLBACK)
		{
			if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
				this.exportCalendar();
			else
				Dialog.error(this.getContext(), R.string.calendar_denied);
		}
	}

	private void exportCalendar()
	{
		if (this.task != null && this.task.getStatus() == AsyncTask.Status.RUNNING)
		{
			this.dialog.show();
		}
		else
		{
			this.task = provider().exportProcessor();
			this.dialog = new ProgressDialog(this.getContext());
			this.dialog.show();
			this.task.execute(this, this.manager.forCalendar(this.code));
		}
	}

	@Override
	public void onProgress(String status)
	{
		this.dialog.setMessage(status);
	}

	@Override
	public void onFailure(Throwable error)
	{
		this.dialog.dismiss();
		Dialog.msgbox(this.getContext(), error.getMessage());
	}

	@Override
	public void onSucces()
	{
		this.dialog.dismiss();
		Dialog.msgbox(this.getContext(), "Done!");
		// Communicate with other fragment to notify we're done!
	}
}

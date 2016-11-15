package be.dealloc.schedule.activities.fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.dealloc.schedule.R;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Fragment;

public class GoogleCalendarFragment extends Fragment
{
	public static final String BUNDLE_CALENDAR = "be.dealloc.schedule.activities.fragments.GoogleCalendarFragment.BUNDLE_CALENDAR";
	private static final int CALENDAR_PERMISSION_CALLBACK = 0x0001;

	private String code;

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
		Dialog.msgbox(this.getContext(), "This functionality is under development!");
	}
}

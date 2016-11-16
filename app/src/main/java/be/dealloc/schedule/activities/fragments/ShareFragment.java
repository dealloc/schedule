package be.dealloc.schedule.activities.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.system.Fragment;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import javax.inject.Inject;
import java.util.List;

public class ShareFragment extends Fragment
{
	@Inject CalendarManager calendarManager;
	@BindView(R.id.share_spCalendar) Spinner spinner;
	private List<Calendar> calendars;
	private String active;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
	{
		View view = this.setLayout(inflater, container, R.layout.fragment_share);

		this.calendars = this.calendarManager.getActiveCalendars();

		this.setupSpinner();

		return view;
	}

	private void setupSpinner()
	{
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getContext(), R.layout.support_simple_spinner_dropdown_item);
		for (Calendar calendar : this.calendars)
			adapter.add(calendar.getName());

		this.spinner.setAdapter(adapter);
	}

	@OnClick(R.id.share_btnGoogleCalendar)
	public void onExportToGoogleCalendarClicked()
	{
		Bundle bundle = new Bundle();
		bundle.putString(GoogleCalendarFragment.BUNDLE_CALENDAR, this.active);
		GoogleCalendarFragment fragment = new GoogleCalendarFragment();
		fragment.setArguments(bundle);
		this.getParentActivity().swap(R.id.activity_share, fragment);
	}

	@OnItemSelected(R.id.share_spCalendar)
	public void onCalendarSelected(int position)
	{
		this.active = this.calendars.get(position).getSecurityCode();
	}
}

package be.dealloc.schedule.activities.fragments;


import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Fragment;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import javax.inject.Inject;
import java.util.List;

public class ShareFragment extends Fragment implements CreateNdefMessageCallback
{
	@Inject CalendarManager calendarManager;
	@BindView(R.id.share_spCalendar) Spinner spinner;
	private List<Calendar> calendars;
	private Calendar active;

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
		bundle.putString(GoogleCalendarFragment.BUNDLE_CALENDAR, this.active.getSecurityCode());
		bundle.putString(GoogleCalendarFragment.BUNDLE_CALENDAR_NAME, this.active.getName());
		GoogleCalendarFragment fragment = new GoogleCalendarFragment();
		fragment.setArguments(bundle);
		this.getParentActivity().swap(R.id.activity_share, fragment);
	}

	@OnClick(R.id.share_btnNFC)
	public void onExportNFCClicked()
	{
		NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this.getContext());
		if (adapter == null)
		{
			Toast.makeText(this.getContext(), R.string.nfc_unavailable, Toast.LENGTH_SHORT).show();
			// TODO navigate away
		}
		else
		{
			if (adapter.isNdefPushEnabled())
			{
				Toast.makeText(this.getContext(), "Set callbacks!", Toast.LENGTH_SHORT).show();
				adapter.setNdefPushMessageCallback(this, this.getParentActivity());

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
					NfcAdapter.getDefaultAdapter(this.getContext()).invokeBeam(this.getParentActivity()); // TODO doesn't seem to work without it, but only available from API 21
				else
					adapter.setNdefPushMessage(this.createNdefMessage(null), this.getParentActivity());
			}
			else
			{
				Toast.makeText(this.getContext(), R.string.nfc_disabled, Toast.LENGTH_SHORT).show();
			}
		}
	}

	@OnClick(R.id.share_btnRename)
	public void onRenameClicked()
	{
		Dialog.input(this.getContext(), R.string.app_name, R.string.enter_name, (dialog, content) -> {
			if (content.trim().isEmpty())
			{
				Dialog.error(this.getContext(), R.string.name_required);
			}
			else
			{
				this.active.setName(content.trim());
				this.calendarManager.save(this.active);
				this.calendars = this.calendarManager.getActiveCalendars();
				this.setupSpinner();
				Dialog.msgbox(this.getContext(), R.string.app_name, R.string.calendar_saved);
			}
		}, null);
	}

	@OnItemSelected(R.id.share_spCalendar)
	public void onCalendarSelected(int position)
	{
		this.active = this.calendars.get(position);
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent nfcEvent)
	{
		String message = "Hello world!";

		return new NdefMessage(NdefRecord.createMime("text/plain", message.getBytes()));
	}

	@Override
	public void onResume()
	{
		super.onResume();
		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(this.getActivity().getIntent().getAction()))
		{
			Intent intent = this.getActivity().getIntent();
			Parcelable[] parcels = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES); // TODO check if array lookup is needed (there's getter for single instance)
			NdefMessage message = (NdefMessage) parcels[0];

			String code = new String(message.getRecords()[0].getPayload());

			Toast.makeText(this.getContext(), "Received message over NFC: " + code, Toast.LENGTH_SHORT).show();
		}
	}
}

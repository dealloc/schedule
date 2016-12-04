package be.dealloc.schedule.activities.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import be.dealloc.schedule.R;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.contracts.network.NfcService;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Fragment;
import be.dealloc.schedule.tasks.BasicTask;
import be.dealloc.schedule.tasks.UpdateColoursTask;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import javax.inject.Inject;
import java.util.List;

import static be.dealloc.schedule.system.Application.provider;

public class CalendarManagerFragment extends Fragment implements CreateNdefMessageCallback, BasicTask.TaskCallback
{
	@Inject CalendarManager calendarManager;
	@Inject NfcService nfcService;
	@BindView(R.id.share_spCalendar) Spinner spinner;
	private List<Calendar> calendars;
	private Calendar active;
	private UpdateColoursTask task;
	private ProgressDialog dialog;

	@Override
	public void onCreate(@Nullable Bundle bundle)
	{
		super.onCreate(bundle);
		this.dialog = new ProgressDialog(this.getContext());
		this.dialog.setCancelable(false);

		this.calendars = this.calendarManager.getActiveCalendars();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
	{
		View view = this.setLayout(inflater, container, R.layout.fragment_calendar_manager);

		this.setupSpinner();

		return view;
	}

	@Override
	public void onDetach()
	{
		super.onDetach();
		this.dialog.dismiss();
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if (this.dialog != null)
			this.dialog.show();
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
		}
		else
		{
			if (adapter.isNdefPushEnabled())
			{
				Toast.makeText(this.getContext(), R.string.nfc_sending, Toast.LENGTH_SHORT).show();
				adapter.setNdefPushMessageCallback(this, this.getParentActivity());

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
					NfcAdapter.getDefaultAdapter(this.getContext()).invokeBeam(this.getParentActivity());
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

	@OnClick(R.id.manage_pickColour)
	public void onPickColourClicked()
	{
		ColorPickerClickListener listener = (dialogInterface, selectedColour, integers) -> {
			if (this.task != null && this.task.getStatus() == AsyncTask.Status.RUNNING)
			{
				this.dialog.show();
			}
			else
			{
				task = provider().colourProcessor();
				dialog.show();
				task.execute(active, selectedColour, this);
			}
		};

		ColorPickerDialogBuilder.with(this.getContext())
				.setTitle(R.string.pick_colour)
				.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
				.density(12)
				.setPositiveButton(android.R.string.ok, listener)
				.build()
				.show();
	}

	@OnItemSelected(R.id.share_spCalendar)
	public void onCalendarSelected(int position)
	{
		this.active = this.calendars.get(position);
	}

	@Override
	public NdefMessage createNdefMessage(NfcEvent nfcEvent)
	{
		String message = this.nfcService.encode(this.active);

		return new NdefMessage(
				NdefRecord.createMime("application/be.dealloc.schedule", message.getBytes())
		);
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
		Dialog.msgbox(this.getContext(), R.string.app_name, R.string.update_complete);
	}
}

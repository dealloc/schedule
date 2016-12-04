package be.dealloc.schedule.activities;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;
import be.dealloc.schedule.R;
import be.dealloc.schedule.activities.fragments.RegistrationFragment;
import be.dealloc.schedule.activities.fragments.UpdateCalendarFragment;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.calendars.CalendarManager;
import be.dealloc.schedule.contracts.network.NfcService;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Fragment;

import javax.inject.Inject;

public class RegistrationActivity extends Activity implements RegistrationFragment.RegistrationHost, UpdateCalendarFragment.CalendarUpdateCallback
{
	public static final String SECURITYCODE_INTENT = "be.dealloc.schedule.activities.RegistrationActivity.SECURITYCODE_INTENT";
	public static final String CALENDARNAME_INTENT = "be.dealloc.schedule.activities.RegistrationActivity.CALENDARNAME_INTENT";
	private static final String FRAGMENT_KEY = "be.dealloc.schedule.activities.RegistrationActivity.FRAGMENT_KEY";
	private static final String PROCESSING_KEY = "be.dealloc.schedule.activities.RegistrationActivity.PROCESSING_KEY";

	protected Fragment current;
	@Inject CalendarManager calendarManager;
	@Inject NfcService nfcService;
	private String processingCode;

	@Override
	protected void onCreate(Bundle bundle)
	{
		super.onCreate(bundle);
		this.setLayout(R.layout.activity_registration);

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(this.getIntent().getAction()))
		{
			Intent intent = this.getIntent();
			Parcelable[] parcels = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage message = (NdefMessage) parcels[0];

			String payload = new String(message.getRecords()[0].getPayload());
			NfcService.NfcResult parsed = this.nfcService.decode(payload);

			this.createCalendar(parsed.getCode(), parsed.getName(), true);
		}
		else if (this.getIntent().getExtras() == null)
		{
			RegistrationFragment fragment = new RegistrationFragment();
			fragment.setHost(this);
			this.swap(fragment);
		}
		else
		{
			String name = this.getIntent().getExtras().getString(CALENDARNAME_INTENT);
			String code = this.getIntent().getExtras().getString(SECURITYCODE_INTENT);

			this.createCalendar(code, name, true);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle bundle)
	{
		super.onSaveInstanceState(bundle);
		this.getSupportFragmentManager()
				.putFragment(bundle, FRAGMENT_KEY, this.current);
		if (current instanceof RegistrationFragment)
			((RegistrationFragment) this.current).setHost(null); // No dangling references to destroyed activity

		bundle.putString(PROCESSING_KEY, this.processingCode);
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle)
	{
		super.onRestoreInstanceState(bundle);
		this.processingCode = bundle.getString(PROCESSING_KEY);
		Fragment fragment = (Fragment) this.getSupportFragmentManager().getFragment(bundle, FRAGMENT_KEY);
		if (fragment != null)
		{
			this.swap(fragment);
			if (fragment instanceof RegistrationFragment)
				((RegistrationFragment) fragment).setHost(this);
		}
	}

	public synchronized void swap(Fragment fragment)
	{
		if (this.current != null)
			this.current.setRetainInstance(false);

		this.current = fragment;
		this.current.setRetainInstance(true);

		super.swap(R.id.registration_layout, fragment);
	}

	@Override
	public void startDesiderius()
	{
		this.navigate(DesideriusActivity.class);
	}

	@Override
	public void initCalendar(String code)
	{
		this.createCalendar(code, "EHB", false);
	}

	@Override
	public void onFailure(Throwable error)
	{
		Toast.makeText(this, "Shit broke: " + error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onSuccess()
	{
		Calendar calendar = this.calendarManager.findBySecurityCode(this.processingCode);
		calendar.setActive(true);
		this.calendarManager.save(calendar);
		this.navigate(MainActivity.class);
	}

	private void createCalendar(String code, String name, boolean active)
	{
		Calendar calendar = this.calendarManager.create();
		calendar.setName(name);
		calendar.setSecurityCode(code);
		calendar.setActive(active);
		this.calendarManager.save(calendar);

		this.processingCode = code;
		Bundle bundle = new Bundle();
		bundle.putString(UpdateCalendarFragment.SECURITY_CODE, code);
		UpdateCalendarFragment fragment = new UpdateCalendarFragment();
		fragment.setCallback(this);
		fragment.setArguments(bundle);
		this.swap(fragment);
	}
}

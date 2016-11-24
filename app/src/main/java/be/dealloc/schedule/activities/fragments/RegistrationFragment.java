package be.dealloc.schedule.activities.fragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.dealloc.schedule.R;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Fragment;
import butterknife.OnClick;

public class RegistrationFragment extends Fragment
{
	private RegistrationHost host;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
	{
		return this.setLayout(inflater, container, R.layout.fragment_registration);
	}

	@OnClick(R.id.registration_btnDesiderius)
	public void onDesideriusClicked()
	{
		if (this.host != null)
			this.host.startDesiderius();
	}

	@OnClick(R.id.registration_btnEnterCode)
	public void onRegistrationCodeClicked()
	{
		if (this.host != null)
		{
			Dialog.input(this.getContext(), R.string.app_name, R.string.enter_security_code, new Dialog.TextDialogCallback()
			{
				@Override
				public void confirm(DialogInterface dialog, String content)
				{
					host.initCalendar(content);
				}
			}, null);
		}
	}

	public void setHost(RegistrationHost host)
	{
		this.host = host;
	}

	public interface RegistrationHost
	{
		void startDesiderius();

		void initCalendar(String code);
	}
}

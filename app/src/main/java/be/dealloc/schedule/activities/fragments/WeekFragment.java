package be.dealloc.schedule.activities.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.dealloc.schedule.R;
import be.dealloc.schedule.system.Fragment;

public class WeekFragment extends Fragment
{
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		return this.setLayout(inflater, container, R.layout.fragment_week);
	}
}

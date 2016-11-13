package be.dealloc.schedule.activities.fragments;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import be.dealloc.schedule.R;
import be.dealloc.schedule.adapters.CourseAdapter;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.system.Application;
import be.dealloc.schedule.system.Fragment;
import butterknife.BindView;

import javax.inject.Inject;

public class ListFragment extends Fragment
{
	@BindView(R.id.list_recycler) RecyclerView recyclerView;
	@Inject CourseManager manager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle)
	{
		View view = this.setLayout(inflater, container, R.layout.fragment_list);

		CourseAdapter adapter = new CourseAdapter(this.manager.getUpcoming());
		RecyclerView.LayoutManager manager = new LinearLayoutManager(Application.provider().context());
		this.recyclerView.setLayoutManager(manager);
		this.recyclerView.setItemAnimator(new DefaultItemAnimator());
		this.recyclerView.setAdapter(adapter);

		return view;
	}
}

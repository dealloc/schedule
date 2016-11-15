package be.dealloc.schedule.system;
// Created by dealloc. All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class Fragment extends android.support.v4.app.Fragment
{
	private boolean injected = false;
	private Unbinder unbinder;

	@Override
	public void onCreate(@Nullable Bundle bundle)
	{
		super.onCreate(bundle);
		this.inject();
	}

	protected View setLayout(LayoutInflater inflater, ViewGroup group, final int layout)
	{
		View view = inflater.inflate(layout, group, false);

		this.unbinder = ButterKnife.bind(this, view);

		return view;
	}

	private synchronized void inject()
	{
		if (!this.injected)
		{
			Application.inject(this);
			this.injected = true;
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroyView();
		this.unbinder.unbind();
	}
}

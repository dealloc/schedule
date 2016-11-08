package be.dealloc.schedule.system;
// Created by dealloc. All rights reserved.

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import butterknife.ButterKnife;

public class Activity extends AppCompatActivity
{
	@Override
	public void onCreate(Bundle saved, PersistableBundle persisted)
	{
		super.onCreate(saved, persisted);
		Application.inject(this);
	}

	@Override
	protected void onCreate(Bundle saved)
	{
		super.onCreate(saved);
		Application.inject(this);
	}

	protected void setLayout(final int layout)
	{
		this.setContentView(layout);
		ButterKnife.bind(this);
	}
}

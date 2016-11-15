package be.dealloc.schedule.activities;

import android.os.Bundle;
import be.dealloc.schedule.R;
import be.dealloc.schedule.activities.fragments.ShareFragment;
import be.dealloc.schedule.system.Activity;

public class ShareActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_share);

		this.swap(R.id.activity_share, new ShareFragment());
	}
}

package be.dealloc.schedule.facades;
// Created by dealloc. All rights reserved.

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import be.dealloc.schedule.R;

/**
 * Dialog class.
 *
 * @author dealloc
 */
public final class Dialog
{
	private Dialog() { }

	public static AlertDialog confirm(Context context, final int title, final int content, DialogInterface.OnClickListener onConfirm, DialogInterface.OnClickListener onDeny)
	{
		return (new AlertDialog.Builder(context))
				.setTitle(title)
				.setMessage(content)
				.setIcon(R.mipmap.ic_launcher)
				.setPositiveButton(android.R.string.yes, onConfirm)
				.setNegativeButton(android.R.string.no, onDeny)
				.create();
	}
}

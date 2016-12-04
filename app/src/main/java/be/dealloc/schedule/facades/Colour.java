package be.dealloc.schedule.facades;
// Created by dealloc. All rights reserved.

import android.graphics.Color;

/**
 * Colour class.
 *
 * @author dealloc
 */
public final class Colour
{
	public static int inverse(int colour)
	{
		float[] hsv = new float[3];
		Color.colorToHSV(colour, hsv);
		hsv[0] = (hsv[0] + 180) % 360;

		return Color.HSVToColor(hsv);
	}

	public static int darken(int colour)
	{
		float[] hsv = new float[3];
		Color.colorToHSV(colour, hsv);

		hsv[2] = 0.7f;

		return Color.HSVToColor(hsv);
	}
}

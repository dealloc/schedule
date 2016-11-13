package be.dealloc.schedule.system;
// Created by dealloc. All rights reserved.

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Process;
import android.support.v4.content.ContextCompat;
import be.dealloc.schedule.activities.MainActivity;
import be.dealloc.schedule.contracts.DaggerServiceProvider;
import be.dealloc.schedule.contracts.ServiceProvider;
import be.dealloc.schedule.providers.EntityProvider;
import be.dealloc.schedule.providers.NetworkProvider;
import be.dealloc.schedule.providers.SystemProvider;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Application extends android.app.Application
{
	private static Application instance;
	private static ServiceProvider provider;
	private static Map<Class<?>, Method> injectors;

	@Override
	public void onCreate()
	{
		super.onCreate();
		Application.instance = this;

		Logger.init("SCHEDULE-LOG");

		Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
			Logger.e(throwable, "Thread %s has encountered a fatal exception.", thread.getName());
			Application.exit();
		});

		this.initServiceProvider();
	}

	public static String string(final int resource)
	{
		return Application.instance.getApplicationContext().getString(resource);
	}

	public static ServiceProvider provider()
	{
		return Application.provider;
	}

	private void initServiceProvider()
	{
		Application.provider = DaggerServiceProvider.builder()
				.systemProvider(new SystemProvider(this))
				.entityProvider(new EntityProvider(getApplicationContext()))
				.networkProvider(new NetworkProvider())
				.build();

		Application.injectors = new HashMap<>();
		for (Method method : ServiceProvider.class.getDeclaredMethods())
		{
			if (method.getName().equals("inject") && method.getParameterTypes().length == 1)
			{
				Class<?> type = method.getParameterTypes()[0]; // Get the type this method injects on.
				Application.injectors.put(type, method);
			}
		}
		Logger.i("Initialized injector cache with %d methods.", injectors.size());
	}

	public static <T> void inject(T activity)
	{
		Class<?> type = activity.getClass();

		if (Application.injectors.containsKey(type))
		{
			try
			{
				Method injector = Application.injectors.get(type);

				injector.invoke(Application.provider, activity);

				Logger.i("Invoked injector for %s", type.getSimpleName());
			}
			catch (Exception error)
			{
				// Failed injection will most likely cause a fatal crash later on.
				Logger.e(error, "Injection failed for %s", type.getName());
			}
		}
		else
		{
			Logger.w("Attempted to invoke injector for %s, but no injectors were registered.", type.getSimpleName());
		}
	}

	public static int color(final int color)
	{
		return ContextCompat.getColor(Application.instance, color);
	}

	public static void restart()
	{
		Intent intent = new Intent(instance.getApplicationContext(), MainActivity.class);
		PendingIntent future = PendingIntent.getActivity(instance.getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager manager = (AlarmManager) instance.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
		manager.set(AlarmManager.RTC, System.currentTimeMillis() + 100, future);
		Application.exit();
	}

	private static void exit()
	{
		Process.killProcess(Process.myPid());
	}
}

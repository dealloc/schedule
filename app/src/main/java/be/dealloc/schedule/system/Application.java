package be.dealloc.schedule.system;
// Created by dealloc. All rights reserved.

import android.os.Process;
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
			Process.killProcess(Process.myPid());
		});

		this.initServiceProvider();
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

	public static <T extends Activity> void inject(T activity)
	{
		Class<? extends Activity> type = activity.getClass();

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

	public static String string(final int resource)
	{
		return Application.instance.getApplicationContext().getString(resource);
	}
}

package be.dealloc.schedule.system;
// Created by dealloc. All rights reserved.

import be.dealloc.schedule.contracts.DaggerServiceProvider;
import be.dealloc.schedule.contracts.ServiceProvider;
import be.dealloc.schedule.providers.EntityProvider;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Application extends android.app.Application
{
	private static ServiceProvider provider;
	private static Map<Class<?>, Method> injectors;

	@Override
	public void onCreate()
	{
		super.onCreate();

		Logger.init("SCHEDULE-LOG");

		this.initServiceProvider();
	}

	private void initServiceProvider()
	{
		Application.provider = DaggerServiceProvider.builder()
				.entityProvider(new EntityProvider(this.getApplicationContext()))
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
			}
			catch (Exception error)
			{
				// Failed injection will most likely cause a fatal crash later on.
				Logger.e(error, "Injection failed for %s", type.getName());
			}
		}
	}
}

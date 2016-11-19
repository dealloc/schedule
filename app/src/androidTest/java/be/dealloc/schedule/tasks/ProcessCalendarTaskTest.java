package be.dealloc.schedule.tasks;
// Created by dealloc. All rights reserved.

import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import be.dealloc.schedule.contracts.entities.calendars.Calendar;
import be.dealloc.schedule.contracts.entities.courses.CourseManager;
import be.dealloc.schedule.contracts.network.NetworkService;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(AndroidJUnit4.class)
public class ProcessCalendarTaskTest
{
	@Rule
	public UiThreadTestRule uiThreadTestRule = new UiThreadTestRule();

	private BasicTask.TaskCallback callback;
	private NetworkService networkService;
	private CourseManager courseManager;
	private Calendar calendar;

	private final String VALID_URL = "https://example.com";
	private final String VALID_ICAL = "BEGIN:VCALENDAR\n" +
			"VERSION:2.0\n" +
			"BEGIN:VEVENT\n" +
			"UID:2601f87f-c255-4640-8dc8-b00b2c9b30d6\n" +
			"DTSTAMP:20161119T162649Z\n" +
			"DTSTART:20161119T172500Z\n" +
			"DTEND:20161119T182500Z\n" +
			"SUMMARY:Test event\n" +
			"LOCATION:Testlocation\n" +
			"DESCRIPTION:This is a test event!\n" +
			"END:VEVENT\n" +
			"END:VCALENDAR";
	private final String INVALID_ICAL = "<html><head></head><body></body></html>"; // The web service returns empty responses on errors.

	@Before
	public void setUp() throws Exception
	{
		this.networkService = mock(NetworkService.class);
		this.courseManager = mock(CourseManager.class);
		this.calendar = mock(Calendar.class);
		this.callback = mock(BasicTask.TaskCallback.class);
	}

	@Test
	public void withValidICalendar() throws Throwable
	{
		final CountDownLatch signal = new CountDownLatch(1);

		when(calendar.getURl()).thenReturn(VALID_URL);

		doAnswer(invocation -> {
			fail("onFailure should not have been called!");
			return null;
		}).when(this.callback).onFailure(any());

		doAnswer(invocation -> {
			signal.countDown();
			return null;
		}).when(this.callback).onSucces();

		doAnswer(invocation -> {
			assertEquals(VALID_URL, invocation.getArguments()[0]);
			((NetworkService.NetworkCallback) invocation.getArguments()[1]).onSucces(200, VALID_ICAL);
			return null;
		}).when(this.networkService).download(anyString(), any(NetworkService.NetworkCallback.class));

		this.uiThreadTestRule.runOnUiThread(() -> {
			ProcessCalendarTask task = new ProcessCalendarTask(this.networkService, this.courseManager);
			task.setCallback(this.callback);
			task.execute(this.calendar);
		});

		Assert.assertTrue("Test did not finish within 10 seconds.", signal.await(10, TimeUnit.SECONDS));
	}

	@Test
	public void withNetworkFailure() throws Throwable
	{
		final CountDownLatch signal = new CountDownLatch(1);
		Throwable exception = mock(Throwable.class);

		doAnswer(invocation -> {
			((NetworkService.NetworkCallback) invocation.getArguments()[1]).onFailure(exception);
			return null;
		}).when(this.networkService).download(anyString(), any(NetworkService.NetworkCallback.class));

		doAnswer(invocation -> {
			signal.countDown();
			return null;
		}).when(this.callback).onFailure(exception);

		this.uiThreadTestRule.runOnUiThread(() -> {
			ProcessCalendarTask task = new ProcessCalendarTask(this.networkService, this.courseManager);
			task.setCallback(this.callback);
			task.execute(this.calendar);
		});

		Assert.assertTrue("Test did not finish within 10 seconds.", signal.await(10, TimeUnit.SECONDS));
	}

	@Test
	public void withInvalidIcalResponse() throws Throwable
	{
		final CountDownLatch signal = new CountDownLatch(1);

		when(calendar.getURl()).thenReturn(VALID_URL);

		doAnswer(invocation -> {
			assertEquals(((Throwable) invocation.getArguments()[0]).getMessage(), "Invalid ical response returned.");
			signal.countDown();
			return null;
		}).when(this.callback).onFailure(any());

		doAnswer(invocation -> {
			fail("onSuccess should have failed!");
			return null;
		}).when(this.callback).onSucces();

		doAnswer(invocation -> {
			assertEquals(VALID_URL, invocation.getArguments()[0]);
			((NetworkService.NetworkCallback) invocation.getArguments()[1]).onSucces(200, INVALID_ICAL);
			return null;
		}).when(this.networkService).download(anyString(), any(NetworkService.NetworkCallback.class));

		this.uiThreadTestRule.runOnUiThread(() -> {
			ProcessCalendarTask task = new ProcessCalendarTask(this.networkService, this.courseManager);
			task.setCallback(this.callback);
			task.execute(this.calendar);
		});

		Assert.assertTrue("Test did not finish within 10 seconds.", signal.await(10, TimeUnit.SECONDS));
	}
}
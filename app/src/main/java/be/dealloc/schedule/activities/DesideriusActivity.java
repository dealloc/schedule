package be.dealloc.schedule.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.webkit.*;
import android.widget.Button;
import android.widget.EditText;
import be.dealloc.schedule.R;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;
import be.dealloc.schedule.system.Application;
import butterknife.BindView;
import butterknife.OnClick;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DesideriusActivity extends Activity
{
	private static final String CAS_URL = "https://cas.ehb.be/login?service=https%3A%2F%2Fdesiderius.ehb.be%2F";
	private static final String CALENDAR_URL = "https://desiderius.ehb.be/index.php?application=Chamilo%5CApplication%5CCalendar&go=ICal";

	@BindView(R.id.desiderius_btnLogin) Button btnLogin;
	@BindView(R.id.desiderius_txtEmail) EditText txtEmail;
	@BindView(R.id.desiderius_txtPassword) EditText txtPassword;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) // TODO buttons should not enable themselves when orientation changes etc.
	{
		super.onCreate(savedInstanceState);
		this.setLayout(R.layout.activity_desiderius);
	}

	@OnClick(R.id.desiderius_btnLogin)
	public void onLoginClicked()
	{
		if (txtEmail.getText().toString().isEmpty() || txtPassword.getText().toString().isEmpty()) // Skip empty fields
			return;

		this.btnLogin.setEnabled(false);
		this.progressDialog = ProgressDialog.show(this, Application.string(R.string.app_name), Application.string(R.string.desiderius_connecting));
		this.killWebJunk();
		WebView view = new WebView(Application.provider().context());
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new DesideriusClient());
		view.loadUrl(CAS_URL);
	}

	private void loginFailed()
	{
		this.killWebJunk();
		this.progressDialog.dismiss();
		Dialog.error(this, R.string.invalid_credentials);
		this.btnLogin.setEnabled(true);
	}

	private void extractCode(String html)
	{
		Matcher matcher = Pattern.compile("[a-z0-9]{40}").matcher(html);
		if (matcher.find())
		{
			this.killWebJunk();
			String securityCode = matcher.group();

			Intent intention = new Intent(this, RegistrationActivity.class);
			intention.putExtra(RegistrationActivity.SECURITYCODE_INTENT, securityCode);
			intention.putExtra(RegistrationActivity.CALENDARNAME_INTENT, this.txtEmail.getText().toString());
			startActivity(intention);
			this.finish();
		}
		else
		{
			this.loginFailed();
		}
	}

	// Kill all cache and junk created by using webview
	private void killWebJunk()
	{
		Logger.i("Killing all webview cache.");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
		{
			CookieManager.getInstance().removeAllCookies(null);
			CookieManager.getInstance().removeSessionCookies(null);
		}
		else
		{
			CookieSyncManager.createInstance(Application.provider().context());
			CookieManager.getInstance().removeAllCookie();
			CookieManager.getInstance().removeSessionCookie();
		}

		Application.provider().context().deleteDatabase("webview.db");
		Application.provider().context().deleteDatabase("webviewCookiesChromium.db.db");
	}

	private class DesideriusClient extends WebViewClient
	{
		private int count = 0; // If login page loads 3x we have invalid login!
		private boolean failed = false;

		@Override
		@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
		public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
		{
			String url = request.getUrl().toString();
			return this.extractCodeFromUrl(url);
		}

		@Override
		@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			return this.extractCodeFromUrl(url);
		}

		@Override
		public void onPageFinished(WebView view, String url)
		{
			if (this.failed)
				return;

			super.onPageFinished(view, url);
			if (url.equals(CAS_URL))
			{
				if (++count == 3)
				{
					Logger.w("Login page was loaded 3x! Invalid credentials assumed.");
					loginFailed();
				}
				else
				{
					progressDialog.setMessage(Application.string(R.string.desiderius_authenticating));
					Logger.i("Injecting credentials for %s into CAS", txtEmail.getText().toString());
					String username = "document.getElementById('username').value = '" + txtEmail.getText().toString() + "'";
					String password = "document.getElementById('password').value = '" + txtPassword.getText().toString() + "'";
					String submit = "document.querySelector('button[type=submit]').click()";
					view.loadUrl(String.format(Locale.ENGLISH, "javascript:%s;%s;%s", username, password, submit));
				}
			}
			else if (!url.equals(CALENDAR_URL))
			{
				Logger.i("Switching to calendar view...");
				progressDialog.setMessage(Application.string(R.string.desiderius_calendar));
				view.loadUrl(CALENDAR_URL);
			}
			else if (url.equals(CALENDAR_URL))
			{
				Logger.i("Executing source extraction");
				progressDialog.setMessage(Application.string(R.string.desiderius_extracting));
				view.loadUrl("javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
			}
		}

		@Override
		@TargetApi(Build.VERSION_CODES.N)
		public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
		{
			super.onReceivedError(view, request, error);
			this.handleError(request.getUrl().toString(), error.getDescription().toString());
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
		{
			super.onReceivedError(view, errorCode, description, failingUrl);
			this.handleError(failingUrl, description);
		}

		private boolean extractCodeFromUrl(String url)
		{
			if (url.startsWith("source://"))
			{
				progressDialog.dismiss();
				try
				{
					String html = URLDecoder.decode(url, "UTF-8").substring(9);
					extractCode(html);
				}
				catch (UnsupportedEncodingException ignored)
				{
					Dialog.error(DesideriusActivity.this, R.string.generic_web_error);
					Logger.e(ignored, "Failed to decode HTML");
				}
				return true;
			}

			return false;
		}

		private void handleError(String url, String error)
		{
			this.failed = true;
			progressDialog.dismiss();

			if (error.contains("Couldn't find the URL"))
				Dialog.error(DesideriusActivity.this, R.string.internet_failure);
			else if (error.contains("The connection to the server timed out."))
				Dialog.error(DesideriusActivity.this, R.string.internet_timedout);
			else
				Dialog.error(DesideriusActivity.this, R.string.generic_web_error);

			btnLogin.setEnabled(true);
			Logger.e("Failed to load %s due to error %s", url, error);
		}
	}
}

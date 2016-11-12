package be.dealloc.schedule.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
	protected void onCreate(Bundle savedInstanceState)
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
		WebView view = new WebView(Application.provider().context());
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new DesideriusClient());
		view.loadUrl(CAS_URL); // TODO check if internet fails or something
	}

	private void loginFailed()
	{
		this.progressDialog.dismiss();
		Dialog.msgbox(this, R.string.app_name, R.string.invalid_credentials).show();
		this.btnLogin.setEnabled(true);
	}

	private void extractCode(String html)
	{
		Matcher matcher = Pattern.compile("[a-z0-9]{40}").matcher(html);
		if (matcher.find())
		{
			String securityCode = matcher.group();

			Intent intention = new Intent(this, RegistrationActivity.class);
			intention.putExtra(RegistrationActivity.SECURITYCODE_INTENT, securityCode);
			startActivity(intention);
			this.finish();
		}
		else
		{
			this.loginFailed();
		}
	}

	private class DesideriusClient extends WebViewClient
	{
		private int count = 0; // If login page loads 3x we have invalid login!

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
					Logger.e(ignored, "Failed to decode HTML");
				}
				return true;
			}

			return false;
		}
	}
}

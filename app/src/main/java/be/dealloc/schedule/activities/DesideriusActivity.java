package be.dealloc.schedule.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ViewFlipper;
import be.dealloc.schedule.R;
import be.dealloc.schedule.facades.Dialog;
import be.dealloc.schedule.system.Activity;
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

	@BindView(R.id.desiderius_flipper) ViewFlipper flipper;
	@BindView(R.id.desiderius_webview) WebView view;
	@BindView(R.id.desiderius_txtEmail) EditText txtEmail;
	@BindView(R.id.desiderius_txtPassword) EditText txtPassword;

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

		flipper.showNext();
		view.getSettings().setJavaScriptEnabled(true);
		view.setWebViewClient(new DesideriusClient());
		view.loadUrl(CAS_URL); // TODO check if internet fails or something
	}

	private void loginFailed()
	{
		this.flipper.showNext();
		Dialog.msgbox(this, R.string.app_name, R.string.invalid_credentials).show();
	}

	private void extractCode(String html)
	{
		Matcher matcher = Pattern.compile("[a-z0-9]{40}").matcher(html);
		if (matcher.find())
		{
			String securityCode = matcher.group();
			this.flipper.showNext();

			Intent intention = new Intent(this, RegistrationActivity.class);
			intention.putExtra(RegistrationActivity.SECURITYCODE_INTENT, securityCode);
			startActivity(intention);
			this.finish();
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
			Logger.i("Loaded %s", url);
			if (url.equals(CAS_URL))
			{
				if (++count == 3)
				{
					loginFailed();
				}
				else
				{
					String username = "document.getElementById('username').value = '" + txtEmail.getText().toString() + "'";
					String password = "document.getElementById('password').value = '" + txtPassword.getText().toString() + "'";
					String submit = "document.querySelector('button[type=submit]').click()";
					view.loadUrl(String.format(Locale.ENGLISH, "javascript:%s;%s;%s", username, password, submit));
				}
			}
			else if (!url.equals(CALENDAR_URL))
			{
				view.loadUrl(CALENDAR_URL);
			}
			else if (url.equals(CALENDAR_URL))
			{
				view.loadUrl("javascript:this.document.location.href = 'source://' + encodeURI(document.documentElement.outerHTML);");
			}
		}

		private boolean extractCodeFromUrl(String url)
		{
			if (url.startsWith("source://"))
			{
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

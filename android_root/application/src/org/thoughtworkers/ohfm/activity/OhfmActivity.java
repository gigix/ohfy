package org.thoughtworkers.ohfm.activity;

import org.thoughtworkers.ohfm.R;
import org.thoughtworkers.ohfm.domain.Server;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OhfmActivity extends Activity implements OnClickListener {
	private static final String PREFERENCE_PASSWORD = "password";
	private static final String PREFERENCE_EMAIL = "email";
	protected static final int MSG_LOGIN_FAIL = 0;
	protected static final int MSG_LOGIN_SUCCESS = 1;

	private Server server;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_LOGIN_SUCCESS) {
				String signInToken = (String) msg.obj;
				Intent intent = new Intent(OhfmActivity.this, TodayActivity.class);
				intent.putExtra(Server.SIGN_IN_TOKEN_NAME, signInToken);

				startActivity(intent);
				return;
			}

			if (msg.what == MSG_LOGIN_FAIL) {
				Toast.makeText(OhfmActivity.this, "Sign in failed. Please check your email and password.",
						Toast.LENGTH_LONG).show();
				return;
			}
		};
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.sign_in).setOnClickListener(this);

		server = Server.create(this);

		SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		((EditText) findViewById(R.id.email)).setText(preferences.getString(PREFERENCE_EMAIL, ""));
		((EditText) findViewById(R.id.password)).setText(preferences.getString(PREFERENCE_PASSWORD, ""));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_in:
			new AsyncJob(this) {
				@Override
				protected void job() {
					signIn();
				}
			}.start();
			break;
		default:
			throw new RuntimeException("Unexpected click event to " + v.getId());
		}
	}

	private void signIn() {
		String email = ((TextView) findViewById(R.id.email)).getText().toString();
		String password = ((TextView) findViewById(R.id.password)).getText().toString();
		String signInToken = server.signIn(email, password);

		if (signInToken != null) {
			Message msg = handler.obtainMessage();
			msg.what = MSG_LOGIN_SUCCESS;
			msg.obj = signInToken;
			handler.sendMessage(msg);

			Editor preferences = getPreferences(MODE_PRIVATE).edit();
			preferences.putString(PREFERENCE_EMAIL, email).putString(PREFERENCE_PASSWORD, password).commit();

			return;
		}

		handler.sendEmptyMessage(MSG_LOGIN_FAIL);
	}
}
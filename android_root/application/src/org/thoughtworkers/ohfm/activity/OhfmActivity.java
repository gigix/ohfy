package org.thoughtworkers.ohfm.activity;

import org.thoughtworkers.ohfm.R;
import org.thoughtworkers.ohfm.control.AsyncJob;
import org.thoughtworkers.ohfm.domain.Credential;
import org.thoughtworkers.ohfm.domain.Server;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class OhfmActivity extends Activity implements OnClickListener {
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
		
		TextView introText = (TextView) findViewById(R.id.app_intro);
		Linkify.addLinks(introText, Linkify.WEB_URLS);
		introText.setMovementMethod(LinkMovementMethod.getInstance());

		findViewById(R.id.sign_in).setOnClickListener(this);

		server = Server.create(this);

		Credential credential = Credential.load(this);
		((EditText) findViewById(R.id.email)).setText(credential.getEmail());
		((EditText) findViewById(R.id.password)).setText(credential.getPassword());
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

			new Credential(email, password).save(this);

			return;
		}

		handler.sendEmptyMessage(MSG_LOGIN_FAIL);
	}
}
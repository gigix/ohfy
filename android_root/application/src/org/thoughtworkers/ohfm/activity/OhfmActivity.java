package org.thoughtworkers.ohfm.activity;

import org.thoughtworkers.ohfm.R;
import org.thoughtworkers.ohfm.domain.Server;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class OhfmActivity extends Activity implements OnClickListener {
	private Server server;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		findViewById(R.id.sign_in).setOnClickListener(this);

		server = Server.create();
		setTitle(getString(R.string.server_host));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_in:
			try {
				signIn();
			} catch(Exception e) {
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			break;
		default:
			throw new RuntimeException("Unexpected click event to " + v.getId());
		}
	}

	private void signIn() {
		String email = ((TextView) findViewById(R.id.email)).getText().toString();
		String password = ((TextView) findViewById(R.id.password)).getText().toString();
		String signInToken = server.signIn(email, password);
		
		if(signInToken != null) {
			startActivity(new Intent(this, TodayActivity.class));
			return;
		}
		
		Toast.makeText(this, "Sign in failed. Please check your email and password.", Toast.LENGTH_LONG).show();
	}
}
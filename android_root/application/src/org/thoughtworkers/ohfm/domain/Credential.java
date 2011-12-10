package org.thoughtworkers.ohfm.domain;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Credential {
	public static final String CREDENTIAL_PREFERENCES = "credential";
	public static final String PREFERENCE_PASSWORD = "password";
	public static final String PREFERENCE_EMAIL = "email";

	private final String email;
	private final String password;

	public void save(Context context) {
		Editor preferences = context.getSharedPreferences(CREDENTIAL_PREFERENCES, Context.MODE_PRIVATE).edit();
		preferences.putString(PREFERENCE_EMAIL, getEmail()).putString(PREFERENCE_PASSWORD, getPassword()).commit();
	}

	public Credential(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public static void clear(Context context) {
		context.getSharedPreferences(CREDENTIAL_PREFERENCES, Context.MODE_PRIVATE).edit().clear().commit();
	}

	public static Credential load(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(CREDENTIAL_PREFERENCES, Context.MODE_PRIVATE);
		String email = preferences.getString(PREFERENCE_EMAIL, "");
		String password = preferences.getString(PREFERENCE_PASSWORD, "");
		return new Credential(email, password);
	}
}

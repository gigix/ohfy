package org.thoughtworkers.ohfm.domain;

import java.util.List;

import org.thoughtworkers.ohfm.R;

import android.content.Context;

public abstract class Server {
	private static Server instance;

	public static void setInstance(Server instance) {
		Server.instance = instance;
	}

	public static Server create(String serverHost) {
		if(instance != null) {
			return instance;
		}
		return new ServerImpl(serverHost);
	}

	public static final String SIGN_IN_TOKEN = "signInToken";

	public abstract String signIn(String email, String password);

	public static Server create(Context context) {
		return create(context.getString(R.string.server_host));
	}

	public abstract List<TodoItem> fetchTodoItems(String signInToken);
}

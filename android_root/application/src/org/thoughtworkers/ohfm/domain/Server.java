package org.thoughtworkers.ohfm.domain;

public abstract class Server {
	private static Server instance;

	public static void setInstance(Server instance) {
		Server.instance = instance;
	}

	public static Server create() {
		if(instance != null) {
			return instance;
		}
		return new ServerImpl();
	}

	public abstract String signIn(String email, String password);
}

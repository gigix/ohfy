package org.thoughtworkers.ohfm.domain;

import java.util.HashMap;
import java.util.Map;

public class ServerStub extends Server {

	@Override
	public String signIn(String email, String password) {
		@SuppressWarnings("serial")
		Map<String, String> validLogins = new HashMap<String, String>() {
			{
				put("user@test.com", "password");
			}
		};
		
		if(validLogins.containsKey(email) && validLogins.get(email).equals(password)) {
			return email;
		}
		return null;
	}

}

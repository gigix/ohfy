package org.thoughtworkers.ohfm.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	@SuppressWarnings("serial")
	@Override
	public List<TodoItem> fetchTodoItems(String signInToken) {
		return new ArrayList<TodoItem>() {
			{
				add(new TodoItem("学Android开发", true));
				add(new TodoItem("游泳", false));
			}
		};
	}

}

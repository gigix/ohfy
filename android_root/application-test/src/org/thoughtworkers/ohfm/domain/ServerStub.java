package org.thoughtworkers.ohfm.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerStub extends Server {
	@SuppressWarnings("serial")
	private static final List<TodoItem> todoItems = new ArrayList<TodoItem>() {
		{
			add(new TodoItem("学Android开发", true, 1, 101));
			add(new TodoItem("游泳", false, 1, 102));
		}
	};
	
	private static final List<TodoItem> mutableTodoItems = new ArrayList<TodoItem>();

	@Override
	public String signIn(String email, String password) {
		@SuppressWarnings("serial")
		Map<String, String> validLogins = new HashMap<String, String>() {
			{
				put("user@test.com", "password");
				put("empty_user@test.com", "password");
			}
		};
		
		if(validLogins.containsKey(email) && validLogins.get(email).equals(password)) {
			return email;
		}
		return null;
	}

	@Override
	public List<TodoItem> fetchTodoItems(String signInToken) {
		@SuppressWarnings("serial")
		Map<String, List<TodoItem>> todoItemsMap = new HashMap<String, List<TodoItem>>() {
			{
				put("user@test.com", todoItems);
				put("empty_user@test.com", mutableTodoItems);
			}
		};
		return todoItemsMap.get(signInToken);
	}

	@Override
	public void updateStatus(TodoItem todoItem, String signInToken) {
	}

	@Override
	public void signOut() {
	}

	@Override
	public void createNewPlan(List<TodoItem> todoItems, String signInToken) {
		for (TodoItem todoItem : todoItems) {
			mutableTodoItems.add(todoItem);
		}
	}
}

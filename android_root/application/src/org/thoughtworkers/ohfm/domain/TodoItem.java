package org.thoughtworkers.ohfm.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class TodoItem {
	private final String title;
	private boolean done;
	private final int habitId;
	private final int executionId;

	public TodoItem(String title, boolean done, int executionId, int habitId) {
		this.title = title;
		this.done = done;
		this.executionId = executionId;
		this.habitId = habitId;
	}
	
	public String getTitle() {
		return title;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public int getHabitId() {
		return habitId;
	}

	public int getExecutionId() {
		return executionId;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<TodoItem> fromJson(String json) {
		Map fromJson = new Gson().fromJson(json, Map.class);
		int id = ((Double) fromJson.get("id")).intValue();
		
		List<Map> todoItemsFromJson = (List<Map>) fromJson.get("todo_items");
		List<TodoItem> result = new ArrayList<TodoItem>();
		for (Map itemFromJson : todoItemsFromJson) {
			String title = (String) itemFromJson.get("title");
			boolean done = (Boolean) itemFromJson.get("done");
			int habitId = ((Double) itemFromJson.get("habit_id")).intValue();
			
			TodoItem todoItem = new TodoItem(title, done, id, habitId);
			result.add(todoItem);
		}
		
		return result;
	}
}

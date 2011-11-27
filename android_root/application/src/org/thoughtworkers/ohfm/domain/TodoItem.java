package org.thoughtworkers.ohfm.domain;

public class TodoItem {
	private final String title;
	private final boolean done;

	public TodoItem(String title, boolean done) {
		this.title = title;
		this.done = done;
	}
	
	public String getTitle() {
		return title;
	}

	public boolean isDone() {
		return done;
	}
}

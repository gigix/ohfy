package org.thoughtworkers.ohfm.control;

import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;

public class TodoItemCheckBox extends CheckBox {

	private final TodoItem todoItem;

	public TodoItemCheckBox(Context context, TodoItem item) {
		super(context);
		todoItem = item;
		
		append(todoItem.getTitle());
		setChecked(todoItem.isDone());
		setTextColor(Color.BLACK);
		updateBackgroundColor();
		setHeight(150);
	}

	private void updateBackgroundColor() {
		setBackgroundColor(backgroundColor(todoItem.isDone()));
	}
	
	public void updateCheckedStatus(Server server, String signInToken) {
		todoItem.setDone(isChecked());
		updateBackgroundColor();
		server.updateStatus(todoItem, signInToken);
	}

	private int backgroundColor(boolean done) {
		return done ? Color.GREEN : Color.YELLOW;
	}

}

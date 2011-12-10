package org.thoughtworkers.ohfm.control;

import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.content.Context;
import android.graphics.Color;
import android.widget.CheckBox;

public class TodoItemCheckBox extends CheckBox {

	private final TodoItem todoItem;
	private final Context context;

	public TodoItemCheckBox(Context context, TodoItem item) {
		super(context);
		this.context = context;
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
	
	public void updateCheckedStatus(final Server server, final String signInToken) {
		todoItem.setDone(isChecked());
		updateBackgroundColor();

		new AsyncJob(context) {
			@Override
			protected void job() {
				server.updateStatus(todoItem, signInToken);
			}
		}.start();		
	}

	private int backgroundColor(boolean done) {
		return done ? Color.GREEN : Color.YELLOW;
	}

}

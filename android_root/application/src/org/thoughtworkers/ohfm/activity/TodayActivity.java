package org.thoughtworkers.ohfm.activity;

import java.util.List;

import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class TodayActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addTodoItems();
	}

	private void addTodoItems() {
		Server server = Server.create(this);
		List<TodoItem> todoItems = server.fetchTodoItems(getIntent().getStringExtra(Server.SIGN_IN_TOKEN));
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		for (TodoItem todoItem : todoItems) {
			layout.addView(buildCheckBox(todoItem));
		}
		
		setContentView(layout);
	}

	private CheckBox buildCheckBox(TodoItem todoItem) {
		CheckBox checkBox = new CheckBox(this);
		checkBox.append(todoItem.getTitle());
		checkBox.setChecked(todoItem.isDone());
		checkBox.setTextColor(Color.BLACK);
		checkBox.setBackgroundColor(backgroundColor(todoItem.isDone()));
		checkBox.setHeight(150);
		return checkBox;
	}

	private int backgroundColor(boolean done) {
		return done ? Color.GREEN : Color.YELLOW;
	}
}

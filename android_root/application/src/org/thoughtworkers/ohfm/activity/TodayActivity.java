package org.thoughtworkers.ohfm.activity;

import java.util.List;

import org.thoughtworkers.ohfm.control.TodoItemCheckBox;
import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class TodayActivity extends Activity implements OnCheckedChangeListener {
	private Server server;

	@Override
	public void onCheckedChanged(CompoundButton target, boolean checked) {
		TodoItemCheckBox checkBox = (TodoItemCheckBox) target;
		checkBox.updateCheckedStatus(server);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		server = Server.create(this);
		addTodoItems();
	}

	private void addTodoItems() {
		List<TodoItem> todoItems = server.fetchTodoItems(getIntent().getStringExtra(Server.SIGN_IN_TOKEN_NAME));
		
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		for (TodoItem todoItem : todoItems) {
			layout.addView(buildCheckBox(todoItem));
		}
		
		setContentView(layout);
	}

	private CheckBox buildCheckBox(TodoItem todoItem) {
		CheckBox checkBox = new TodoItemCheckBox(this, todoItem);
		checkBox.setOnCheckedChangeListener(this);
		return checkBox;
	}
}

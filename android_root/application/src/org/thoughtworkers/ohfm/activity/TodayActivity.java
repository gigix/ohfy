package org.thoughtworkers.ohfm.activity;

import java.util.List;

import org.thoughtworkers.ohfm.R;
import org.thoughtworkers.ohfm.control.AsyncJob;
import org.thoughtworkers.ohfm.control.TodoItemCheckBox;
import org.thoughtworkers.ohfm.domain.Credential;
import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

public class TodayActivity extends Activity implements OnCheckedChangeListener {
	private Server server;

	@Override
	public void onCheckedChanged(CompoundButton target, boolean checked) {
		TodoItemCheckBox checkBox = (TodoItemCheckBox) target;
		checkBox.updateCheckedStatus(server, getSignInToken());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		server = Server.create(this);

		new AsyncJob(this) {
			@Override
			protected void job() {
				addTodoItems();
			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() != R.id.sign_out) {
			return false;
		}
		signOut();
		finish();
		return true;
	}
	
	private void signOut() {
		server.signOut();
		Credential.clear(this);
	}

	private void addTodoItems() {
		List<TodoItem> todoItems = server.fetchTodoItems(getSignInToken());

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		for (TodoItem todoItem : todoItems) {
			layout.addView(buildCheckBox(todoItem));
		}

		setContentView(layout);
	}

	private String getSignInToken() {
		return getIntent().getStringExtra(Server.SIGN_IN_TOKEN_NAME);
	}

	private CheckBox buildCheckBox(TodoItem todoItem) {
		CheckBox checkBox = new TodoItemCheckBox(this, todoItem);
		checkBox.setOnCheckedChangeListener(this);
		return checkBox;
	}
}

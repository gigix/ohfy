package org.thoughtworkers.ohfm.activity;

import java.util.List;

import org.thoughtworkers.ohfm.R;
import org.thoughtworkers.ohfm.control.AsyncJob;
import org.thoughtworkers.ohfm.control.TodoItemCheckBox;
import org.thoughtworkers.ohfm.domain.Credential;
import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

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
				renderTodoItems();
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
		if (item.getItemId() != R.id.sign_out) {
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

	private void renderTodoItems() {
		List<TodoItem> todoItems = server.fetchTodoItems(getSignInToken());

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		if (todoItems.isEmpty()) {
			renderNewPlanButton(layout);
		} else {
			renderTodoItems(todoItems, layout);
		}

		setContentView(layout);
	}

	private void renderNewPlanButton(LinearLayout layout) {
		TextView message = new TextView(this);
		message.setText("\n\nYou don't have a plan yet. How about ...\n\n");
		layout.addView(message);
		
		Button newPlanButton = new Button(this);
		newPlanButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View target) {
				Intent intent = new Intent(TodayActivity.this, NewPlanActivity.class);
				intent.putExtra(Server.SIGN_IN_TOKEN_NAME, getSignInToken());
				startActivity(intent);
			}
		});
		newPlanButton.setText("Make A Plan For Next 30 Days");
		layout.addView(newPlanButton);
	}

	private void renderTodoItems(List<TodoItem> todoItems, LinearLayout layout) {
		for (TodoItem todoItem : todoItems) {
			layout.addView(buildCheckBox(todoItem));
		}
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

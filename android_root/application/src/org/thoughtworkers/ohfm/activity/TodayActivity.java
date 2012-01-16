package org.thoughtworkers.ohfm.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
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
				renderTodoItems(false);
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
		switch (item.getItemId()) {
		case R.id.sign_out:
			signOut();
			finish();
			return true;
		case R.id.new_plan:
			newPlan();
			return true;
		default:
			return false;
		}
	}

	private void newPlan() {
		Intent intent = new Intent(this, NewPlanActivity.class);
		intent.putExtra(Server.SIGN_IN_TOKEN_NAME, getSignInToken());
		startActivity(intent);
	}

	private void signOut() {
		server.signOut();
		Credential.clear(this);
	}

	private void renderTodoItems(boolean yesterday) {
		// TODO: fetch yesterday's items (and then update their status)
		List<TodoItem> todoItems = server.fetchTodoItems(getSignInToken(), yesterday);

		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);

		if (todoItems.isEmpty()) {
			renderEmptyDay(layout, yesterday);
		} else {
			renderTodoItems(todoItems, layout, yesterday);
		}

		setContentView(layout);
	}

	private void renderEmptyDay(LinearLayout layout, boolean yesterday) {
		if (yesterday) {
			TextView message = new TextView(this);
			message.setText("\n\nYou don't have todo item yesterday. \nPlease switch back.\n\n");
			layout.addView(message);

			renderSwitchButton(layout, yesterday);
		} else {
			renderNewPlanButton(layout);
		}
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

	private void renderTodoItems(List<TodoItem> todoItems, LinearLayout layout, boolean yesterday) {
		for (TodoItem todoItem : todoItems) {
			layout.addView(buildCheckBox(todoItem));
		}

		TextView emptyLine = new TextView(this);
		layout.addView(emptyLine);

		TextView dateDisplay = new TextView(this);
		dateDisplay.setText(dateDescription(yesterday));
		layout.addView(dateDisplay);

		renderSwitchButton(layout, yesterday);
	}

	private String dateDescription(boolean yesterday) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
		if (yesterday) {
			Date date = new Date();
			date.setDate(new Date().getDate() - 1);
			return "Showing yesterday: " + simpleDateFormat.format(date);
		}
		return "Today is " + simpleDateFormat.format(new Date());
	}

	private void renderSwitchButton(LinearLayout layout, final boolean yesterday) {
		Button switchButton = new Button(this);
		switchButton.setText(yesterday ? "Today >>" : "<< Yesterday");
		layout.addView(switchButton);

		switchButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				renderTodoItems(!yesterday);
			}
		});
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

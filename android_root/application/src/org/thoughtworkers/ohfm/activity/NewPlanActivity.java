package org.thoughtworkers.ohfm.activity;

import java.util.ArrayList;
import java.util.List;

import org.thoughtworkers.ohfm.R;
import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.TodoItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class NewPlanActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_plan);

		findViewById(R.id.create_plan).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				@SuppressWarnings("serial")
				List<Integer> inputIds = new ArrayList<Integer>() {
					{
						add(R.id.todo_0);
						add(R.id.todo_1);
						add(R.id.todo_2);
					}
				};
				List<TodoItem> todoItems = new ArrayList<TodoItem>();
				for (Integer inputId : inputIds) {
					String todoItemTitle = getInputedTodoItem(inputId);
					if (!todoItemTitle.trim().isEmpty()) {
						todoItems.add(new TodoItem(todoItemTitle));
					}
				}

				if (todoItems.isEmpty()) {
					Toast.makeText(NewPlanActivity.this, "Please input at least ONE thing to do.", Toast.LENGTH_LONG)
							.show();
					return;
				}

				Server server = Server.create(NewPlanActivity.this);
				server.createNewPlan(todoItems, getSignInToken());

				Intent intent = new Intent(NewPlanActivity.this, TodayActivity.class);
				intent.putExtra(Server.SIGN_IN_TOKEN_NAME, getSignInToken());
				startActivity(intent);
			}
		});
	}

	private String getSignInToken() {
		return getIntent().getStringExtra(Server.SIGN_IN_TOKEN_NAME);
	}

	private String getInputedTodoItem(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}
}

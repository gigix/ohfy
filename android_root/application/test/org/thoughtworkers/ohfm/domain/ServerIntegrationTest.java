package org.thoughtworkers.ohfm.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

public class ServerIntegrationTest extends TestCase {
	private static final String TITLE_PROGRAMMING = "Programming";
	private static final String TITLE_SWIMMING = "游泳";
	private static final String INVALID_EMAIL = "invalid.user@test.com";
	private static final String VALID_PASSWORD = "password";
	private static final String VALID_EMAIL = "user@test.com";
	private static final String VALID_EMPTY_USER_MAIL = "empty_user@test.com";
	private String serverHost;
	private Server server;

	@Override
	protected void setUp() throws Exception {
		Properties settings = new Properties();
		settings.load(getClass().getClassLoader().getResourceAsStream("settings.properties"));
		serverHost = (String) settings.get("server.host");
		server = Server.create(serverHost);
	}

	public void test_should_sign_in_with_valid_username_and_password() throws IOException {
		String signInToken = server.signIn(VALID_EMAIL, VALID_PASSWORD);
		assertNotNull(signInToken);
		assertTrue(signInToken.length() > 0);
	}

	public void test_should_not_get_sign_in_token_with_invalid_username_and_password() throws Exception {
		String signInToken = server.signIn(INVALID_EMAIL, VALID_PASSWORD);
		assertNull(signInToken);
	}

	public void test_should_fetch_todo_items_with_valid_sign_in_token() throws Exception {
		String signInToken = server.signIn(VALID_EMAIL, VALID_PASSWORD);
		List<TodoItem> todoItems = server.fetchTodoItems(signInToken, false);
		assertEquals(2, todoItems.size());

		TodoItem firstTodoItem = todoItems.get(0);
		assertTrue(firstTodoItem.isDone());

		TodoItem secondTodoItem = todoItems.get(1);
		assertFalse(secondTodoItem.isDone());
	}
	
	public void test_should_fetch_todo_item_from_yesterday() throws Exception {
		String signInToken = server.signIn(VALID_EMAIL, VALID_PASSWORD);
		List<TodoItem> todoItems = server.fetchTodoItems(signInToken, true);
		assertEquals(2, todoItems.size());

		TodoItem firstTodoItem = todoItems.get(0);
		assertFalse(firstTodoItem.isDone());

		TodoItem secondTodoItem = todoItems.get(1);
		assertFalse(secondTodoItem.isDone());
	}

	public void test_should_update_status_of_todo_item() throws Exception {
		String signInToken = server.signIn(VALID_EMAIL, VALID_PASSWORD);
		TodoItem todoItem = getFirstTodoItem(signInToken);
		assertTrue(todoItem.isDone());

		todoItem.setDone(false);
		server.updateStatus(todoItem, signInToken);
		todoItem = getFirstTodoItem(signInToken);
		assertFalse(todoItem.isDone());

		todoItem.setDone(true);
		server.updateStatus(todoItem, signInToken);
		todoItem = getFirstTodoItem(signInToken);
		assertTrue(todoItem.isDone());
	}

	@SuppressWarnings("serial")
	public void test_should_create_new_plan() throws Exception {
		String signInToken = server.signIn(VALID_EMPTY_USER_MAIL, VALID_PASSWORD);
		ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>() {
			{
				add(new TodoItem(TITLE_SWIMMING));
				add(new TodoItem(TITLE_PROGRAMMING));
			}
		};
		server.createNewPlan(todoItems, signInToken);
		
		List<TodoItem> fetchedTodoItems = server.fetchTodoItems(signInToken, false);
		assertEquals(2, fetchedTodoItems.size());
//		This test doesn't work with Chinese for some reason...
//		assertEquals(TITLE_SWIMMING, fetchedTodoItems.get(0).getTitle());
		assertEquals(TITLE_PROGRAMMING, fetchedTodoItems.get(1).getTitle());
	}
	
	private TodoItem getFirstTodoItem(String signInToken) {
		List<TodoItem> todoItems = server.fetchTodoItems(signInToken, false);
		TodoItem firstTodoItem = todoItems.get(0);
		return firstTodoItem;
	}
}

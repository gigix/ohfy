package org.thoughtworkers.ohfm.domain;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

public class ServerIntegrationTest extends TestCase {
	private static final String INVALID_EMAIL = "invalid.user@test.com";
	private static final String VALID_PASSWORD = "password";
	private static final String VALID_EMAIL = "user@test.com";
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
		List<TodoItem> todoItems = server.fetchTodoItems(signInToken);
		assertEquals(2, todoItems.size());

		TodoItem firstTodoItem = todoItems.get(0);
		assertTrue(firstTodoItem.isDone());
		
		TodoItem secondTodoItem = todoItems.get(1);
		assertFalse(secondTodoItem.isDone());
	}
}

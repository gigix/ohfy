package org.thoughtworkers.ohfm.domain;

import java.util.List;

import junit.framework.TestCase;

public class TodoItemTest extends TestCase {
	public void test_parse_json_to_todo_items() throws Exception {
		String json = "{\"id\":2,\"todo_items\":["
				+ "{\"title\":\"学Android开发\",\"done\":true,\"habit_id\":1},"
				+ "{\"title\":\"游泳\",\"done\":false,\"habit_id\":2}]}";
		List<TodoItem> todoItems = TodoItem.fromJson(json);
		assertEquals(2, todoItems.size());

		TodoItem firstTodoItem = todoItems.get(0);
		assertEquals("学Android开发", firstTodoItem.getTitle());
		assertTrue(firstTodoItem.isDone());
		
		TodoItem secondTodoItem = todoItems.get(1);
		assertEquals("游泳", secondTodoItem.getTitle());
		assertFalse(secondTodoItem.isDone());
	}
}

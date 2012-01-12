package org.thoughtworkers.ohfm.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServerImpl extends Server {
	private final String serverHost;

	public ServerImpl(String serverHost) {
		this.serverHost = serverHost;
	}

	@SuppressWarnings("serial")
	@Override
	public String signIn(final String email, final String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>() {
			{
				add(new BasicNameValuePair("email", email));
				add(new BasicNameValuePair("password", password));
			}
		};

		HttpPost request = new HttpPost(urlToApi("sign_in"));
		HttpResponse response = doPost(request, params);

		Header tokenHeader = response.getFirstHeader(SIGN_IN_TOKEN_NAME);
		if (tokenHeader == null) {
			return null;
		}

		return tokenHeader.getValue();
	}

	@Override
	public void signOut() {
		// TODO Implement server-side sign out (if necessary)
	}

	@Override
	public List<TodoItem> fetchTodoItems(String signInToken, boolean yesterday) {
		HttpGet request = new HttpGet(urlToApi("todos"));
		request.addHeader(SIGN_IN_TOKEN_NAME, signInToken);
		String content = fetch(request);
		return TodoItem.fromJson(content);
	}

	@Override
	public void updateStatus(final TodoItem todoItem, String signInToken) {
		@SuppressWarnings("serial")
		List<NameValuePair> params = new ArrayList<NameValuePair>() {
			{
				add(new BasicNameValuePair("execution_id", todoItem.getExecutionId().toString()));
				add(new BasicNameValuePair("habit_id", todoItem.getHabitId().toString()));
				add(new BasicNameValuePair("done", todoItem.isDone().toString()));
			}
		};

		HttpPost request = new HttpPost(urlToApi("todos"));
		request.setHeader(SIGN_IN_TOKEN_NAME, signInToken);

		doPost(request, params);
	}

	@Override
	public void createNewPlan(final List<TodoItem> todoItems, String signInToken) {
		HttpPost request = new HttpPost(urlToApi("plans"));
		request.setHeader(SIGN_IN_TOKEN_NAME, signInToken);

		@SuppressWarnings("serial")
		List<NameValuePair> params = new ArrayList<NameValuePair>() {
			{
				for (int i = 0; i < todoItems.size(); i++) {
					add(new BasicNameValuePair(String.format("habit_names[%d]", i), todoItems.get(i).getTitleEncoded()));
				}
			}
		};
		doPost(request, params);
	}

	private HttpResponse doPost(HttpPost request, List<NameValuePair> params) {
		HttpResponse response;
		try {
			request.setEntity(new UrlEncodedFormEntity(params));
			response = new DefaultHttpClient().execute(request);
		} catch (Exception e) {
			throw new OhfmException(e);
		}
		return response;
	}

	private String fetch(HttpUriRequest request) {
		try {
			HttpResponse response = new DefaultHttpClient().execute(request);
			return readInputStream(response.getEntity().getContent());
		} catch (IOException e) {
			throw new OhfmException("Error accessing server", e);
		}
	}

	private String readInputStream(InputStream inputStream) throws IOException {
		BufferedReader responseReader = new BufferedReader(new InputStreamReader(inputStream));
		String content = "";
		String row;
		while ((row = responseReader.readLine()) != null) {
			content += row;
		}
		return content;
	}

	private String urlToApi(String apiName) {
		return String.format("http://%s/api/%s", serverHost, apiName);
	}

}

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
		HttpResponse response;
		try {
			request.setEntity(new UrlEncodedFormEntity(params));
			response = new DefaultHttpClient().execute(request);
		} catch (Exception e) {
			throw new OhfmException(e);
		}

		Header tokenHeader = response.getFirstHeader(SIGN_IN_TOKEN_NAME);
		if (tokenHeader == null) {
			return null;
		}

		return tokenHeader.getValue();
	}

	@Override
	public List<TodoItem> fetchTodoItems(String signInToken) {
		HttpGet request = new HttpGet(urlToApi("todos"));
		request.addHeader(SIGN_IN_TOKEN_NAME, signInToken);
		String content = fetch(request);
		return TodoItem.fromJson(content);
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
		return String.format("http://%s:3000/api/%s", serverHost, apiName);
	}

}

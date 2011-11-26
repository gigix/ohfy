package org.thoughtworkers.ohfm.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServerImpl extends Server {

	@SuppressWarnings("serial")
	@Override
	public String signIn(final String email, final String password) {
		List<NameValuePair> params = new ArrayList<NameValuePair>() {
			{
				add(new BasicNameValuePair("email", email));
				add(new BasicNameValuePair("password", password));
			}
		};
		
		HttpPost request = new HttpPost("http://192.168.13.20:3000/api/sign_in");
		HttpResponse response;
		try {
			request.setEntity(new UrlEncodedFormEntity(params));
			response = new DefaultHttpClient().execute(request);
		} catch (Exception e) {
			throw new OhfmException(e);
		}
		
		Header[] allHeaders = response.getAllHeaders();
		for (Header header : allHeaders) {
			System.out.println(header.getName() + " : " + header.getValue());
		}
		
		Header tokenHeader = response.getFirstHeader("sign_in_token");
		return tokenHeader.getValue();
	}

}

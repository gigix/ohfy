package org.thoughtworkers.ohfm.domain;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

public class ServerIntegrationTest extends TestCase {
	private String serverHost;

	@Override
	protected void setUp() throws Exception {
		Properties settings = new Properties();
		settings.load(getClass().getClassLoader().getResourceAsStream("settings.properties"));
		serverHost = (String) settings.get("server.host");
	}
	
	public void test_should_sign_in_with_valid_username_and_password() throws IOException {
		Server server = Server.create(serverHost);
		String signInToken = server.signIn("user@test.com", "password");
		assertNotNull(signInToken);
		assertTrue(signInToken.length() > 0);
	}	
	
	public void test_should_not_get_sign_in_token_with_invalid_username_and_password() throws Exception {
		String signInToken = Server.create(serverHost).signIn("invalid.user@test.com", "password");
		assertNull(signInToken);
	}
}

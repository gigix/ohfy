package org.thoughtworkers.ohfm.domain;

import junit.framework.TestCase;

public class ServerIntegrationTest extends TestCase {
	public void test_should_sign_in_with_valid_username_and_password() {
		Server server = Server.create();
		String signInToken = server.signIn("user@test.com", "password");
		assertNotNull(signInToken);
		assertTrue(signInToken.length() > 0);
	}	
}

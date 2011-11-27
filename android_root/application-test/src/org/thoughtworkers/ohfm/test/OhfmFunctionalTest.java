package org.thoughtworkers.ohfm.test;

import org.thoughtworkers.ohfm.activity.OhfmActivity;
import org.thoughtworkers.ohfm.activity.TodayActivity;
import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.ServerStub;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class OhfmFunctionalTest extends ActivityInstrumentationTestCase2<OhfmActivity> {
	private static final Class<OhfmActivity> ACTIVITY_UNDER_TEST = OhfmActivity.class;

	private Solo solo;

	public OhfmFunctionalTest() {
		super(ACTIVITY_UNDER_TEST.getPackage().getName(), ACTIVITY_UNDER_TEST);
	}

	@Override
	protected void setUp() throws Exception {
		Server.setInstance(new ServerStub());
		
		solo = new Solo(getInstrumentation(), getActivity());
		assertCurrentActivity(OhfmActivity.class);

		assertStringExist("Email");
		assertStringExist("Password");
		assertStringExist("Sign in");
	}

	@Override
	protected void tearDown() throws Exception {
		try {
			solo.finalize();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		getActivity().finish();
		super.tearDown();
	}

	public void test_should_be_able_to_sign_in_with_valid_email_and_password() throws Exception {
		signIn("user@test.com", "password");
		assertCurrentActivity(TodayActivity.class);
	}

	public void test_should_fail_signing_in_with_invalid_email_and_password() throws Exception {
		signIn("not.exist@test.com", "password");
		assertCurrentActivity(OhfmActivity.class);
	}
	
	public void test_should_show_todo_items_in_today_activity() throws Exception {
		signIn("user@test.com", "password");
		assertStringExist("学Android开发");
	}

	private void signIn(String email, String password) {
		solo.enterText(0, email);
		solo.enterText(1, password);
		solo.clickOnButton(0);
	}

	private void assertCurrentActivity(Class<? extends Activity> expectedClass) {
		solo.assertCurrentActivity("Unexpected Activity!", expectedClass);
	}

	private void assertStringExist(String expectedString) {
		assertEquals(true, solo.searchText(expectedString));
	}
}

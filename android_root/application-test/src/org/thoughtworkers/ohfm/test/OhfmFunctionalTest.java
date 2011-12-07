package org.thoughtworkers.ohfm.test;

import java.util.ArrayList;

import org.thoughtworkers.ohfm.activity.OhfmActivity;
import org.thoughtworkers.ohfm.activity.TodayActivity;
import org.thoughtworkers.ohfm.domain.Server;
import org.thoughtworkers.ohfm.domain.ServerStub;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;

import com.jayway.android.robotium.solo.Solo;

public class OhfmFunctionalTest extends ActivityInstrumentationTestCase2<OhfmActivity> {
	private static final String VALID_PASSWORD = "password";

	private static final String VALID_EMAIL = "user@test.com";

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

	public void test_simplest_usage_scenario() throws Exception {
		should_not_allow_invalid_sign_in();
		should_allow_valid_sign_in();
		should_show_todo_items_after_signed_in();
		should_be_able_to_change_status_of_todo_items_after_signed_in();
	}

	public void test_should_remember_username_and_password_after_signed_in() throws Throwable {
		signIn(VALID_EMAIL, VALID_PASSWORD);
		restartApplication();
		
		assertEquals(VALID_EMAIL, solo.getEditText(0).getText().toString());
		assertEquals(VALID_PASSWORD, solo.getEditText(1).getText().toString());
	}

	private void restartApplication() throws Exception {
		tearDown();
		setUp();
	}

	private void should_be_able_to_change_status_of_todo_items_after_signed_in() throws Exception {
		solo.clickOnCheckBox(0);
		solo.clickOnCheckBox(1);
		restartApplication();
		
		signIn(VALID_EMAIL, VALID_PASSWORD);
		assertCheckBoxIsNotChecked(0);
		assertCheckBoxIsChecked(1);
	}

	private void should_show_todo_items_after_signed_in() {
		assertStringExist("学Android开发");		
		assertCheckBoxIsChecked(0);
		assertCheckBoxIsNotChecked(1);
	}

	private void should_allow_valid_sign_in() {
		signIn(VALID_EMAIL, VALID_PASSWORD);
		assertCurrentActivity(TodayActivity.class);
	}

	private void should_not_allow_invalid_sign_in() {
		signIn("not.exist@test.com", VALID_PASSWORD);
		assertCurrentActivity(OhfmActivity.class);
	}

	private void assertCheckBoxIsChecked(int index) {
		ArrayList<CheckBox> allCheckBoxes = solo.getCurrentCheckBoxes();
		assertTrue(allCheckBoxes.get(index).isChecked());
	}
	
	private void assertCheckBoxIsNotChecked(int index) {
		ArrayList<CheckBox> allCheckBoxes = solo.getCurrentCheckBoxes();
		assertFalse(allCheckBoxes.get(index).isChecked());
	}
	
	private void signIn(String email, String password) {
		enterText(0, email);
		enterText(1, password);
		solo.clickOnButton(0);
	}

	private void enterText(int index, String text) {
		solo.clearEditText(index);
		solo.enterText(index, text);
	}

	private void assertCurrentActivity(Class<? extends Activity> expectedClass) {
		solo.assertCurrentActivity("Unexpected Activity!", expectedClass);
	}

	private void assertStringExist(String expectedString) {
		assertEquals(true, solo.searchText(expectedString));
	}
}

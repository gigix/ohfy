package org.thoughtworkers.ohfm.test;

import org.thoughtworkers.ohfm.OhfmActivity;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class OhfmActivityTest extends ActivityInstrumentationTestCase2<OhfmActivity> {
	private static final Class<OhfmActivity> ACTIVITY_UNDER_TEST = OhfmActivity.class;

	private Solo solo;
	
	public OhfmActivityTest() {
		super(ACTIVITY_UNDER_TEST.getPackage().getName(), ACTIVITY_UNDER_TEST);
	}
	
	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		assertCurrentActivity(OhfmActivity.class);
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

	public void test_should_show_sign_in_form() throws Exception {
		assertStringExist("Email");
		assertStringExist("Password");
		assertStringExist("Sign in");
	}
	
	private void assertCurrentActivity(Class<? extends Activity> expectedClass) {
		solo.assertCurrentActivity("Unexpected Activity!", expectedClass);
	}
	
	private void assertStringExist(String expectedString) {
		assertEquals(true, solo.searchText(expectedString));
	}
}

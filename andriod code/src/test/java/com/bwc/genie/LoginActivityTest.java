package com.bwc.genie;

import com.bwc.genie.login.LoginActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class LoginActivityTest {
    private LoginActivity activity;

    private static final String loginJson = "{\"error\":false,\"user\":{\"name\":\"A\",\"email\":\"A\"," +
            "\"id\":\"17\",\"sex\":\"m\",\"phonenumber\":\"09987435\",\"dateofbirth\":\"2001-01-02\"," +
            "\"weight\":\"90.00\",\"height\":\"162.00\",\"typeofdiabetes\":\"1\",\"address\":\"Ireland\"," +
            "\"country\":\"Ireland\",\"city\":\"Dublin\",\"zipcode\":\"989\",\"created_at\":\"2016-03-11 13:19:39.704903\"}}\n";

    @Test
    public void shouldHaveHappySmiles() throws Exception {
        String error = new LoginActivity().getResources().getString(R.string.error_field_required);
        assert(error.equals("This field is required"));
    }

    @Before
    public void setup()  {
        activity = Robolectric.buildActivity(LoginActivity.class)
                .create().get();
    }
    @Test
    public void checkActivityNotNull() throws Exception {
        assertNotNull(activity);
    }

    @Test
    public void onLoginResponse() throws Exception
    {


//        Button button = (Button) activity.findViewById(R.id.btnLogin);
//        button.performClick();
//        Intent intent = Shadows.shadowOf(activity).peekNextStartedActivity();
//        assertEquals(GlucoseTestListActivity.class.getCanonicalName(), intent.getComponent().getClassName());
    }

    @Test
    public void testButtonClick() throws Exception {
//        MainActivity activity = Robolectric.buildActivity(MainActivity.class)
//                .create().get();
//        Button view = (Button) activity.findViewById(R.id.button1);
//        assertNotNull(view);
//        view.performClick();
//        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("Lala"));
    }


}
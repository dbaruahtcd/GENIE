package com.bwc.genie;

import android.content.Intent;
import android.os.Parcelable;

import com.bwc.genie.glucose.GlucoseTest;
import com.ibm.icu.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;



@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class GlucoseUnitTest {

    @Test
    public void checkGlucoseModelTest() throws Exception {
        GlucoseTest test = new GlucoseTest("1","120",new GregorianCalendar().getTimeInMillis(), true, "Tired");

        Intent intent = new Intent();
        ArrayList<Parcelable> serializable = new ArrayList<>();
        serializable.add(test);

        intent.putExtra("glucoseTest", test);
        Assert.assertSame(intent, intent.putExtra("test", serializable));
        Assert.assertEquals(serializable, intent.getExtras().get("test"));
        Assert.assertEquals(serializable, intent.getSerializableExtra("test"));
        Assert.assertEquals(test, intent.getParcelableExtra("glucoseTest"));
    }


}

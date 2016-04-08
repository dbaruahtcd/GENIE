package com.bwc.genie;


import android.content.Intent;
import android.os.Parcelable;

import com.bwc.genie.registration.RegisteredUser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "src/main/AndroidManifest.xml")
public class RegisteredUserUnitTest {

    @Test
    public void checkRegisteredUser() {
        RegisteredUser user = new RegisteredUser();
        user.setId("12");
        user.setAddress("12341234");
        user.setCity("141123");
        user.setDateOfBirth("2000-11-12");
        user.setEmail("123912912");
        user.setFullname("asdjklasmdf");
        user.setHeight("123123");
        user.setWeight("123123");
        user.setTypeOfDiabetes("1");
        user.setPhoneNumber("12314124");
        user.setPassword("1241354923413");


        Intent intent = new Intent();
        ArrayList<Parcelable> serializable = new ArrayList<>();
        serializable.add(user);

        intent.putExtra("user", user);
        Assert.assertSame(intent, intent.putExtra("userSerial", serializable));
        Assert.assertEquals(serializable, intent.getExtras().get("userSerial"));
        Assert.assertEquals(serializable, intent.getSerializableExtra("userSerial"));
        Assert.assertEquals(user, intent.getParcelableExtra("user"));

        RegisteredUser newUser = intent.getParcelableExtra("user");
        Assert.assertEquals(newUser.getDateOfBirth(), user.getDateOfBirth());
        Assert.assertEquals(newUser.getEmail(), user.getEmail());
        Assert.assertEquals(newUser.getFullname(), user.getFullname());
        Assert.assertEquals(newUser.getId(), user.getId());
        Assert.assertEquals(newUser.getTypeOfDiabetes(), user.getTypeOfDiabetes());
        Assert.assertEquals(newUser.getWeight(), user.getWeight());



    }
}

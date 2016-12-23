package edu.uah.uahnavigation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Jairo on 12/23/2016.
 */

@RunWith(AndroidJUnit4.class)
public class ActivitySplashScreenAcceptanceTests {

    @Rule
    public ActivityTestRule<SplashScreenActivity> mActivityRule
            = new ActivityTestRule<>(SplashScreenActivity.class);

    @Test
    public void testhasInternetConnectionTrue() {

    }

    @Test
    public void testhasInternetConnectionFalse() {

    }
}

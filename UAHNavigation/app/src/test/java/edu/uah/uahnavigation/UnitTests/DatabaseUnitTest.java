package edu.uah.uahnavigation.UnitTests;

import android.app.Activity;
import android.content.Context;

import org.apache.tools.ant.Main;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import edu.uah.uahnavigation.BuildConfig;
import edu.uah.uahnavigation.ClassTabActivity;
import edu.uah.uahnavigation.MainActivity;

import static org.junit.Assert.assertTrue;

/**
 * Created by Daniel on 1/21/2017.
 */
@RunWith(MyRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)
public class DatabaseUnitTest  {

    @Test
    public void testSomething() throws Exception {
        assertTrue(Robolectric.setupActivity(MainActivity.class) != null);
    }
}





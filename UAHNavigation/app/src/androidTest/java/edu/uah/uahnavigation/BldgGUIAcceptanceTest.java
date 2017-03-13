package edu.uah.uahnavigation;

import android.os.Build;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import edu.uah.model.Buildings;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Created by Daniel on 3/13/2017.
 */
@RunWith(AndroidJUnit4.class)
public class BldgGUIAcceptanceTest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testSpinners() {
        onView(withId(R.id.greetEditText))
                .perform(typeText("Buildings GUI Test"), closeSoftKeyboard());

        onView(withId(R.id.buildingbtn)).perform(click());
        //onView(withId(R.id.spinnerBuilding)).check(matches("Engineering Building")));
        //onData(allOf(is(instanceOf(Buildings.class)), is("Engineering Building"))).perform(click());
        //onView(withId(R.id.spinnerBuilding)).check(matches(withSpinnerText(containsString("Engineering Building"))));
        //onView(withId(R.id.spinnerRoom)).perform(click());
    }

}

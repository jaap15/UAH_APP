package edu.uah.uahnavigation;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Daniel on 3/14/2017.
 */

@RunWith(AndroidJUnit4.class)
public class AppNavigatorAcceptanceTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testGreet() {
        onView(withId(R.id.greetEditText))
                .perform(typeText("App Navigator Acceptance Test"), closeSoftKeyboard());

        onView(withText("Greet")).perform(click());

        onView(withId(R.id.classbtn)).perform(click());
        onView(withId(R.id.returnbtn)).perform(click());;
        onView(withId(R.id.buildingbtn)).perform(click());
        onView(withId(R.id.returnbtn)).perform(click());
    }
}

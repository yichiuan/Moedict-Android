package com.yichiuan.moedict.ui.search;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.yichiuan.moedict.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SearchActivityTest {
    @Rule
    public ActivityTestRule<SearchActivity> activityRule = new ActivityTestRule<>(
            SearchActivity.class);

    @Test
    public void search_when_general_then_show_result() {

        final String query = "我";

        onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                .perform(replaceText(query), closeSoftKeyboard());

        onView(withId(R.id.recyclerview_search_result)).check(matches(isDisplayed()));
    }

    @Test
    public void search_when_no_items_then_show_no_result() {
        onView(withId(android.support.v7.appcompat.R.id.search_src_text))
                .perform(replaceText("query"), closeSoftKeyboard());

        onView(withId(R.id.recyclerview_search_result)).check(matches(not(isDisplayed())));
    }
}
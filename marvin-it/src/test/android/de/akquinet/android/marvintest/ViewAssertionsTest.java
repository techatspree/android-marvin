package de.akquinet.android.marvintest;

import static de.akquinet.android.marvin.AndroidMatchers.hasText;
import static de.akquinet.android.marvin.AndroidMatchers.isEnabled;
import static de.akquinet.android.marvin.AndroidMatchers.isOnScreen;
import static de.akquinet.android.marvin.AndroidMatchers.isVisible;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import de.akquinet.android.marvin.AndroidTestCase;
import de.akquinet.android.marvintest.activities.ActivityB;


public class ViewAssertionsTest extends AndroidTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertEmptyActivityList();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        assertEmptyActivityList();
    }

    private void assertEmptyActivityList() {
        MatcherAssert.assertThat(getStartedActivities(),
                Matchers.<Activity> empty());
    }

    public void testDoesNotExist() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        assertThat(activity(startActivity).view(0).getView(), nullValue());
    }

    public void testExists() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        assertThat(activity(startActivity)
                .view(ActivityB.CONTENT_VIEW_ID), notNullValue());
    }

    public void testIsVisible() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        assertThat(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID),
                isVisible());
    }

    public void testIsEnabled() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        assertThat(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID),
                isEnabled());
    }

    public void testIsOnScreen() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        assertThat(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID),
                isOnScreen(activity(startActivity).rootView().getView()));
    }

    public void testFindTextViewHasText() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        // @formatter:off
         assertThat(activity(startActivity).findTextView("42").getView(), 
         		hasText("42"));
         // @formatter:on
    }

    public void testIsDisabled() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        assertThat(startActivity.findViewById(ActivityB.TEXT_VIEW_ID),
                not(isEnabled()));
    }

    public void testIsNotVisible() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        assertThat(startActivity.findViewById(ActivityB.TEXT_VIEW_ID),
                not(isVisible()));
    }

    public void testSetText() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        activity(startActivity).
                view(ActivityB.TEXT_VIEW_ID)
                .setText("What was the question?");

        assertThat(startActivity
                .findViewById(ActivityB.TEXT_VIEW_ID),
                hasText("What was the question?"));
    }

    public void testClick() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        activity(startActivity).view(ActivityB.CONTENT_VIEW_ID).click();

        assertThat(startActivity.clickIdentifier, equalTo(ActivityB.CLICK));
    }
}

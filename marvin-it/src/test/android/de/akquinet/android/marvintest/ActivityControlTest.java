package de.akquinet.android.marvintest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.util.concurrent.TimeUnit;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import de.akquinet.android.marvin.testcase.MarvinTestCase;
import de.akquinet.android.marvintest.activities.ActivityA;
import de.akquinet.android.marvintest.activities.ActivityB;
import de.akquinet.android.marvintest.activities.ActivityC;


public class ActivityControlTest extends MarvinTestCase {
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

    public void testStartsActivity() {
        assertEmptyActivityList();

        ActivityA startActivity = startActivity(ActivityA.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        // @formatter:off
        assertThat(startActivity)
                .doNothing()
                .startsActivity(ActivityB.class);
        // @formatter:on
    }

    public void testDoesNotStartActivity() {
        ActivityA startActivity = startActivity(ActivityA.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        // @formatter:off
        assertThat(startActivity)
                .doNothing()
                .doesNotStartActivity(ActivityC.class, 5, TimeUnit.SECONDS);
        // @formatter:on
    }

    private void assertEmptyActivityList() {
        MatcherAssert.assertThat(getStartedActivities(),
                Matchers.<Activity> empty());
    }
}

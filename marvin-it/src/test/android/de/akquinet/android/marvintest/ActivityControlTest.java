/*
 * Copyright 2010 akquinet
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.akquinet.android.marvintest;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import de.akquinet.android.marvin.AndroidTestCase;
import de.akquinet.android.marvintest.activities.ActivityA;
import de.akquinet.android.marvintest.activities.ActivityB;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static de.akquinet.android.marvin.AndroidMatchers.hasText;
import static org.hamcrest.Matchers.*;


public class ActivityControlTest extends AndroidTestCase {
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

    public void testFinish() {
        ActivityB activity = perform().startActivity(ActivityB.class);
        await().idle();

        MatcherAssert.assertThat(activity, notNullValue());
        activity(activity).finish();

        MatcherAssert.assertThat(activity.isFinishing(), is(true));
    }

    public void testFlipOrientation() {
        ActivityB startActivity = perform().startActivity(ActivityB.class);
        await().idle();

        MatcherAssert.assertThat(startActivity, notNullValue());
        int startOrientation = startActivity.getRequestedOrientation();

        activity(startActivity).flipOrientation();
        perform().sleep(3000);
        MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
                not(equalTo(startOrientation)));
    }

    public void testSendString() {
        ActivityB startActivity = perform().startActivity(ActivityB.class);
        await().idle();

        MatcherAssert.assertThat(startActivity, notNullValue());

        View editText = startActivity.findViewById(ActivityB.EDIT_TEXT_ID);
        MatcherAssert.assertThat(editText.requestFocus(), is(true));

        perform().instrument().sendStringSync("true");
        MatcherAssert.assertThat(editText, hasText("true"));
    }

    public void testSetOrientation() {
        ActivityB startActivity = perform().startActivity(ActivityB.class);
        startActivity
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        perform().sleep(3000);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
                is(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));
        perform().sleep(3000);

        activity(startActivity).setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
                is(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
    }

    public void testStartsActivityIndirect() {
        ActivityA startActivity = perform().startActivity(ActivityA.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        ActivityB activityB = await().activity(ActivityB.class, 30, TimeUnit.SECONDS);
        assertThat(activityB, is(not(nullValue())));
    }


    public void testStartsActivity() {
        ActivityA startActivity = perform().startActivity(ActivityA.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        await().activity(ActivityB.class, 30, TimeUnit.SECONDS);
    }

    public void testGetMostRecentlyStartedActivity() {
        final ActivityB startActivity = perform().startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        await().activity(ActivityB.class, 10, TimeUnit.SECONDS);

        Activity recentlyStarted = getActivityMonitor().getMostRecentlyStartedActivity();
        MatcherAssert.assertThat(startActivity, sameInstance(recentlyStarted));
    }

    public void testGetStartedActivities() {
        boolean aBool = false;
        boolean bBool = false;

        ActivityA aActivity = perform().startActivity(ActivityA.class);
        MatcherAssert.assertThat(aActivity, notNullValue());

        perform().sleep(2000);

        List<Activity> actList = getStartedActivities();

        for (Activity act : actList) {
            if (act instanceof ActivityA)
                aBool = true;
            else if (act instanceof ActivityB)
                bBool = true;
        }

        MatcherAssert.assertThat(aBool && bBool, is(true));
    }

    private void assertEmptyActivityList() {
        MatcherAssert.assertThat(getStartedActivities(),
                Matchers.<Activity> empty());
    }
}

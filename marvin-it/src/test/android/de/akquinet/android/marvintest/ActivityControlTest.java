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

import static de.akquinet.android.marvin.AndroidMatchers.hasText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.View;
import de.akquinet.android.marvin.AndroidTestCase;
import de.akquinet.android.marvintest.activities.ActivityA;
import de.akquinet.android.marvintest.activities.ActivityB;
import de.akquinet.android.marvintest.activities.ActivityC;


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

    public void testKeyDownUp() {
        assertEmptyActivityList();

        ActivityC startActivity = startActivity(ActivityC.class);
        waitForIdle();

        assertThat(startActivity, is(notNullValue()));

        keyDownUp(KeyEvent.KEYCODE_A);

        assertThat(startActivity.keyIdentifier,
                is(equalTo(KeyEvent.KEYCODE_A)));
        assertThat(startActivity.actionIdentifierDown, is(true));
        assertThat(startActivity.actionIdentifierUp, is(true));
    }

    public void testKey() {
        assertEmptyActivityList();

        ActivityC startActivity = startActivity(ActivityC.class);
        waitForIdle();

        MatcherAssert.assertThat(startActivity, notNullValue());
        key(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A));

        assertThat(startActivity.keyIdentifier, equalTo(KeyEvent.KEYCODE_A));
        assertThat(startActivity.actionIdentifierDown, is(true));
    }

    public void testFinish() {
        assertEmptyActivityList();

        ActivityB activity = startActivity(ActivityB.class);
        waitForIdle();

        MatcherAssert.assertThat(activity, notNullValue());
        activity(activity).finish();

        MatcherAssert.assertThat(activity.isFinishing(), is(true));
    }

    public void testFlipOrientation() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        waitForIdle();

        MatcherAssert.assertThat(startActivity, notNullValue());
        int startOrientation = startActivity.getRequestedOrientation();

        activity(startActivity).flipOrientation();
        MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
                not(equalTo(startOrientation)));
    }

    public void testSendString() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        waitForIdle();

        MatcherAssert.assertThat(startActivity, notNullValue());

        View editText = startActivity.findViewById(ActivityB.EDIT_TEXT_ID);
        MatcherAssert.assertThat(editText.requestFocus(), is(true));

        sendString("true");
        MatcherAssert.assertThat(editText, hasText("true"));
    }

    public void testClick() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        click(50, 50);

        MatcherAssert.assertThat(startActivity.clickIdentifier,
                equalTo(ActivityB.CLICK));
    }

    public void testSetOrientation() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        startActivity
                .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
                is(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));

        activity(startActivity).setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
                is(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
    }

    public void testStartsActivity() {
        assertEmptyActivityList();

        ActivityA startActivity = startActivity(ActivityA.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        waitForActivity(ActivityA.class, 30, TimeUnit.SECONDS);
    }

    public void testGetMostRecentlyStartedActivity() {

        final ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, notNullValue());

        Activity recentlyStarted = getMostRecentlyStartedActivity();
        MatcherAssert.assertThat(startActivity, sameInstance(recentlyStarted));
    }

    public void testWaitForActivity() {
        assertEmptyActivityList();

        ActivityA startActivity = startActivity(ActivityA.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));

        waitForActivity(ActivityB.class, 10, TimeUnit.SECONDS);
    }

    public void testGetStartedActivities() {
        boolean aBool = false;
        boolean bBool = false;

        assertEmptyActivityList();

        ActivityA aActivity = startActivity(ActivityA.class);
        MatcherAssert.assertThat(aActivity, notNullValue());

        sleep(2000);

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

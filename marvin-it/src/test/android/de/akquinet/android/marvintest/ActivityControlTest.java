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

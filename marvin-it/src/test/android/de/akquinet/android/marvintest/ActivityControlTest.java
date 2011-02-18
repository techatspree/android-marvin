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

import java.util.concurrent.TimeUnit;
import static org.hamcrest.Matchers.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import de.akquinet.android.marvin.testcase.MarvinTestCase;
import de.akquinet.android.marvintest.activities.ActivityA;
import de.akquinet.android.marvintest.activities.ActivityB;
import de.akquinet.android.marvintest.activities.ActivityC;

/*
 * TODO: waitForIdle, resultsIn, finish, flipOrientation, key, keyDownUp,
 * sleep, doNothing
 */

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
    
    
    public void testSendString()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         View editText = startActivity.findViewById(ActivityB.EDIT_TEXT_ID);
         MatcherAssert.assertThat(editText.requestFocus(), is(true));
         
         // @formatter:off
         assertThat(startActivity)
         		.sendString("true");
         // @formatter:on
         MatcherAssert.assertThat(((android.widget.TextView) editText).getText().toString(), is(equalTo("true")));
    }
    
    //TODO: focus fails > find out if either focus() contains errors or testFocus()
    public void testFocus() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        
        View button = startActivity.findViewById(ActivityB.BUTTON_ID);
        
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        MatcherAssert.assertThat(button.isFocused(), is(false));
        
        int y= button.getTop();
        
        // @formatter:off
        assertThat(startActivity)
        		.focus(50, y+1)
        		.waitForIdle();
        // @formatter:on
        MatcherAssert.assertThat(button.isFocused(), is(true));
    }
    
    public void testClickLong() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        
        // @formatter:off
        assertThat(startActivity)
        		.clickLong(50, 50)
        		.waitForIdle();
        // @formatter:on
        MatcherAssert.assertThat(startActivity.clickIdentifier, is(equalTo(ActivityB.LONG_CLICK)));
    }
    
    public void testClick() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        
        // @formatter:off
        assertThat(startActivity)
        		.click(50, 50)
        		.waitForIdle();
        // @formatter:on
        MatcherAssert.assertThat(startActivity.clickIdentifier, is(equalTo(ActivityB.CLICK)));
    }
    
    public void testRootView() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        
        // @formatter:off
        assertThat(startActivity)
        		.rootView()
        		.equals(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID).getRootView());
        // @formatter:on
    }
    
    public void testView() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        
        // @formatter:off
        assertThat(startActivity)
        		.view(ActivityB.CONTENT_VIEW_ID)
        		.equals(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID));
        
        assertThat(startActivity)
				.view(startActivity.viewGroup)
				.equals(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID));
        
        // @formatter:on     
    }
    
    public void testSetOrientation() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        startActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        MatcherAssert.assertThat(startActivity.getRequestedOrientation(), is(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));
        
        // @formatter:off
        assertThat(startActivity)
        		.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // @formatter:on
        
        MatcherAssert.assertThat(startActivity.getRequestedOrientation(), is(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
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

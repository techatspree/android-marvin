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

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.Matchers.*;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.View;
import de.akquinet.android.marvin.matchers.Condition;
import de.akquinet.android.marvin.matchers.ViewGroupMatchers;
import de.akquinet.android.marvin.testcase.MarvinTestCase;
import de.akquinet.android.marvintest.activities.*;

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

	public void testResultsInRunnable() {
		assertEmptyActivityList();

		final ActivityD startActivity = startActivity(ActivityD.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle()
         		.resultsIn(new Runnable(){

					@Override
					public void run() {
						sleep(5000);
						MatcherAssert.assertThat(startActivity.finished,is(true));					
					}
         			
         		});
         // @formatter:on
	}

	public void testResultsInCondition() {
		assertEmptyActivityList();

		final ActivityD startActivity = startActivity(ActivityD.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle()
         		.resultsIn(new Condition("Test resultsIn with Condition") {
                    @Override
                    public boolean matches() {
                        return startActivity.finished == true;
                    }
                }, 10, TimeUnit.SECONDS);
         // @formatter:on
	}

	public void testResultsInMatcher() {
		assertEmptyActivityList();

		ActivityD startActivity = startActivity(ActivityD.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle()
         		.resultsIn(startActivity.finished, Matchers.equalTo(true), 10, TimeUnit.SECONDS);
         // @formatter:on
	}

	public void testWaitForIdle() {
		assertEmptyActivityList();

		ActivityD startActivity = startActivity(ActivityD.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle();
         // @formatter:on

		MatcherAssert.assertThat(startActivity.finished, is(true));
	}

	public void testKeyDownUp() {
		assertEmptyActivityList();

		ActivityC startActivity = startActivity(ActivityC.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle()
         		.keyDownUp(KeyEvent.KEYCODE_A)
         		.waitForIdle();
         // @formatter:on

		MatcherAssert.assertThat(startActivity.keyIdentifier,
				is(equalTo(KeyEvent.KEYCODE_A)));
		MatcherAssert.assertThat(startActivity.actionIdentifierDown, is(true));
		MatcherAssert.assertThat(startActivity.actionIdentifierUp, is(true));
	}

	public void testKey() {
		assertEmptyActivityList();

		ActivityC startActivity = startActivity(ActivityC.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle()
         		.key(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A))
         		.waitForIdle();
         // @formatter:on

		MatcherAssert.assertThat(startActivity.keyIdentifier,
				is(equalTo(KeyEvent.KEYCODE_A)));
		MatcherAssert.assertThat(startActivity.actionIdentifierDown, is(true));
	}

	public void testSleep() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		long beforeSleep = System.currentTimeMillis();

		// @formatter:off
         assertThat(startActivity)
         		.sleep(5000);
         // @formatter:on

		MatcherAssert.assertThat(System.currentTimeMillis(),
				is(greaterThan(beforeSleep + 5000)));
	}

	public void testFinish() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
         assertThat(startActivity)
         		.waitForIdle()
         		.and()
         		.finish();
         // @formatter:on

		MatcherAssert.assertThat(startActivity.isFinishing(), is(true));
	}

	public void testFlipOrientation() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));
		int startOrientation = startActivity.getRequestedOrientation();

		// @formatter:off
         assertThat(startActivity)
         		.flipOrientation()
         		.waitForIdle();
         // @formatter:on
		MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
				is(not(equalTo(startOrientation))));
	}

	public void testSendString() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		View editText = startActivity.findViewById(ActivityB.EDIT_TEXT_ID);
		MatcherAssert.assertThat(editText.requestFocus(), is(true));

		// @formatter:off
         assertThat(startActivity)
         		.sendString("true")
         		.waitForIdle();
         // @formatter:on
		MatcherAssert.assertThat(((android.widget.TextView) editText).getText()
				.toString(), is(equalTo("true")));
	}

	// TODO: focus fails
//	public void testFocus() {
//		assertEmptyActivityList();
//
//		ActivityB startActivity = startActivity(ActivityB.class);
//
//		View view = startActivity.findViewById(ActivityB.BUTTON_ID);
//
//		MatcherAssert.assertThat(startActivity, is(notNullValue()));
//		MatcherAssert.assertThat(view.isFocused(), is(false));
//
//		int y = view.getTop();
//
//		// @formatter:off
//        assertThat(startActivity)
// 				.waitForIdle()
//        		.focus(50, y+1)
//        		.waitForIdle();
//        // @formatter:on
//		MatcherAssert.assertThat(view.isFocused(), is(true));
//	}

	public void testClickLong() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
        assertThat(startActivity)
 				.waitForIdle()
        		.clickLong(50, 50)
        		.waitForIdle();
        // @formatter:on
		MatcherAssert.assertThat(startActivity.clickIdentifier,
				is(equalTo(ActivityB.LONG_CLICK)));
	}

	public void testClick() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		// @formatter:off
        assertThat(startActivity)
				.waitForIdle()
        		.click(50, 50)
        		.waitForIdle();
        // @formatter:on
		MatcherAssert.assertThat(startActivity.clickIdentifier,
				is(equalTo(ActivityB.CLICK)));
	}

	public void testSetOrientation() {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		startActivity
				.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));
		MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
				is(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE));

		// @formatter:off
        assertThat(startActivity)
 				.waitForIdle()
 				.and()
        		.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // @formatter:on

		MatcherAssert.assertThat(startActivity.getRequestedOrientation(),
				is(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT));
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

	public void testWaitForConditionMatcher() throws TimeoutException {

		final ActivityD startActivity = startActivity(ActivityD.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		waitForCondition(startActivity.finished, Matchers.comparesEqualTo(true), 10, TimeUnit.SECONDS);
	}
	
	public void testWaitForConditionCondition() throws TimeoutException {

		final ActivityD startActivity = startActivity(ActivityD.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		waitForCondition(new Condition("Test waitForCondition") {
			@Override
			public boolean matches() {
				return startActivity.finished;
			}
		}, 10, TimeUnit.SECONDS);
	}
	
	public void testGetMostRecentlyStartedActivity(){

		final ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));
		
		Activity recentlyStarted = getMostRecentlyStartedActivity();
		MatcherAssert.assertThat(startActivity, is(equalTo(recentlyStarted)));
	}
	
	public void testWaitForActivity() {
		assertEmptyActivityList();

		ActivityA startActivity = startActivity(ActivityA.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		waitForActivity(ActivityB.class,10,TimeUnit.SECONDS);
	}
	
	public void testMoreChildrenThan() throws TimeoutException {
		assertEmptyActivityList();

		ActivityB startActivity = startActivity(ActivityB.class);
		MatcherAssert.assertThat(startActivity, is(notNullValue()));

		View parent = waitForView(startActivity, ActivityB.CONTENT_VIEW_ID, 10,
				TimeUnit.SECONDS);
		waitForCondition(parent, ViewGroupMatchers.moreChildrenThan(2),
				20, TimeUnit.SECONDS);
	}
	
	public void testGetStartedActivities() {
		boolean aBool = false;
		boolean bBool = false;
		
		assertEmptyActivityList();

		ActivityA aActivity = startActivity(ActivityA.class);
		MatcherAssert.assertThat(aActivity, is(notNullValue()));
		
		sleep(2000);
		
		List<Activity> actList = getStartedActivities();

		for(Activity act : actList)
		{
			if(act instanceof ActivityA) aBool=true;
			else if(act instanceof ActivityB) bBool=true;
		}
		
		MatcherAssert.assertThat(aBool && bBool, is(true));
	}
	
	private void assertEmptyActivityList() {
		MatcherAssert.assertThat(getStartedActivities(),
				Matchers.<Activity> empty());
	}
}

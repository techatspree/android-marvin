package de.akquinet.android.marvintest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import android.widget.TextView;
import de.akquinet.android.marvin.testcase.MarvinTestCase;
import de.akquinet.android.marvintest.activities.ActivityB;

public class ViewAssertionsTest extends MarvinTestCase {

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
    
    public void testDoesNotExist()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(0)
         		.doesNotExist();
         // @formatter:on
    }
    
    public void testExists()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.CONTENT_VIEW_ID)
         		.exists();
         // @formatter:on
    }
    
    public void testIsVisible()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.CONTENT_VIEW_ID)
         		.isVisible();
         // @formatter:on
    }
    
    public void testIsEnabled()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.CONTENT_VIEW_ID)
         		.isEnabled();
         // @formatter:on
    }
    
    public void testIsOnScreen()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.CONTENT_VIEW_ID)
         		.isOnScreen();
         // @formatter:on
    }
    //TODO:fails
    public void testFindTextViewHasText()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.CONTENT_VIEW_ID)
         		.findTextView("42")
         		.hasText("42");
         // @formatter:on
    }
    
    public void testIsDisabled()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.TEXT_VIEW_ID)
         		.isDisabled();
         // @formatter:on
    }
    
    public void testIsNotVisible()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.TEXT_VIEW_ID)
         		.isNotVisible();
         // @formatter:on
    }
    
    public void testSetText()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.TEXT_VIEW_ID)
         		.setText("What's the question?")
         		.waitForIdle();
         // @formatter:on
         
		MatcherAssert.assertThat(((TextView) startActivity
				.findViewById(ActivityB.TEXT_VIEW_ID)).getText().toString(),
				is("What's the question?"));
	}
}

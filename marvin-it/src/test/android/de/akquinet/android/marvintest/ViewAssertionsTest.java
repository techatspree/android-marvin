package de.akquinet.android.marvintest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import de.akquinet.android.marvin.assertions.ViewFilter;
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
         		.setText("What was the question?")
         		.waitForIdle();
         // @formatter:on
         
		MatcherAssert.assertThat(((TextView) startActivity
				.findViewById(ActivityB.TEXT_VIEW_ID)).getText().toString(),
				is("What was the question?"));
	}
    
    public void testRootView() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        
        // @formatter:off
        assertThat(startActivity)
 				.waitForIdle()
 				.and()
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
				.waitForIdle()
				.and()
        		.view(ActivityB.CONTENT_VIEW_ID)
        		.equals(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID));
        
        assertThat(startActivity)
 				.waitForIdle()
 				.and()
				.view(startActivity.viewGroup)
				.equals(startActivity.findViewById(ActivityB.CONTENT_VIEW_ID));
        
        // @formatter:on     
    }
    
    public void testFindView()
    {
    	 assertEmptyActivityList();

         ActivityB startActivity = startActivity(ActivityB.class);
         MatcherAssert.assertThat(startActivity, is(notNullValue()));
         
         // @formatter:off
         assertThat(startActivity)
         		.view(ActivityB.CONTENT_VIEW_ID)
         		.findView(new ViewFilter(){
					@Override
					public boolean accept(View view) {
						if(view.getId()==ActivityB.BUTTON_ID)return true;
						return false;
					}    			
         		});
         		
         // @formatter:on
	}
    
    public void testClick() {
        assertEmptyActivityList();

        ActivityB startActivity = startActivity(ActivityB.class);
        MatcherAssert.assertThat(startActivity, is(notNullValue()));
        
        // @formatter:off
        assertThat(startActivity)
 				.waitForIdle()
 				.and()
        		.view(ActivityB.CONTENT_VIEW_ID)
        		.click()
        		.waitForIdle();
        // @formatter:on
        
        MatcherAssert.assertThat(startActivity.clickIdentifier, is(equalTo(ActivityB.CLICK)));
    }
}

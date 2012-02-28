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
package de.akquinet.android.marvin;

import android.app.Activity;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.test.InstrumentationTestCase;
import android.util.Log;
import de.akquinet.android.marvin.actions.ActionFactory;
import de.akquinet.android.marvin.actions.ActivityAction;
import de.akquinet.android.marvin.actions.PerformAction;
import de.akquinet.android.marvin.actions.AwaitAction;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;
import de.akquinet.android.marvin.monitor.StartedActivity;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * <p/>
 * Base test case class for Marvin tests. It provides methods to control
 * activites, bind to services and define various assertions.
 * <p/>
 * <p/>
 * Every activity that is started directly or indirectly within a test method is
 * finished automatically on {@link #tearDown()} to ensure a clean state for
 * following tests. If you want to leave certain activities running, you can
 * make use of method {@link #leaveRunningAfterTearDown(Class...)}.
 * <p/>
 * <p/>
 * When overwriting {@link #setUp()} or {@link #tearDown()}, you must call the
 * super implementations.
 *
 * @author Philipp Kumar
 */
public class AndroidTestCase extends InstrumentationTestCase {
    /**
     * Activity monitor keeping track of started activites
     */
    protected ExtendedActivityMonitor activityMonitor;

    /**
     * Information on what to do with instances of certain activity types on
     * {@link #tearDown()}
     */
    private Map<Class<? extends Activity>, TearDownAction> tearDownActions =
            new HashMap<Class<? extends Activity>, TearDownAction>();

    /**
     * {@link ServiceConnection} instances created using the bindService(..)
     * methods.
     */
    private Map<IBinder, ServiceConnection> serviceConnections =
            new HashMap<IBinder, ServiceConnection>();

    /*
     * 
     */

    /**
     * <p/>
     * Define certain Activity types as to be left running even after
     * {@link #tearDown()}.
     * <p/>
     * <p/>
     * Per default, an activity started during {@link #setUp()} as well as
     * during the test method run itself is finished on {@link #tearDown()},
     * even those that are not explicitely started from the test. To prevent
     * this for certain activity types, call this method. Activities of the
     * given types will be left running until instrumentation finishes (in
     * general, this is after the whole test suite is finished).
     *
     * @param activityClasses the activity types not to be finished on {@link #tearDown()}
     */
    public final void leaveRunningAfterTearDown(
            Class<? extends Activity>... activityClasses) {
        for (Class<? extends Activity> activityClass : activityClasses) {
            tearDownActions.put(activityClass, TearDownAction.LEAVE_RUNNING);
        }
    }

    public final void finishOnTearDown(
            Class<? extends Activity>... activityClasses) {
        for (Class<? extends Activity> activityClass : activityClasses) {
            tearDownActions.put(activityClass, TearDownAction.FINISH);
        }
    }

    /**
     * Returns a list of all activities that were started since the beginning of
     * this test, in the order of their start time.
     */
    public final List<Activity> getStartedActivities() {
        SortedSet<StartedActivity> startedActivities = new TreeSet<StartedActivity>(
                new Comparator<StartedActivity>() {
                    public int compare(StartedActivity a1, StartedActivity a2) {
                        return (int) (a1.getStartTime() - a2.getStartTime());
                    }

                    ;
                });

        startedActivities.addAll(activityMonitor.getStartedActivities());

        ArrayList<Activity> results = new ArrayList<Activity>();
        for (StartedActivity startedActivity : startedActivities) {
            results.add(startedActivity.getActivity());
        }

        return results;
    }

    public <T extends Activity> ActivityAction activity(T activity) {
        return ActionFactory.createActivityAction(getInstrumentation(), activityMonitor, activity);
    }

    public AwaitAction await() {
        return ActionFactory.createAwaitAction(getInstrumentation(), activityMonitor);
    }

    public PerformAction perform() {
        return ActionFactory.createPerformAction(getInstrumentation(), activityMonitor);
    }

    public <T> void assertThat(T actual, Matcher<? super T> matcher) {
        MatcherAssert.assertThat(actual, matcher);
    }

    public <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        MatcherAssert.assertThat(reason, actual, matcher);
    }

    /**
     * Convenience method for
     * <i>getInstrumentation().getContext().getString(int)</i>
     */
    public final String getString(int resId) {
        return getInstrumentation().getContext().getString(resId);
    }

    public final ExtendedActivityMonitor getActivityMonitor() {
        return activityMonitor;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activityMonitor =
                new ExtendedActivityMonitor(getInstrumentation());
        this.activityMonitor.start();
        getInstrumentation().waitForIdleSync();
        Thread.sleep(3000);
    }

    @Override
    protected void tearDown() throws Exception {
        this.activityMonitor.stop();
        getInstrumentation().waitForIdleSync();

        for (StartedActivity activity : activityMonitor.getStartedActivities()) {
            if (!shallLeaveRunning(activity.getActivity())
                    && !activity.getActivity().isFinishing()) {
                try {
                    Log.i(getClass().getName(), "Finishing activity: "
                            + activity.getActivity().getClass().getName());
                    activity.getActivity().finish();
                    getInstrumentation().waitForIdleSync();
                    Thread.sleep(1000);
                    getInstrumentation().waitForIdleSync();
                } catch (Exception e) {
                    Log.e(getClass().getName(),
                            "Problem on activity finish:", e);
                }
            }
        }
        activityMonitor.clear();

        // Unbind services that were bound using bindService(..) methods
        for (ServiceConnection connection : serviceConnections.values()) {
            getInstrumentation().getTargetContext().unbindService(connection);
        }
        serviceConnections.clear();

        getInstrumentation().waitForIdleSync();

        super.tearDown();
    }

    private boolean shallLeaveRunning(Activity activity) {
        TearDownAction tearDownAction =
                this.tearDownActions.get(activity.getClass());
        if (tearDownAction != null) {
            return tearDownAction.equals(TearDownAction.LEAVE_RUNNING);
        }
        return false;
    }

    private enum TearDownAction {
        LEAVE_RUNNING, FINISH;
    }
}

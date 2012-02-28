package de.akquinet.android.marvin.actions;


import android.app.Activity;
import android.app.Instrumentation;
import android.view.View;
import de.akquinet.android.marvin.matchers.Condition;
import de.akquinet.android.marvin.matchers.util.WaitForConditionUtil;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;
import org.hamcrest.Matcher;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public interface AwaitAction {
    /**
     * Blocks until an {@link android.app.Activity} of the given type is started. The
     * instance of the started activity is then returned. If such an activity is
     * not started within the given amount of time, this method returns null.
     *
     * @param activityClass the type of activity to wait for
     * @param timeout       amount of time to wait for activity start
     * @param timeUnit      the time unit of the timeout parameter
     * @return the activity waited for, or null if timeout was reached before
     *         any suitable activity was started
     */
    <T extends Activity> T activity(
            Class<T> activityClass, long timeout, TimeUnit timeUnit);

    /**
     * <p/>
     * Waits for a view with the given id to become visible.
     * <p/>
     * <p/>
     * Will return immediately if this view is already existent and visible. If
     * it is not, the method blocks until the view appears or the given timeout
     * is reached. In the latter case, an {@link java.util.concurrent.TimeoutException} is thrown.
     *
     * @param activity the activity instance
     * @param viewId   the id of the view to wait for
     * @param timeout  the timeout value
     * @param timeUnit the time unit of the timeout value
     * @return the View object of the existent and visible view
     * @throws java.util.concurrent.TimeoutException
     *          if the view does not appear withing the given time frame
     */
    View view(Activity activity, int viewId,
                     long timeout, TimeUnit timeUnit) throws TimeoutException;

    /**
     * <p/>
     * Waits for the given {@link de.akquinet.android.marvin.matchers.Condition} to match.
     * <p/>
     * <p/>
     * Will return immediately if the condition already matches. If it does not,
     * the method blocks until the condition matches or the given timeout is
     * reached. In the latter case, an {@link java.util.concurrent.TimeoutException} is thrown.
     *
     * @param condition the {@link de.akquinet.android.marvin.matchers.Condition} that shall match
     * @param timeout   the timeout value
     * @param timeUnit  the time unit of the timeout value
     * @throws java.util.concurrent.TimeoutException
     *          if the condition does not match within the given time frame
     */
    void condition(Condition condition, long timeout, TimeUnit timeUnit)
            throws TimeoutException;

    /**
     * <p/>
     * Waits for the given Hamcrest {@link org.hamcrest.Matcher} to match on a given item.
     * <p/>
     * <p/>
     * Will return immediately if the matcher already matches on this item. If
     * it does not, the method blocks until the matcher matches or the given
     * timeout is reached. In the latter case, an {@link java.util.concurrent.TimeoutException} is
     * thrown.
     *
     * @param item     the item to be passed to the matcher
     * @param matcher  the hamcrest {@link org.hamcrest.Matcher} that shall match
     * @param timeout  the timeout value
     * @param timeUnit the time unit of the timeout value
     * @throws java.util.concurrent.TimeoutException
     *          if the matcher does not match on the given item within the
     *          given time frame
     */
    <T> void condition(Object item, Matcher<T> matcher,
                              long timeout, TimeUnit timeUnit) throws TimeoutException;

    void idle();
}

class AwaitActionImpl extends BaseActionImpl implements AwaitAction {
    public AwaitActionImpl(Instrumentation instrumentation,
                           ExtendedActivityMonitor activityMonitor) {
        super(instrumentation, activityMonitor);
    }

    @Override
    public final <T extends Activity> T activity(
            Class<T> activityClass, long timeout, TimeUnit timeUnit) {
        return activityMonitor.waitForActivity(
                activityClass, timeout, timeUnit);
    }

    @Override
    public final View view(final Activity activity, final int viewId,
                                  long timeout, TimeUnit timeUnit) throws TimeoutException {
        condition(new Condition("View exists and is visible") {
            @Override
            public boolean matches() {
                View view = activity.findViewById(viewId);
                return view != null && view.getVisibility() == View.VISIBLE;
            }
        }, timeout, timeUnit);

        return activity.findViewById(viewId);
    }

    @Override
    public final void condition(Condition condition, long timeout, TimeUnit timeUnit)
            throws TimeoutException {
        WaitForConditionUtil.waitForCondition(condition, timeout, timeUnit);
    }

    @Override
    public final <T> void condition(Object item, Matcher<T> matcher,
                                           long timeout, TimeUnit timeUnit) throws TimeoutException {
        WaitForConditionUtil.waitForCondition(item, matcher, timeout, timeUnit);
    }

    @Override
    public final void idle() {
        instrumentation.waitForIdleSync();
    }
}

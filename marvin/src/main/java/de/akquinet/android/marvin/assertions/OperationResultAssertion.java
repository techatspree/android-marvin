package de.akquinet.android.marvin.assertions;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.hamcrest.Matcher;

import android.app.Activity;
import android.test.AssertionFailedError;
import de.akquinet.android.marvin.assertions.util.Counter;
import de.akquinet.android.marvin.matchers.Condition;
import de.akquinet.android.marvin.matchers.util.WaitForConditionUtil;
import de.akquinet.android.marvin.monitor.StartedActivity;


/**
 * Assertion type that follows an operation in the assertion chain.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
public interface OperationResultAssertion<T extends Activity>
        extends InstrumentationAssertion<T> {
    Counter counter = new Counter();

    /**
     * Follow up with an {@link ActivityAssertion} checking results of the
     * operation.
     */
    ActivityAssertion<T> resultsIn();

    /**
     * <p>
     * Follow up the operation by waiting for the given {@link Condition} to
     * match.
     * 
     * <p>
     * Will return immediately if the condition already matches. If it does not,
     * the method blocks until the condition matches or the given timeout is
     * reached. In the latter case, the assertion fails.
     * 
     * @param condition
     *            the {@link Condition} that shall match
     * @param timeout
     *            the timeout value
     * @param timeUnit
     *            the time unit of the timeout value
     * @throws AssertionFailedError
     *             if the condition does not match within the given time frame
     */
    ConjunctionAssertion<T> resultsIn(Condition condition,
            long timeout, TimeUnit timeUnit);

    /**
     * <p>
     * Follow up the operation by waiting for the given {@link Matcher} to
     * match.
     * 
     * <p>
     * Will return immediately if the matcher already matches on this item. If
     * it does not, the method blocks until the matcher matches or the given
     * timeout is reached. In the latter case, the assertion fails.
     * 
     * @param item
     *            the item to be passed to the matcher
     * @param matcher
     *            the hamcrest {@link Matcher} that shall match
     * @param timeout
     *            the timeout value
     * @param timeUnit
     *            the time unit of the timeout value
     * @throws AssertionFailedError
     *             if the matcher does not match on the given item within the
     *             given time frame
     */
    <S> ConjunctionAssertion<T> resultsIn(Object item, Matcher<S> matcher,
            long timeout, TimeUnit timeUnit);

    /**
     * Follow up the operation by running the given {@link Runnable} object.
     * There you can define custom assertions to check the operation result.
     */
    ConjunctionAssertion<T> resultsIn(Runnable runnable);

    /**
     * Special implication of the operation and asserts that an activity of the
     * given type will be started within 30 seconds. Use
     * {@link #startsActivity(Class, long, TimeUnit)} to define a custom
     * timeout.
     * 
     * @param <A>
     *            the activity type
     * @param activityClass
     *            the activity type
     * @return a {@link ConjunctionAssertionImpl} to follow up with another
     *         assertion chain
     */
    <A extends Activity> ConjunctionAssertion<T> startsActivity(
            Class<A> activityClass);

    /**
     * Special implication of the operation and asserts that an Activity of the
     * given type will be started within the given timeframe (in milliseconds).´
     * 
     * @param activityClass
     *            the activity type
     * @param timeout
     *            the time to wait until the assertion fails
     * @param timeUnit
     *            the unit of the timeout parameter
     * @return a {@link ConjunctionAssertionImpl} to follow up with another
     *         assertion chain
     */
    ConjunctionAssertion<T> startsActivity(
            Class<? extends Activity> activityClass, long timeout, TimeUnit timeUnit);

    /**
     * Special implication of the operation and asserts that an Activity of the
     * given type will <b>not</b> be started within the given timeframe (in
     * milliseconds).
     * 
     * @param activityClass
     *            the activity type
     * @param timeout
     *            the time to wait until the assertion fails
     * @param timeUnit
     *            the unit of the timeout parameter
     * @return a {@link ConjunctionAssertionImpl} to follow up with another
     *         assertion chain
     */
    ConjunctionAssertion<T> doesNotStartActivity(
            Class<? extends Activity> activityClass, long timeout, TimeUnit timeUnit);

    /**
     * Follow up with another assertion.
     */
    ActivityAndResultAssertion<T> and();
}


/*
 * Implementation
 */

class OperationResultAssertionImpl<T extends Activity> extends
        InstrumentationAssertionImpl<T> implements OperationResultAssertion<T> {
    private static final int DEFAULT_WAIT_FOR_ACTIVITY_SECONDS = 30;
    private static final long STARTTIME_TOLERANCE = 1000;

    public OperationResultAssertionImpl(AssertionContext<T> context) {
        super(context);
        OperationResultAssertion.counter.count++;
    }

    public ActivityAssertion<T> resultsIn() {
        getInstrumentation().waitForIdleSync();
        return new ActivityAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> resultsIn(Condition condition,
            long timeout, TimeUnit timeUnit) {
        try {
            WaitForConditionUtil.waitForCondition(condition, timeout, timeUnit);
        }
        catch (TimeoutException e) {
            throw new AssertionFailedError(e.getMessage());
        }
        return new ConjunctionAssertionImpl<T>(context);
    }

    public <S> ConjunctionAssertion<T> resultsIn(Object item, Matcher<S> matcher,
            long timeout, TimeUnit timeUnit) {
        try {
            WaitForConditionUtil.waitForCondition(item, matcher, timeout, timeUnit);
        }
        catch (TimeoutException e) {
            throw new AssertionFailedError(e.getMessage());
        }
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> resultsIn(Runnable runnable) {
        getInstrumentation().waitForIdleSync();
        runnable.run();
        return new ConjunctionAssertionImpl<T>(context);
    }

    public <A extends Activity> ConjunctionAssertion<T> startsActivity(
            Class<A> activityClass) {
        return startsActivity(activityClass, DEFAULT_WAIT_FOR_ACTIVITY_SECONDS, TimeUnit.SECONDS);
    }

    public ConjunctionAssertion<T> startsActivity(
            Class<? extends Activity> activityClass, long timeout, TimeUnit timeUnit) {
        long timeoutInMs = timeUnit.toMillis(timeout);
        if (!activityIsStartedWithinTimeframe(activityClass, timeoutInMs)) {
            String message = "Timeout hit (" + timeoutInMs
                    + " ms) while waiting for activity to start: "
                    + activityClass.getName();
            message += "\n" + "Started activities: "
                    + getActivityMonitor().getStartedActivities();
            Assert.fail(message);
        }

        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> doesNotStartActivity(
            Class<? extends Activity> activityClass, long timeout, TimeUnit timeUnit) {
        long timeoutInMs = timeUnit.toMillis(timeout);
        if (activityIsStartedWithinTimeframe(activityClass, timeoutInMs)) {
            String message = "Activity was started"
                    + " but it should not have been started: "
                    + activityClass.getName();
            Assert.fail(message);
        }

        return new ConjunctionAssertionImpl<T>(context);
    }

    public ActivityAndResultAssertion<T> and() {
        getInstrumentation().waitForIdleSync();
        return new ActivityAndResultAssertionImpl<T>(context);
    }

    private boolean activityIsStartedWithinTimeframe(
            Class<? extends Activity> activityClass, long timeoutInMs) {
        if (activityClass == null) {
            throw new IllegalArgumentException("activityClass was null");
        }
        if (timeoutInMs < 0) {
            throw new IllegalArgumentException("timeoutInMs was negative");
        }

        /*
         * The activity might have been started already before we reach this
         * piece of code.
         * Thus, let's see if an activity was started in the meantime that is of
         * the type we intended to wait for. Although, if it was started before
         * our operation, we do not consider this as a new activity start
         * (obviously).
         */
        List<StartedActivity> startedActivities = getActivityMonitor().getStartedActivities();
        for (StartedActivity activity : startedActivities) {
            if (activity.getActivity().getClass().equals(activityClass)
                    && activity.getStartTime() >= context.getLastOperationTimestamp()
                            - STARTTIME_TOLERANCE) {
                return true;
            }
        }

        Activity activityWaitedFor = getActivityMonitor().waitForActivity(
                 activityClass, timeoutInMs, TimeUnit.MILLISECONDS);

        return activityWaitedFor != null;
    }
}

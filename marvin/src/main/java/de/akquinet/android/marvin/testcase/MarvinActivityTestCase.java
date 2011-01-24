package de.akquinet.android.marvin.testcase;

import android.app.Activity;
import de.akquinet.android.marvin.assertions.ActivityAssertion;
import de.akquinet.android.marvin.assertions.AssertionFactory;


/**
 * <p>
 * Base test case class for Marvin tests that focus on a single {@link Activity}.
 * 
 * <p>
 * Extend this class and, in your parameterless constructor, call super with the
 * class object of the activity you want to test. The activity will be
 * automatically started during {@link #setUp()} and finished during
 * {@link #tearDown()}, as well as any other activity started directly or
 * indirectly during this test (be sure to call super when you overwrite those
 * methods).
 * 
 * <p>
 * The activity instance is obtained by calling {@link #getActivity()}.
 * 
 * <p>
 * When overwriting {@link #setUp()} or {@link #tearDown()}, you must call the
 * super implementations.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
public class MarvinActivityTestCase<T extends Activity>
        extends MarvinTestCase {
    private final Class<T> activityType;
    private T activity;

    public MarvinActivityTestCase(Class<T> activityType) {
        this.activityType = activityType;
    }

    /**
     * Define a new chain of assertions for the activity under test.
     * 
     * @return an {@link ActivityAssertion} object on which you can call methods
     *         to continue the chain and thus define the actual assertion(s)
     */
    public ActivityAssertion<T> assertThat() {
        return AssertionFactory.newActivityAssertion(
                this.activity, getInstrumentation(), activityMonitor);
    }

    /**
     * @return the activity under test
     */
    protected T getActivity() {
        return this.activity;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = startActivity(this.activityType);
    }
}

package de.akquinet.android.marvin.assertions;

import android.app.Activity;
import android.app.Instrumentation;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


/**
 * Base class for assertions. Every assertion is linked to an {@link Activity}
 * instance and has access to the test {@link Instrumentation} instance.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
abstract class MarvinBaseAssertion<T extends Activity> {
    protected final AssertionContext<T> context;

    protected MarvinBaseAssertion(AssertionContext<T> context) {
        this.context = context;
    }

    protected T getActivity() {
        return context.getActivity();
    }

    protected Instrumentation getInstrumentation() {
        return context.getInstrumentation();
    }

    protected ExtendedActivityMonitor getActivityMonitor() {
        return context.getActivityMonitor();
    }
}

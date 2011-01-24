package de.akquinet.android.marvin.assertions;

import android.app.Activity;
import android.app.Instrumentation;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


public class AssertionFactory {
    private AssertionFactory() {
    }

    public static <T extends Activity> ActivityAssertion<T> newActivityAssertion(
            T activity, Instrumentation instrumentation,
            ExtendedActivityMonitor activityMonitor) {
        return new ActivityAssertionImpl<T>(
                new AssertionContext<T>(activity, instrumentation, activityMonitor));
    }
}

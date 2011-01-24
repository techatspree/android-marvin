package de.akquinet.android.marvin.assertions;

import android.app.Instrumentation;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


class AssertionContext<T> {
    private final long startTimestamp;
    private long lastOperationTimestamp = 0;

    private final T activity;
    private final Instrumentation instrumentation;
    private final ExtendedActivityMonitor activityMonitor;

    public AssertionContext(T activity, Instrumentation instrumentation,
            ExtendedActivityMonitor activityMonitor) {
        this.startTimestamp = System.currentTimeMillis();
        this.activity = activity;
        this.instrumentation = instrumentation;
        this.activityMonitor = activityMonitor;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getLastOperationTimestamp() {
        return lastOperationTimestamp;
    }

    public void actionPerformed() {
        this.lastOperationTimestamp = System.currentTimeMillis();
    }

    public T getActivity() {
        return activity;
    }

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public ExtendedActivityMonitor getActivityMonitor() {
        return activityMonitor;
    }
}

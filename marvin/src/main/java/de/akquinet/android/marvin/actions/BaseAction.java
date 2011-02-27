package de.akquinet.android.marvin.actions;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


public interface BaseAction {
}


class BaseActionImpl implements BaseAction {
    private final long startTimestamp;
    private long lastOperationTimestamp = 0;

    private final Instrumentation instrumentation;
    private final ExtendedActivityMonitor activityMonitor;

    public BaseActionImpl(Instrumentation instrumentation, ExtendedActivityMonitor activityMonitor) {
        this.startTimestamp = System.currentTimeMillis();
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

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public ExtendedActivityMonitor getActivityMonitor() {
        return activityMonitor;
    }

    protected void click(float x, float y) {
        MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
        MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
        try {
            getInstrumentation().sendPointerSync(downEvent);
            getInstrumentation().sendPointerSync(upEvent);
        }
        catch (SecurityException e) {
            Assert.fail("Click on (" + x + "," + y + ") failed.");
        }
    }
}

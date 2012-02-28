package de.akquinet.android.marvin.actions;

import junit.framework.Assert;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


public interface BaseAction
{
}


class BaseActionImpl implements BaseAction
{
    private final long startTimestamp;
    private long lastOperationTimestamp = 0;

    protected final Instrumentation instrumentation;
    protected final ExtendedActivityMonitor activityMonitor;

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

    public void click(float x, float y) {
        MotionEvent downEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 0);
        MotionEvent upEvent = MotionEvent.obtain(SystemClock.uptimeMillis(),
                SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 0);
        try {
            instrumentation.sendPointerSync(downEvent);
            instrumentation.sendPointerSync(upEvent);
        }
        catch (SecurityException e) {
            Assert.fail("Click on (" + x + "," + y + ") failed.");
        }
    }
}

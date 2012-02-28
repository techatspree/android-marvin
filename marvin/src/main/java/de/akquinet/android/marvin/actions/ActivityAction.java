package de.akquinet.android.marvin.actions;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


public interface ActivityAction
{
    Activity get();
    
    FindViewAction view();

    /**
     * Flips orientation of this activity from portrait to landscape and vice
     * versa.
     */
    void flipOrientation();

    /**
     * <p>
     * Set the orientation of this activity to the given value.
     * 
     * <p>
     * Example:
     * <code>setOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)</code>
     * 
     * @param orientation
     *            An orientation constant as used in
     *            {@link ActivityInfo#screenOrientation}.
     * 
     * @see ActivityInfo
     */
    void setOrientation(int orientation);

    /**
     * Destroy this activity by calling {@link Activity#finish()} on it.
     */
    void finish();
}


class ActivityActionImpl<T extends Activity> extends BaseActionImpl implements ActivityAction
{
    protected final T activity;

    public ActivityActionImpl(Instrumentation instrumentation,
            ExtendedActivityMonitor activityMonitor, T activity) {
        super(instrumentation, activityMonitor);

        this.activity = activity;
    }

    public Activity get() {
        return activity;
    }

    public void flipOrientation() {
        actionPerformed();
        int currentOrientation = get().getRequestedOrientation();
        if (currentOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            get().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            get().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void setOrientation(int orientation) {
        actionPerformed();
        get().setRequestedOrientation(orientation);
    }

    public void finish() {
        actionPerformed();
        get().finish();
    }

    public FindViewAction view() {
        return ActionFactory.createFindViewAction(instrumentation, activityMonitor, activity);
    }

}

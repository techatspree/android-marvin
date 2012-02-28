package de.akquinet.android.marvin.actions;

import android.app.Activity;
import android.app.Instrumentation;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


public class ActionFactory {
    public static BaseAction createBaseAction(
            Instrumentation instrumentation, ExtendedActivityMonitor activityMonitor) {
        return new BaseActionImpl(instrumentation, activityMonitor);
    }

    public static AwaitAction createAwaitAction(
            Instrumentation instrumentation, ExtendedActivityMonitor activityMonitor) {
        return new AwaitActionImpl(instrumentation, activityMonitor);
    }

    public static PerformAction createPerformAction(
            Instrumentation instrumentation, ExtendedActivityMonitor activityMonitor) {
        return new PerformActionImpl(instrumentation, activityMonitor);
    }

    public static <T extends Activity> ActivityAction createActivityAction(
            Instrumentation instrumentation, ExtendedActivityMonitor activityMonitor, T activity) {
        return new ActivityActionImpl<T>(instrumentation, activityMonitor, activity);
    }

    public static <T extends Activity> FindViewAction createFindViewAction(
            Instrumentation instrumentation, ExtendedActivityMonitor activityMonitor, T activity) {
        return new FindViewActionImpl<T>(instrumentation, activityMonitor, activity);
    }
}

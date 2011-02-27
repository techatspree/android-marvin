package de.akquinet.android.marvin.actions;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;


public interface ViewAction {
    View getView();

    void click();

    void setText(String text);
}


class ViewActionImpl<T extends Activity> extends BaseActionImpl implements ViewAction {
    private final T activity;
    private final View view;

    public ViewActionImpl(ActivityActionImpl<T> activityAction, View view) {
        super(activityAction.getInstrumentation(), activityAction.getActivityMonitor());

        this.activity = activityAction.getActivity();
        this.view = view;
    }

    public ViewActionImpl(ActivityActionImpl<T> activityAction, int viewId) {
        super(activityAction.getInstrumentation(), activityAction.getActivityMonitor());

        this.activity = activityAction.getActivity();
        this.view = activityAction.getActivity().findViewById(viewId);
    }

    public View getView() {
        return view;
    }

    @Override
    public void click() {
        Assert.assertNotNull("View should exist but does not.", view);

        actionPerformed();

        final List<Boolean> monitor = new ArrayList<Boolean>();

        // Call the OnClickListener of the view in UI thread
        activity.runOnUiThread(new Runnable() {
            public void run() {
                boolean listenerRegistered = view.performClick();
                synchronized (monitor) {
                    monitor.add(listenerRegistered);
                    monitor.notifyAll();
                }
            }
        });

        synchronized (monitor) {
            try {
                while (monitor.isEmpty()) {
                    monitor.wait();
                }
            }
            catch (InterruptedException e) {
            }
        }

        if (monitor.size() > 0 && Boolean.FALSE.equals(monitor.get(0))) {
            /*
             * There was no OnClickListener registered. But there may be a view
             * above/below our view that has a listener. It would have gotten
             * invoked on a user click, so we perform a click at the coordinates
             * of our view to reach the same behavior.
             */
            int[] location = new int[2];
            view.getLocationOnScreen(location);

            click(location[0], location[1]);
        }

        getInstrumentation().waitForIdleSync();
    }

    @Override
    public void setText(final String text) {
        Assert.assertNotNull("View should exist but does not.", view);

        if (!(view instanceof TextView)) {
            Assert.fail("View is no TextView");
        }

        actionPerformed();

        activity.runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) view).setText(text);
            }
        });

        getInstrumentation().waitForIdleSync();
    }
}

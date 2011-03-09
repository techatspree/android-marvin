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
    /**
     * Clicks on the given coordinates.
     */
    void click(float x, float y);

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

    ViewAction rootView();

    ViewAction view(int id);

    ViewAction findTextView(final String regex);

    ViewAction findView(ViewFilter filter);
}


class ActivityActionImpl<T extends Activity> extends BaseActionImpl implements ActivityAction
{
    private final T activity;

    public ActivityActionImpl(Instrumentation instrumentation,
            ExtendedActivityMonitor activityMonitor, T activity) {
        super(instrumentation, activityMonitor);

        this.activity = activity;
    }

    public T getActivity() {
        return activity;
    }

    public void flipOrientation() {
        actionPerformed();
        int currentOrientation = getActivity().getRequestedOrientation();
        if (currentOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public void setOrientation(int orientation) {
        actionPerformed();
        getActivity().setRequestedOrientation(orientation);
    }

    public void finish() {
        actionPerformed();
        getActivity().finish();
    }

    @Override
    public ViewAction rootView() {
        View rootView = getActivity().getWindow().getDecorView();

        return new ViewActionImpl<T>(this, rootView);
    }

    @Override
    public ViewAction view(int id) {
        getActivity().findViewById(id);

        return new ViewActionImpl<T>(this, id);
    }

    public ViewAction findTextView(final String regex) {
        View rootView = getActivity().getWindow().getDecorView();

        View theView = findView(rootView, new ViewFilter() {
            @Override
            public boolean accept(View view) {
                return view instanceof TextView
                        && ((TextView) view).getText().toString()
                                .matches(regex);
            }
        });

        return new ViewActionImpl<T>(this, theView);
    }

    public ViewAction findView(ViewFilter filter) {
        View rootView = getActivity().getWindow().getDecorView();

        View theView = findView(rootView, filter);
        return new ViewActionImpl<T>(this, theView);
    }

    private View findView(View view, ViewFilter filter) {
        if (filter.accept(view)) {
            return view;
        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View childView = findView(group.getChildAt(i), filter);
                if (childView != null) {
                    return childView;
                }
            }
        }

        return null;
    }
}

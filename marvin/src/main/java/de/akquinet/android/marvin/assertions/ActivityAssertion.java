package de.akquinet.android.marvin.assertions;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import de.akquinet.android.marvin.assertions.util.Counter;


/**
 * <p>
 * Represents an assertion on an activity and is the main assertion type of
 * Marvin.
 * 
 * <p>
 * An assertion chain starts with this type.
 * 
 * <p>
 * TODO: Missing functionality to check for and control dialogs. Possibly
 * achievable by getting the decor views, may necessitate reflective access.
 * 
 * @author Philipp Kumar
 * @param <T>
 *            the activity type
 */
public interface ActivityAssertion<T extends Activity>
        extends InstrumentationAssertion<T> {
    Counter counter = new Counter();

    /**
     * Define a {@link ViewAssertion} on the view with the given id.
     * 
     * @param viewId
     *            the android id of the view
     */
    ViewAssertion<T> view(int viewId);

    /**
     * Define a {@link ViewAssertion} on the given view.
     * 
     * @param view
     *            the android view
     */
    ViewAssertion<T> view(View view);

    /**
     * Define a {@link ViewAssertion} on the root view of this getActivity().
     * 
     * @param view
     *            the android view
     */
    ViewAssertion<T> rootView();

    /**
     * Flips orientation of this activity from portrait to landscape and vice
     * versa.
     */
    OperationResultAssertion<T> flipOrientation();

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
    OperationResultAssertion<T> setOrientation(
            int orientation);

    /**
     * Close this activity by calling {@link Activity#finish()} on it.
     */
    OperationResultAssertion<T> finish();
}


/*
 * Implementation
 */

class ActivityAssertionImpl<T extends Activity>
        extends InstrumentationAssertionImpl<T> implements ActivityAssertion<T> {
    public ActivityAssertionImpl(AssertionContext<T> context) {
        super(context);
        ActivityAssertion.counter.count++;
    }

    public ViewAssertion<T> view(int viewId) {
        return new ViewAssertionImpl<T>(context, viewId);
    }

    public ViewAssertion<T> view(View view) {
        return new ViewAssertionImpl<T>(context, view);
    }

    public ViewAssertion<T> rootView() {
        return new ViewAssertionImpl<T>(context, getActivity().getWindow().getDecorView());
    }

    public OperationResultAssertion<T> flipOrientation() {
        context.actionPerformed();
        int currentOrientation = getActivity().getRequestedOrientation();
        if (currentOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        else {
            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> setOrientation(int orientation) {
        context.actionPerformed();
        getActivity().setRequestedOrientation(orientation);

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> finish() {
        context.actionPerformed();
        getActivity().finish();
        return new OperationResultAssertionImpl<T>(context);
    }
}

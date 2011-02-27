package de.akquinet.android.marvin.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import android.view.View;


public class IsOnScreen<T extends View> extends BaseMatcher<T> {
    private final T rootView;

    public IsOnScreen(T rootView) {
        this.rootView = rootView;
    }

    public boolean matches(Object arg) {
        if (!(arg instanceof View)) {
            throw new IllegalArgumentException("not a view");
        }

        View view = (View) arg;

        // The following code is part of the Android SDK,
        // android/test/ViewAsserts.java (Apache License 2.0)

        int[] xy = new int[2];
        view.getLocationOnScreen(xy);

        int[] xyRoot = new int[2];
        rootView.getLocationOnScreen(xyRoot);

        int y = xy[1] - xyRoot[1];

        return y >= 0 && y <= view.getRootView().getHeight();
    }

    public void describeTo(Description description) {
        description.appendText("isOnScreen(")
                .appendValue(rootView)
                .appendText(")");
    }

    @Factory
    public static <T extends View> Matcher<T> isOnScreen(T rootView) {
        return new IsOnScreen<T>(rootView);
    }
}

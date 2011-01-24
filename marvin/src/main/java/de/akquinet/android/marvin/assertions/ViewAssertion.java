/*
 * Copyright 2010 akquinet
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.akquinet.android.marvin.assertions;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.app.Activity;
import android.test.ViewAsserts;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.akquinet.android.marvin.assertions.util.Counter;


/**
 * <p>
 * Represents an assertion defined on a {@link View}.
 * <p>
 * A view in Android is almost anything that is displayed within an activity
 * (images, buttons, textfields, ...).
 * <p>
 * The view itself is not required to exist (see {@link #doesNotExist()}).
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
public interface ViewAssertion<T extends Activity> {
    Counter counter = new Counter();

    /**
     * <p>
     * Define a {@link ViewAssertion} on a {@link TextView} that is either the
     * current view or one of its children. This text view is required to hold a
     * text that matches the given regular expression.
     * <p>
     * As a button is also a text view, you can use this to get a button as
     * well.
     * <p>
     * Note that this view is not required to exist in order to call this
     * method. Therefore, a possible chain might continue with
     * {@link #doesNotExist()} to assert non-existance.
     * 
     * @param regex
     *            a the regular expression the text view's text is matched
     *            against
     */
    ViewAssertion<T> findTextView(String regex);

    /**
     * <p>
     * Define a {@link ViewAssertion} on a {@link View} that is either the
     * current view or one of its children. This view is required to match
     * against the given {@link ViewFilter}.
     * <p>
     * Note that this view is not required to exist in order to call this
     * method. Therefore, a possible chain might continue with
     * {@link #doesNotExist()} to assert non-existance.
     * 
     * @param filter
     *            a filter to determine a matching view in the view hierarchy
     */
    ViewAssertion<T> findView(ViewFilter filter);

    /**
     * Asserts that the view exists in the getActivity().
     */
    ConjunctionAssertion<T> exists();

    /**
     * Asserts that the view does not exist in the getActivity().
     */
    ConjunctionAssertion<T> doesNotExist();

    /**
     * Asserts that the view exists in the activity and is visible.
     */
    ConjunctionAssertion<T> isVisible();

    /**
     * Asserts that the view exists in the activity but is not visible.
     */
    ConjunctionAssertion<T> isNotVisible();

    /**
     * Asserts that the view is currently visible on the screen.
     */
    ConjunctionAssertion<T> isOnScreen();

    /**
     * Asserts that the view has the given text. Note that this assertion will
     * fail on views that are not of type {@link TextView}.
     */
    ConjunctionAssertion<T> hasText(final String text);

    /**
     * Asserts that the view is enabled.
     */
    ConjunctionAssertion<T> isEnabled();

    /**
     * Asserts that the view is disabled.
     */
    ConjunctionAssertion<T> isDisabled();

    /**
     * Performs a click event on the view, causing its onclick listener to
     * fire.
     * <p>
     * In quite a number of cases, there is no listener attached directly to the
     * view. In this case, the screen coordinates of the view are obtained and a
     * click event is sent with that screen position via instrumentation.
     */
    OperationResultAssertion<T> click();

    /**
     * Set the text of this view. Note that this assertion will fail on views
     * that are not of type {@link TextView}.
     */
    OperationResultAssertion<T> setText(final String text);
}


/*
 * Implementation
 */

class ViewAssertionImpl<T extends Activity> extends MarvinBaseAssertion<T>
        implements ViewAssertion<T> {
    public static volatile int instances = 0;

    private final int viewId;
    private final View view;

    public ViewAssertionImpl(AssertionContext<T> context, int viewId) {
        super(context);
        ViewAssertion.counter.count++;
        this.viewId = viewId;
        this.view = this.getActivity().findViewById(this.viewId);
    }

    public ViewAssertionImpl(AssertionContext<T> context, View view) {
        super(context);
        instances++;
        this.viewId = view != null ? view.getId() : -1;
        this.view = view;
    }

    public ConjunctionAssertion<T> exists() {
        Assert.assertNotNull("View should exist but does not.", view);
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> doesNotExist() {
        Assert.assertNull("View should not exist but does.", view);
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ViewAssertion<T> findTextView(final String regex) {
        View theView = findView(this.view, new ViewFilter() {
            @Override
            public boolean accept(View view) {
                return view instanceof TextView
                        && ((TextView) view).getText().toString()
                                .matches(regex);
            }
        });
        return new ViewAssertionImpl<T>(context, theView);
    }

    public ViewAssertion<T> findView(ViewFilter filter) {
        View theView = findView(this.view, filter);
        return new ViewAssertionImpl<T>(context, theView);
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

    public ConjunctionAssertion<T> isVisible() {
        exists();

        Assert.assertEquals("View should be visible but is not.",
                View.VISIBLE, view.getVisibility());
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> isNotVisible() {
        if (view != null) {
            Assert.assertTrue("View should be invisible but is visible.",
                    view.getVisibility() == View.INVISIBLE
                            || view.getVisibility() == View.GONE);
        }
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> isOnScreen() {
        exists();

        ViewAsserts.assertOnScreen(view.getRootView(), view);

        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> hasText(final String text) {
        exists();

        if (!(view instanceof TextView)) {
            Assert.fail("View is non-TextView");
        }

        Assert.assertEquals(text, ((TextView) view).getText());
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> isEnabled() {
        exists();

        Assert.assertTrue("View is not enabled but should be",
                view.isEnabled());
        return new ConjunctionAssertionImpl<T>(context);
    }

    public ConjunctionAssertion<T> isDisabled() {
        exists();

        Assert.assertFalse("View is enabled but should not be",
                view.isEnabled());
        return new ConjunctionAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> click() {
        context.actionPerformed();
        exists();

        final List<Boolean> monitor = new ArrayList<Boolean>();

        // Call the OnClickListener of the view in UI thread
        getActivity().runOnUiThread(new Runnable() {
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

            new ActivityAssertionImpl<T>(context).click(location[0], location[1]);
        }

        getInstrumentation().waitForIdleSync();

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> setText(final String text) {
        context.actionPerformed();
        exists();

        if (!(view instanceof TextView)) {
            Assert.fail("View is no TextView");
        }

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ((TextView) view).setText(text);
            }
        });

        getInstrumentation().waitForIdleSync();

        return new OperationResultAssertionImpl<T>(context);
    }
}

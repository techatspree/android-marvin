package de.akquinet.android.marvin.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.view.ViewGroup;


public class ViewGroupMatchers {
    /**
     * Has the {@link ViewGroup} more children than value?
     */
    public static Matcher<Integer> moreChildrenThan(final int value) {
        return new BaseMatcher<Integer>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("a child count ").appendText("greater than");
                description.appendText(" ").appendValue(value);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("was ").appendValue(((ViewGroup) item).getChildCount());
            }

            @Override
            public boolean matches(Object item) {
                checkType(item);

                return ((ViewGroup) item).getChildCount() > value;
            }
        };
    }

    private static void checkType(Object argument) {
        if (!(argument instanceof ViewGroup)) {
            throw new IllegalArgumentException("expected a " + ViewGroup.class.getSimpleName());
        }
    }
}

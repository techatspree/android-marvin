package de.akquinet.android.marvin.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.ViewGroup;


public class ViewGroupComparison<T extends ViewGroup> extends TypeSafeMatcher<T>
{
    private static final int LESS_THAN = -1;
    private static final int GREATER_THAN = 1;
    private static final int EQUAL = 0;
    private final int childCount;
    private final int minCompare, maxCompare;

    private ViewGroupComparison(int childCount, int minCompare, int maxCompare) {
        this.childCount = childCount;
        this.minCompare = minCompare;
        this.maxCompare = maxCompare;
    }

    @Override
    public boolean matchesSafely(T viewGroup) {
        int compare = Integer.signum(viewGroup.getChildCount() - childCount);
        return minCompare <= compare && compare <= maxCompare;
    }

    @Override
    public void describeMismatchSafely(T viewGroup, Description mismatchDescription) {
        mismatchDescription.appendValue("child count of " + viewGroup).appendText(" was ")
                         .appendText(comparison(viewGroup.getChildCount() - childCount))
                         .appendText(" ").appendValue(childCount);
    };

    public void describeTo(Description description) {
        description.appendText("a child count ").appendText(comparison(minCompare));
        if (minCompare != maxCompare) {
            description.appendText(" or ").appendText(comparison(maxCompare));
        }
        description.appendText(" ").appendValue(childCount);
    }

    private String comparison(int compare) {
        if (compare == EQUAL) {
            return "equal to";
        }
        else if (compare > EQUAL) {
            return "greater than";
        }
        else {
            return "less than";
        }
    }

    /**
     * Is value = expected?
     */
    @Factory
    public static <T extends ViewGroup> Matcher<T> equalChildrenCountAs(int value) {
        return new ViewGroupComparison<T>(value, EQUAL, EQUAL);
    }

    /**
     * Is value > expected?
     */
    @Factory
    public static <T extends ViewGroup> Matcher<T> moreChildrenThan(int value) {
        return new ViewGroupComparison<T>(value, GREATER_THAN, GREATER_THAN);
    }

    /**
     * Is value >= expected?
     */
    @Factory
    public static <T extends ViewGroup> Matcher<T> moreChildrenOrEqual(int value) {
        return new ViewGroupComparison<T>(value, EQUAL, GREATER_THAN);
    }

    /**
     * Is value < expected?
     */
    @Factory
    public static <T extends ViewGroup> Matcher<T> lessChildrenThan(int value) {
        return new ViewGroupComparison<T>(value, LESS_THAN, LESS_THAN);
    }

    /**
     * Is value <= expected?
     */
    @Factory
    public static <T extends ViewGroup> Matcher<T> lessChildrenOrEqual(int value) {
        return new ViewGroupComparison<T>(value, LESS_THAN, EQUAL);
    }
}

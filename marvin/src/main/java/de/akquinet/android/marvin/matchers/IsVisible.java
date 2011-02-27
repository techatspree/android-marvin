package de.akquinet.android.marvin.matchers;

import static org.hamcrest.core.IsNot.not;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import android.view.View;


public class IsVisible<T extends View> extends BaseMatcher<T> {
    public boolean matches(Object o) {
        if (!(o instanceof View)) {
            throw new IllegalArgumentException("not a view");
        }

        return ((View) o).getVisibility() == View.VISIBLE;
    }

    public void describeTo(Description description) {
        description.appendText("visible");
    }

    @Factory
    public static <T extends View> Matcher<T> isVisible() {
        return new IsVisible<T>();
    }

    @Factory
    public static <T extends View> Matcher<T> isNotVisible() {
        return not(new IsVisible<T>());
    }
}

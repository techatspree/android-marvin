package de.akquinet.android.marvin.matchers;

import static org.hamcrest.core.IsNot.not;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import android.view.View;


public class IsEnabled<T> extends BaseMatcher<T> {
    public boolean matches(Object o) {
        if (!(o instanceof View)) {
            throw new IllegalArgumentException("not a view");
        }

        return ((View) o).isEnabled();
    }

    public void describeTo(Description description) {
        description.appendText("enabled");
    }

    @Factory
    public static <T extends View> Matcher<T> isEnabled() {
        return new IsEnabled<T>();
    }

    @Factory
    public static <T extends View> Matcher<T> isNotEnabled() {
        return not(new IsEnabled<T>());
    }
}

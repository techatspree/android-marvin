package de.akquinet.android.marvin.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;

import android.view.View;
import android.widget.TextView;


public class HasText<T> extends BaseMatcher<T> {
    private final String text;

    public HasText(String text) {
        this.text = text;
    }

    public boolean matches(Object arg) {
        return arg instanceof TextView
                && text.equals(((TextView) arg).getText().toString());
    }

    public void describeTo(Description description) {
        description.appendText("hasText(")
                .appendValue(text)
                .appendText(")");
    }

    @Factory
    public static <T extends View> Matcher<T> hasText(String text) {
        return new HasText<T>(text);
    }
}

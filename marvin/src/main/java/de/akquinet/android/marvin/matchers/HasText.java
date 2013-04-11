package de.akquinet.android.marvin.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;
import android.widget.TextView;

public class HasText<T> extends TypeSafeMatcher<T> {
    private final String expected;

    public HasText(String expected) {
        this.expected = expected;
    }

    public boolean matchesSafely(Object arg) {
        return arg instanceof TextView
                && expected.equals(((TextView) arg).getText().toString());
    }

    public void describeTo(Description description) {
        description.appendText("hasText(")
                .appendValue(expected)
                .appendText(")");
    }
    
    protected void describeMismatchSafely(T actual, Description mismatchDescription) {
        if (actual instanceof TextView) {
            TextView t = (TextView) actual;
            mismatchDescription.appendValue(actual) .appendText(" had text \"")
                    .appendText(t.getText().toString()).appendText("\"");
        } else {
            mismatchDescription.appendValue(actual) .appendText(" was not a TextView");
        }
    }

    @Factory
    public static <T extends View> Matcher<T> hasText(String text) {
        return new HasText<T>(text);
    }
}


//public class HasText<T> extends BaseMatcher<T> {
//    private final String text;
//
//    public HasText(String text) {
//        this.text = text;
//    }
//
//    public boolean matches(Object arg) {
//        return arg instanceof TextView
//                && text.equals(((TextView) arg).getText().toString());
//    }
//
//    public void describeTo(Description description) {
//        description.appendText("hasText(")
//                .appendValue(text)
//                .appendText(")");
//    }
//
//    @Factory
//    public static <T extends View> Matcher<T> hasText(String text) {
//        return new HasText<T>(text);
//    }
//}

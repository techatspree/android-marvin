package de.akquinet.android.marvin.matchers.util;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

import de.akquinet.android.marvin.matchers.Condition;


public class WaitForConditionUtil {
    public static <T> void waitForCondition(Object item, Matcher<T> matcher,
            long timeout, TimeUnit timeUnit) throws TimeoutException {
        long waitUntil = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        while (!matcher.matches(item) && System.currentTimeMillis() <= waitUntil) {
            sleep(250);
        }

        if (!matcher.matches(item)) {
            Description description = new StringDescription();
            description.appendText("\nExpected: ")
                       .appendDescriptionOf(matcher)
                       .appendText("\n     but: ");
            matcher.describeMismatch(item, description);

            throw new TimeoutException("Timeout hit ("
                    + timeout + " " + timeUnit.toString().toLowerCase()
                    + ") while waiting for condition to match. "
                    + description.toString());
        }
    }

    public static void waitForCondition(Condition condition, long timeout, TimeUnit timeUnit)
            throws TimeoutException {
        long waitUntil = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        while (!condition.matches() && System.currentTimeMillis() <= waitUntil) {
            sleep(250);
        }

        if (!condition.matches()) {
            throw new TimeoutException("Timeout hit ("
                    + timeout + " " + timeUnit.toString().toLowerCase()
                    + ") while waiting for condition to match: "
                    + condition.getDescription());
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException e) {
            // continue
        }
    }
}

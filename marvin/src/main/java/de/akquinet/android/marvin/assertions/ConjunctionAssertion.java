package de.akquinet.android.marvin.assertions;

import android.app.Activity;
import de.akquinet.android.marvin.assertions.util.Counter;


/**
 * Simple assertion to do conjunctions.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
public interface ConjunctionAssertion<T extends Activity> {
    Counter counter = new Counter();

    /**
     * Follow up with another assertion.
     */
    ActivityAndResultAssertion<T> and();
}


/*
 * Implementation
 */

class ConjunctionAssertionImpl<T extends Activity>
        extends MarvinBaseAssertion<T> implements ConjunctionAssertion<T> {
    public ConjunctionAssertionImpl(AssertionContext<T> context) {
        super(context);
        ConjunctionAssertion.counter.count++;
    }

    public ActivityAndResultAssertion<T> and() {
        return new ActivityAndResultAssertionImpl<T>(context);
    }
}

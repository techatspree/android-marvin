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

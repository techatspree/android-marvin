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
import android.app.Instrumentation;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


/**
 * Base class for assertions. Every assertion is linked to an {@link Activity}
 * instance and has access to the test {@link Instrumentation} instance.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
abstract class MarvinBaseAssertion<T extends Activity> {
    protected final AssertionContext<T> context;

    protected MarvinBaseAssertion(AssertionContext<T> context) {
        this.context = context;
    }

    protected T getActivity() {
        return context.getActivity();
    }

    protected Instrumentation getInstrumentation() {
        return context.getInstrumentation();
    }

    protected ExtendedActivityMonitor getActivityMonitor() {
        return context.getActivityMonitor();
    }
}

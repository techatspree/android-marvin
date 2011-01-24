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

import android.app.Instrumentation;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;


class AssertionContext<T> {
    private final long startTimestamp;
    private long lastOperationTimestamp = 0;

    private final T activity;
    private final Instrumentation instrumentation;
    private final ExtendedActivityMonitor activityMonitor;

    public AssertionContext(T activity, Instrumentation instrumentation,
            ExtendedActivityMonitor activityMonitor) {
        this.startTimestamp = System.currentTimeMillis();
        this.activity = activity;
        this.instrumentation = instrumentation;
        this.activityMonitor = activityMonitor;
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getLastOperationTimestamp() {
        return lastOperationTimestamp;
    }

    public void actionPerformed() {
        this.lastOperationTimestamp = System.currentTimeMillis();
    }

    public T getActivity() {
        return activity;
    }

    public Instrumentation getInstrumentation() {
        return instrumentation;
    }

    public ExtendedActivityMonitor getActivityMonitor() {
        return activityMonitor;
    }
}

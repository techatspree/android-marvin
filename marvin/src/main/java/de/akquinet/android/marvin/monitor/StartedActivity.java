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
package de.akquinet.android.marvin.monitor;

import java.util.Date;

import android.app.Activity;


public class StartedActivity implements Comparable<StartedActivity>
{
    private final Activity activity;
    private final long startTime;

    public StartedActivity(Activity activity, long startTime) {
        super();
        this.activity = activity;
        this.startTime = startTime;
    }

    public Activity getActivity() {
        return activity;
    }

    public long getStartTime() {
        return startTime;
    }

    @Override
    public int compareTo(StartedActivity startedActivity) {
        return (int) (this.startTime - startedActivity.getStartTime());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof StartedActivity)) {
            return false;
        }

        StartedActivity other = (StartedActivity) obj;

        return activity == other.activity && startTime == other.startTime;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 23 * result + activity.hashCode();
        result = 23 * result + (int) startTime;
        return result;
    }

    @Override
    public String toString() {
        return activity.getClass().getName() + " (" + new Date(startTime) + ")";
    }
}

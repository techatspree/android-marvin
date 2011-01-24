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

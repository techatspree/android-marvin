package de.akquinet.android.marvin.assertions.util;

public class Counter
{
    public volatile int count = 0;

    @Override
    public String toString() {
        return "" + count;
    }
}

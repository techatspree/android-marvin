package de.akquinet.android.marvintest.util;

public class TestUtils
{
    public static void sleepQuietly(long time) {
        try {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
            // ignore
        }
    }
}

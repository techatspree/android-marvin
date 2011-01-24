package de.akquinet.android.marvin.util;

import java.util.concurrent.TimeUnit;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;


public class TemporaryServiceConnection implements ServiceConnection {
    private final long timeoutInMs;

    private volatile ComponentName componentName;
    private volatile IBinder iBinder;

    public TemporaryServiceConnection(long timeout, TimeUnit timeUnit) {
        this.timeoutInMs = timeUnit.toMillis(timeout);
    }

    public synchronized ComponentName getComponentName() {
        return this.componentName;
    }

    public synchronized IBinder getBinder() {
        return this.iBinder;
    }

    public IBinder getBinderSync() {
        long time = System.currentTimeMillis();

        while (System.currentTimeMillis() < time + timeoutInMs) {
            if (this.iBinder != null) {
                synchronized (this) {
                    return this.iBinder;
                }
            }
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                return null;
            }
        }

        return null;
    }

    @Override
    public synchronized void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.componentName = componentName;
        this.iBinder = iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }
}

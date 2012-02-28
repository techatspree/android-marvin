package de.akquinet.android.marvin.actions;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import de.akquinet.android.marvin.monitor.ExtendedActivityMonitor;
import de.akquinet.android.marvin.util.TemporaryServiceConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public interface PerformAction {
    /**
     * Returns the {@link Instrumentation} instance in order to perform instrumentation
     * actions on the target application.
     *
     * @return the {@link Instrumentation} instance
     */
    Instrumentation instrument();

    /**
     * Clicks on the given coordinates.
     */
    void click(float x, float y);

    /**
     * Launches a new {@link android.app.Activity} of the given type, wait for it to start
     * and return the corresponding activity instance. The activity is later
     * finished on {@link de.akquinet.android.marvin.AndroidTestCase#tearDown()} per default.
     * <p/>
     * The function returns as soon as the activity goes idle following the call
     * to its {@link android.app.Activity#onCreate}. Generally this means it has gone
     * through the full initialization including {@link android.app.Activity#onResume} and
     * drawn and displayed its initial window.
     *
     * @param <T>           the activity type
     * @param activityClass the activity type
     * @return the created activity instance
     */
    @SuppressWarnings("unchecked")
    <T extends Activity> T startActivity(Class<T> activityClass);

    /**
     * Launches a new {@link android.app.Activity} using the given {@link android.content.Intent}. The
     * started activity is closed on {@link de.akquinet.android.marvin.AndroidTestCase#tearDown()} per default.
     * <p/>
     * The function returns as soon as the activity goes idle following the call
     * to its {@link android.app.Activity#onCreate}. Generally this means it has gone
     * through the full initialization including {@link android.app.Activity#onResume} and
     * drawn and displayed its initial window.
     *
     * @param intent the {@link android.content.Intent} to use for activity start.
     * @return the created Activity instance
     */
    Activity startActivity(Intent intent);

    /**
     * Synchronously binds to the service of the given class and returns the
     * service object. The service is auto-created (see
     * Service.BIND_AUTO_CREATE) and automatically unbound during
     * {@link de.akquinet.android.marvin.AndroidTestCase#tearDown()}.
     *
     * @param serviceClass the class of the service to bind to
     * @param timeout      Time to wait for the service before throwing an
     *                     {@link java.util.concurrent.TimeoutException}.
     * @param timeUnit     the unit of the timeout parameter
     * @throws java.util.concurrent.TimeoutException
     *          if the timeout is reached before we could connect to the
     *          service
     */
    IBinder bindService(Class<?> serviceClass, int timeout,
                        TimeUnit timeUnit) throws TimeoutException;

    /**
     * Synchronously binds to the service using the given intent and flags, and
     * return the service object. The intent and flags parameters are passed to
     * {@link android.content.Context#bindService(android.content.Intent, android.content.ServiceConnection, int)}
     * . The service is automatically unbound during {@link de.akquinet.android.marvin.AndroidTestCase#tearDown()}.
     *
     * @param serviceIntent the intent to bind to the service
     * @param flags         the flags used to bind to the service
     * @param timeout       Time to wait for the service before throwing an
     *                      {@link java.util.concurrent.TimeoutException}.
     * @param timeUnit      the unit of the timeout parameter
     * @throws java.util.concurrent.TimeoutException
     *          if the timeout is reached before we could connect to the
     *          service
     */
    IBinder bindService(Intent serviceIntent, int flags,
                        int timeout, TimeUnit timeUnit) throws TimeoutException;

    /**
     * Convenience method for {@link Thread#sleep(long)}, returns immediately on
     * interrupt.
     */
    void sleep(long ms);
}


class PerformActionImpl extends BaseActionImpl implements PerformAction {
    /**
     * {@link ServiceConnection} instances created using the bindService(..)
     * methods.
     */
    private Map<IBinder, ServiceConnection> serviceConnections =
            new HashMap<IBinder, ServiceConnection>();

    public PerformActionImpl(Instrumentation instrumentation,
                             ExtendedActivityMonitor activityMonitor) {
        super(instrumentation, activityMonitor);
    }


    @Override
    @SuppressWarnings("unchecked")
    public final <T extends Activity> T startActivity(Class<T> activityClass) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.setClassName(instrumentation.getTargetContext(), activityClass.getName());

        return (T) startActivity(intent);
    }

    @Override
    public final Activity startActivity(Intent intent) {
        return instrumentation.startActivitySync(intent);
    }

    @Override
    public Instrumentation instrument() {
        return instrumentation;
    }

    @Override
    public final IBinder bindService(Class<?> serviceClass, int timeout,
                                     TimeUnit timeUnit) throws TimeoutException {
        Intent intent = new Intent(
                instrumentation.getTargetContext(), serviceClass);
        return bindService(intent, serviceClass, Service.BIND_AUTO_CREATE,
                timeout, timeUnit);
    }

    @Override
    public final IBinder bindService(Intent serviceIntent, int flags,
                                     int timeout, TimeUnit timeUnit) throws TimeoutException {
        return bindService(serviceIntent, null, flags, timeout, timeUnit);
    }

    @Override
    public final void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            // abort sleeping
        }
    }

    private IBinder bindService(Intent serviceIntent, Class<?> serviceClass,
                                int flags, int timeout, TimeUnit timeUnit) throws TimeoutException {
        TemporaryServiceConnection serviceConnection =
                new TemporaryServiceConnection(timeout, timeUnit);
        instrumentation.getTargetContext().bindService(serviceIntent,
                serviceConnection, flags);

        IBinder serviceBinder = serviceConnection.getBinderSync();
        if (serviceBinder == null) {
            throw new TimeoutException("Timeout hit ("
                    + timeout + " " + timeUnit.toString().toLowerCase()
                    + ") while trying to connect to service"
                    + serviceClass != null ? " " + serviceClass.getName() : ""
                    + ".");
        }
        serviceConnections.put(serviceBinder, serviceConnection);
        return serviceBinder;
    }
}

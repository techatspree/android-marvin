package de.akquinet.android.marvintest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class NonExistingServiceImpl extends Service
{
    public interface NonExistingService
    {
    }

    private NonExistingServiceBinder nonExistingService = new NonExistingServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return nonExistingService;
    }

    public class NonExistingServiceBinder
            extends Binder
            implements NonExistingService
    {
    }
}

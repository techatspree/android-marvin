package de.akquinet.android.marvintest.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class AdderAndroidService extends Service {
    public interface AdderService {
        int add(int... numbers);
    }

    private AdderServiceBinder adderService = new AdderServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return adderService;
    }

    public class AdderServiceBinder
            extends Binder
            implements AdderService {
        public int add(int... numbers) {
            int sum = 0;
            for (int number : numbers) {
                sum += number;
            }
            return sum;
        }
    }
}

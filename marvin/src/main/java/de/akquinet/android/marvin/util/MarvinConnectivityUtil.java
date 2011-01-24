package de.akquinet.android.marvin.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;


public class MarvinConnectivityUtil {

    private MarvinConnectivityUtil() {
    }

    public static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static void setAirplaneMode(
            Context context, boolean newStatus) {
        boolean airplaneModeOn = isAirplaneModeOn(context);

        if (airplaneModeOn && newStatus) {
            return;
        }
        if (!airplaneModeOn && !newStatus) {
            return;
        }
        if (airplaneModeOn && !newStatus) {
            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0);
            Intent intent = new Intent
                    (Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", 0);
            context.sendBroadcast(intent);
            return;
        }
        if (!airplaneModeOn && newStatus) {

            Settings.System.putInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 1);
            Intent intent = new Intent
                    (Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", 1);
            context.sendBroadcast(intent);
            return;
        }
    }
}

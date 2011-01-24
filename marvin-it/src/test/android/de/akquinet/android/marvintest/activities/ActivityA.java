package de.akquinet.android.marvintest.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import de.akquinet.android.marvintest.util.TestUtils;


public class ActivityA extends Activity {
    @Override
    protected void onResume() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                TestUtils.sleepQuietly(1000);
                return null;
            }

            protected void onPostExecute(Void result) {
                Intent intent = new Intent(ActivityA.this, ActivityB.class);
                startActivity(intent);
            };
        }.execute();
        super.onResume();
    }
}

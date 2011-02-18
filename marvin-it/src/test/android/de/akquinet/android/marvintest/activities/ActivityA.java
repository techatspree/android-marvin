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

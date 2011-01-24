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

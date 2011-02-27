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
package de.akquinet.android.marvintest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.hamcrest.MatcherAssert;

import android.os.IBinder;
import de.akquinet.android.marvin.AndroidTestCase;
import de.akquinet.android.marvintest.services.AdderAndroidService;
import de.akquinet.android.marvintest.services.AdderAndroidService.AdderService;
import de.akquinet.android.marvintest.services.NonExistingServiceImpl;


public class ServiceBindingTest extends AndroidTestCase {
    /**
     * See if we can synchronously bind to a service.
     */
    public void testBindToExistingService() throws TimeoutException {
        IBinder adderServiceBinder =
                bindService(AdderAndroidService.class, 10, TimeUnit.SECONDS);

        MatcherAssert.assertThat(adderServiceBinder,
                instanceOf(AdderService.class));

        AdderService service = (AdderService) adderServiceBinder;
        MatcherAssert.assertThat(service.add(1, 2, 3), equalTo(1 + 2 + 3));
    }

    /**
     * Binding to a non-existing service (this one is not defined in the
     * manifest) should fail.
     */
    public void testBindToNonExistingService() {
        try {
            bindService(NonExistingServiceImpl.class, 10, TimeUnit.SECONDS);
        }
        catch (TimeoutException e) {
            // OK
            return;
        }

        Assert.fail("Expected timeout while trying to bind to non-existing service");
    }
}

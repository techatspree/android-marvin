package de.akquinet.android.marvintest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.hamcrest.MatcherAssert;

import android.os.IBinder;
import de.akquinet.android.marvin.testcase.MarvinTestCase;
import de.akquinet.android.marvintest.services.AdderAndroidService;
import de.akquinet.android.marvintest.services.AdderAndroidService.AdderService;
import de.akquinet.android.marvintest.services.NonExistingServiceImpl;


public class ServiceBindingTest extends MarvinTestCase {
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

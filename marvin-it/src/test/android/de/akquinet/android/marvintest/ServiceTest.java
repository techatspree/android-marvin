package de.akquinet.android.marvintest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.MatcherAssert;

import de.akquinet.android.marvin.testcase.MarvinServiceTestCase;
import de.akquinet.android.marvintest.services.AdderAndroidService;
import de.akquinet.android.marvintest.services.AdderAndroidService.AdderService;


public class ServiceTest extends MarvinServiceTestCase<AdderAndroidService.AdderService> {
    public ServiceTest() {
        super(AdderAndroidService.class);
    }

    public void testCorrectBinding() {
        AdderService service = getService();

        MatcherAssert.assertThat(service, notNullValue());
        MatcherAssert.assertThat(getService().add(4, 5, 6), equalTo(4 + 5 + 6));
    }
}

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
package de.akquinet.android.marvin;

import java.util.concurrent.TimeUnit;

import android.app.Service;


/**
 * <p>
 * Base test case class for Marvin tests that focus on a single Android
 * {@link Service}.
 * 
 * <p>
 * Extend this class and, in your parameterless constructor, call super with the
 * class object of the Android service you want to test. The service will be
 * automatically bound to during {@link #setUp()}.
 * 
 * <p>
 * The service binder instance is obtained by calling {@link #getService()}.
 * 
 * <p>
 * When overwriting {@link #setUp()} or {@link #tearDown()}, you must call the
 * super implementations.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the service binder type
 */
public class ServiceTestCase<T> extends AndroidTestCase {
    private final Class<? extends Service> androidServiceType;
    private T service;

    private final int timeout;
    private final TimeUnit timeUnit;

    /**
     * Creates a new {@link ServiceTestCase} that will bind to the Android
     * service with the given type.
     * 
     * @param androidServiceType
     *            the class object of the Android service we want to bind to
     */
    public ServiceTestCase(Class<? extends Service> androidServiceType) {
        this(androidServiceType, 60, TimeUnit.SECONDS);
    }

    /**
     * Creates a new {@link ServiceTestCase} that will bind to the Android
     * service with the given type.
     * 
     * @param androidServiceType
     *            the class object of the Android service we want to bind to
     * @param timeout
     *            maximum time to wait for the service binding to occur before
     *            causing the test to fail
     * @param timeUnit
     *            the time unit of the timeout parameter
     */
    public ServiceTestCase(Class<? extends Service> androidServiceType,
            int timeout, TimeUnit timeUnit) {
        this.androidServiceType = androidServiceType;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /**
     * @return the service binder object under test
     */
    public T getService() {
        return this.service;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setUp() throws Exception {
        super.setUp();

        this.service = (T) bindService(androidServiceType, timeout, timeUnit);
    }

    @Override
    protected void tearDown() throws Exception {
        // TODO: Do we need to unbind here? If so, we need access to the
        // ServiceConnection object...

        super.tearDown();
    }
}

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

import android.app.Activity;
import android.view.View;
import de.akquinet.android.marvin.actions.ActivityAction;


/**
 * <p>
 * Base test case class for tests that focus on a single {@link Activity}.
 * 
 * <p>
 * Extend this class and, in your parameterless constructor, call super with the
 * class object of the activity you want to test. The activity will be
 * automatically started during {@link #setUp()} and finished during
 * {@link #tearDown()} (be sure to call super when you overwrite those methods)
 * as well as any other activity started directly or indirectly during this
 * test.
 * 
 * <p>
 * The activity instance is obtained by calling {@link #getActivity()}.
 * 
 * <p>
 * When overwriting {@link #setUp()} or {@link #tearDown()}, you must call the
 * super implementations.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
public class ActivityTestCase<T extends Activity>
        extends AndroidTestCase
{
    private final Class<T> activityType;
    private T activity;

    public ActivityTestCase(Class<T> activityType) {
        this.activityType = activityType;
    }

    /**
     * @return the activity under test
     */
    protected T getActivity() {
        return this.activity;
    }

    protected ActivityAction activity() {
        return activity(this.activity);
    }

    /**
     * @return the root view of the activity under test
     */
    protected final View rootView() {
        return this.activity.getWindow().getDecorView();
    }

    protected View findView(int id) {
        return this.activity.findViewById(id);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = perform().startActivity(this.activityType);
    }
}

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
package de.akquinet.android.marvin.assertions;

import java.util.concurrent.TimeUnit;

import org.hamcrest.Matcher;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import de.akquinet.android.marvin.assertions.util.Counter;
import de.akquinet.android.marvin.matchers.Condition;


public interface ActivityAndResultAssertion<T extends Activity>
        extends ActivityAssertion<T>, OperationResultAssertion<T> {
    Counter counter = new Counter();
}


/*
 * Implementation
 */

class ActivityAndResultAssertionImpl<T extends Activity>
        extends MarvinBaseAssertion<T>
        implements ActivityAndResultAssertion<T> {

    private final ActivityAssertion<T> activityAssertion;
    private final OperationResultAssertion<T> resultAssertion;

    public ActivityAndResultAssertionImpl(AssertionContext<T> context) {
        super(context);
        ActivityAndResultAssertion.counter.count++;

        this.activityAssertion = new ActivityAssertionImpl<T>(context);
        this.resultAssertion = new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> finish() {
        return activityAssertion.finish();
    }

    public OperationResultAssertion<T> flipOrientation() {
        return activityAssertion.flipOrientation();
    }

    public OperationResultAssertion<T> key(KeyEvent event) {
        return activityAssertion.key(event);
    }

    public OperationResultAssertion<T> keyDownUp(int key) {
        return activityAssertion.keyDownUp(key);
    }

    public OperationResultAssertion<T> sendString(String text) {
        return activityAssertion.sendString(text);
    }

    public OperationResultAssertion<T> focus(float x, float y) {
        return activityAssertion.focus(x, y);
    }

    public OperationResultAssertion<T> click(float x, float y) {
        return activityAssertion.click(x, y);
    }

    public OperationResultAssertion<T> clickLong(float x, float y) {
        return activityAssertion.clickLong(x, y);
    }

    public OperationResultAssertion<T> setOrientation(int orientation) {
        return activityAssertion.setOrientation(orientation);
    }

    public OperationResultAssertion<T> sleep(long timeInMs) {
        return activityAssertion.sleep(timeInMs);
    }

    public OperationResultAssertion<T> run(Runnable runnable) {
        return activityAssertion.run(runnable);
    }

    public OperationResultAssertion<T> doNothing() {
        return activityAssertion.doNothing();
    }

    public ViewAssertion<T> view(int viewId) {
        return activityAssertion.view(viewId);
    }

    public ViewAssertion<T> view(View view) {
        return activityAssertion.view(view);
    }

    @Override
    public ViewAssertion<T> rootView() {
        return activityAssertion.rootView();
    }

    public OperationResultAssertion<T> waitForIdle() {
        return activityAssertion.waitForIdle();
    }

    public ActivityAndResultAssertion<T> and() {
        return resultAssertion.and();
    }

    public ConjunctionAssertion<T> doesNotStartActivity(
            Class<? extends Activity> activityClass, long timeout, TimeUnit timeUnit) {
        return resultAssertion.doesNotStartActivity(
                activityClass, timeout, timeUnit);
    }

    public ActivityAssertion<T> resultsIn() {
        return resultAssertion.resultsIn();
    }

    @Override
    public ConjunctionAssertion<T> resultsIn(Condition condition,
            long timeout, TimeUnit timeUnit) {
        return resultAssertion.resultsIn(condition, timeout, timeUnit);
    }

    @Override
    public <S> ConjunctionAssertion<T> resultsIn(Object item, Matcher<S> matcher,
            long timeout, TimeUnit timeUnit) {
        return resultAssertion.resultsIn(item, matcher, timeout, timeUnit);
    }

    public ConjunctionAssertion<T> resultsIn(Runnable runnable) {
        return resultAssertion.resultsIn(runnable);
    }

    public <A extends Activity> ConjunctionAssertion<T> startsActivity(
            Class<A> activityClass) {
        return resultAssertion.startsActivity(activityClass);
    }

    public ConjunctionAssertion<T> startsActivity(
            Class<? extends Activity> activityClass, long timeout, TimeUnit timeUnit) {
        return resultAssertion.startsActivity(activityClass, timeout, timeUnit);
    }
}

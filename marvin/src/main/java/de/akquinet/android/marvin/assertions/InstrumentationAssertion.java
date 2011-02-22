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

import junit.framework.Assert;
import android.app.Activity;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;


/**
 * Assertion making use of instrumentation, like sending events to an activity.
 * 
 * @author Philipp Kumar
 * 
 * @param <T>
 *            the activity type
 */
public interface InstrumentationAssertion<T extends Activity> {
    /**
     * <p>
     * Sends an up and down key event sync to the currently focused window.
     * 
     * <p>
     * Example: <code>keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN)</code>
     * 
     * <p>
     * You can only do this if the currently focused window belongs to the
     * application under test. In particular, this method will fail while the
     * key lock is active.
     * 
     * @param key
     *            The integer keycode for the event.
     * @see KeyEvent
     */
    OperationResultAssertion<T> keyDownUp(int key);

    /**
     * <p>
     * Sends a key event to the currently focused window.
     * 
     * <p>
     * Example: <code>keyDownUp(KeyEvent.KEYCODE_DPAD_DOWN)</code>
     * 
     * <p>
     * You can only do this if the currently focused window belongs to the
     * application under test. In particular, this method will fail while the
     * key lock is active.
     * 
     * @param event
     *            The key event.
     * @see KeyEvent
     */
    OperationResultAssertion<T> key(KeyEvent event);

    /**
     * <p>
     * Sends the key events corresponding to the given text to the currently
     * focused window.
     * 
     * <p>
     * You can only do this if the currently focused window belongs to the
     * application under test. In particular, this method will fail while the
     * key lock is active.
     * 
     * @param text
     *            The text to be sent.
     */
    OperationResultAssertion<T> sendString(String text);

    /**
     * <p>
     * Focuses the view at the given coordinates.
     * 
     * <p>
     * You can only do this if the currently focused window belongs to the
     * application under test. In particular, this method will fail while the
     * key lock is active.
     * 
     * @param x
     *            the x coordinate of the view to focus
     * @param y
     *            the y coordinate of the view to focus
     */
//    OperationResultAssertion<T> focus(float x, float y);

    /**
     * <p>
     * Clicks the view at the given coordinates.
     * 
     * <p>
     * You can only do this if the currently focused window belongs to the
     * application under test. In particular, this method will fail while the
     * key lock is active.
     * 
     * @param x
     *            the x coordinate of the view to click
     * @param y
     *            the y coordinate of the view to click
     */
    OperationResultAssertion<T> click(float x, float y);

    /**
     * <p>
     * Long-clicks the view at the given coordinates.
     * 
     * <p>
     * You can only do this if the currently focused window belongs to the
     * application under test. In particular, this method will fail while the
     * key lock is active.
     * 
     * @param x
     *            the x coordinate of the view to long-click
     * @param y
     *            the y coordinate of the view to long-click
     */
    OperationResultAssertion<T> clickLong(float x, float y);

    /**
     * Sleeps for the given amount of milliseconds. If interrupted, wakes up
     * immediately.
     */
    OperationResultAssertion<T> sleep(long timeInMs);

    /**
     * Synchronously waits until the application becomes idle.
     */
    OperationResultAssertion<T> waitForIdle();

    /**
     * Run the given runnable.
     */
    OperationResultAssertion<T> run(Runnable runnable);

    /**
     * Do nothing.
     */
    OperationResultAssertion<T> doNothing();
}


/*
 * Implementation
 */

abstract class InstrumentationAssertionImpl<T extends Activity>
        extends MarvinBaseAssertion<T> implements InstrumentationAssertion<T> {
    protected InstrumentationAssertionImpl(AssertionContext<T> context) {
        super(context);
    }

    public OperationResultAssertion<T> keyDownUp(int key) {
        context.actionPerformed();
        getInstrumentation().sendKeyDownUpSync(key);

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> key(KeyEvent event) {
        context.actionPerformed();
        getInstrumentation().sendKeySync(event);

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> sendString(String text) {
        context.actionPerformed();
        getInstrumentation().sendStringSync(text);

        return new OperationResultAssertionImpl<T>(context);
    }

//  FIXME: Test fails on this one
//    public OperationResultAssertion<T> focus(float x, float y) {
//        context.actionPerformed();
//        long downTime = SystemClock.uptimeMillis();
//        long eventTime = SystemClock.uptimeMillis();
//        MotionEvent event = MotionEvent.obtain(downTime, eventTime,
//                MotionEvent.ACTION_DOWN, x, y, 0);
//        getInstrumentation().sendPointerSync(event);
//
//        return new OperationResultAssertionImpl<T>(context);
//    }

    public OperationResultAssertion<T> click(float x, float y) {
        context.actionPerformed();
        // Reuses code from Robotium
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, x, y, 0);
        MotionEvent upEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, x, y, 0);
        try {
            getInstrumentation().sendPointerSync(downEvent);
            getInstrumentation().sendPointerSync(upEvent);
        }
        catch (SecurityException e) {
            Assert.fail("Click on (" + x + "," + y + ") failed.");
        }

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> clickLong(float x, float y) {
        context.actionPerformed();
        // Reuses code from Robotium
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent event =
                MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_DOWN, x, y, 0);
        try {
            getInstrumentation().sendPointerSync(event);
        }
        catch (SecurityException e) {
            Assert.fail("Long click on (" + x + "," + y + ") failed.");
        }
        getInstrumentation().waitForIdleSync();
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_MOVE,
                x + ViewConfiguration.getTouchSlop() / 2,
                y + ViewConfiguration.getTouchSlop() / 2, 0);
        getInstrumentation().sendPointerSync(event);
        getInstrumentation().waitForIdleSync();
        sleep((int) (ViewConfiguration.getLongPressTimeout() * 1.5f));
        eventTime = SystemClock.uptimeMillis();
        event = MotionEvent.obtain(downTime, eventTime, MotionEvent.ACTION_UP, x, y, 0);
        getInstrumentation().sendPointerSync(event);
        getInstrumentation().waitForIdleSync();
        sleep(500);

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> sleep(long timeInMs) {
        context.actionPerformed();
        try {
            Thread.sleep(timeInMs);
        }
        catch (InterruptedException e) {
            // ignore
        }

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> waitForIdle() {
        context.actionPerformed();
        try {
            Thread.sleep(1000);
        }
        catch (InterruptedException e) {
            // ignore
        }
        
        getInstrumentation().waitForIdleSync();

        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> run(Runnable runnable) {
        context.actionPerformed();
        runnable.run();
        return new OperationResultAssertionImpl<T>(context);
    }

    public OperationResultAssertion<T> doNothing() {
        context.actionPerformed();
        return new OperationResultAssertionImpl<T>(context);
    }
}

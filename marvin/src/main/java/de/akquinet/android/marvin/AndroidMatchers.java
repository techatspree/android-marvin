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

import org.hamcrest.Matcher;

import android.view.View;
import android.widget.TextView;
import de.akquinet.android.marvin.matchers.HasText;
import de.akquinet.android.marvin.matchers.IsEnabled;
import de.akquinet.android.marvin.matchers.IsOnScreen;
import de.akquinet.android.marvin.matchers.IsVisible;


/**
 * A set of Hamcrest matchers for Android.
 * 
 * @author Philipp Kumar
 */
public class AndroidMatchers {
    /**
     * Evaluates to true if the value is a {@link TextView} with a text equal to
     * the parameter.
     */
    public static <T extends View> Matcher<T> hasText(String text) {
        return HasText.<T> hasText(text);
    }

    /**
     * Evaluates to true if the view is enabled.
     */
    public static <T extends View> Matcher<T> isEnabled() {
        return IsEnabled.<T> isEnabled();
    }

    /**
     * Evaluates to true if the view is not enabled.
     */
    public static <T extends View> Matcher<T> isNotEnabled() {
        return IsEnabled.<T> isNotEnabled();
    }

    /**
     * Evaluates to true if the view is visible on the current screen, using the
     * given rootView as reference.
     */
    public static <T extends View> Matcher<T> isOnScreen(T rootView) {
        return IsOnScreen.<T> isOnScreen(rootView);
    }

    /**
     * Evaluates to true if the view is visible.
     */
    public static <T extends View> Matcher<T> isVisible() {
        return IsVisible.<T> isVisible();
    }

    /**
     * Evaluates to true if the view is not visible.
     */
    public static <T extends View> Matcher<T> isNotVisible() {
        return IsVisible.<T> isNotVisible();
    }
}

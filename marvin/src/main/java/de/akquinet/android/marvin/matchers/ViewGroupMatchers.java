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
package de.akquinet.android.marvin.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.view.ViewGroup;


public class ViewGroupMatchers {
    /**
     * Has the {@link ViewGroup} more children than value?
     */
    public static Matcher<Integer> moreChildrenThan(final int value) {
        return new BaseMatcher<Integer>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("a child count ").appendText("greater than");
                description.appendText(" ").appendValue(value);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText("was ").appendValue(((ViewGroup) item).getChildCount());
            }

            @Override
            public boolean matches(Object item) {
                checkType(item);

                return ((ViewGroup) item).getChildCount() > value;
            }
        };
    }

    private static void checkType(Object argument) {
        if (!(argument instanceof ViewGroup)) {
            throw new IllegalArgumentException("expected a " + ViewGroup.class.getSimpleName());
        }
    }
}
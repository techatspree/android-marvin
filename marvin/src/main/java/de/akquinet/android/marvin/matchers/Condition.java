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

/**
 * A simple condition that can have a description and does either match (
 * {@link #matches()} evaluates to true) or does not.
 * 
 * @author Philipp Kumar
 */
public abstract class Condition {
    private final String description;

    /**
     * Create a new condition without any description.
     */
    public Condition() {
        this("");
    }

    /**
     * Create a new condition with the given description.
     */
    public Condition(String description) {
        this.description = description;
    }

    /**
     * Evaluates the condition to true or false.
     */
    public abstract boolean matches();

    /**
     * @return the description of this condition
     */
    public String getDescription() {
        return this.description;
    }
}

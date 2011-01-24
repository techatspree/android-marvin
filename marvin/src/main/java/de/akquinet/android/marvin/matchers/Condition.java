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

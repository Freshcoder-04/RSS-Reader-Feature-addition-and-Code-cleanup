package com.sismics.security;

import java.util.Locale;
import java.util.Objects;
import org.joda.time.DateTimeZone;

/**
 * Immutable anonymous principal.
 */
public final class AnonymousPrincipal implements IPrincipal {
    /**
     * Anonymous principal name.
     */
    private static final String NAME = "anonymous";
    
    /**
     * User locale.
     */
    private final Locale locale;
    
    /**
     * User timezone.
     */
    private final DateTimeZone dateTimeZone;
    
    /**
     * Constructor of AnonymousPrincipal.
     * 
     * @param locale User locale; must not be null.
     * @param dateTimeZone User timezone; must not be null.
     */
    public AnonymousPrincipal(Locale locale, DateTimeZone dateTimeZone) {
        // Validate inputs to protect internal state.
        this.locale = Objects.requireNonNull(locale, "locale must not be null");
        this.dateTimeZone = Objects.requireNonNull(dateTimeZone, "dateTimeZone must not be null");
    }
    
    @Override
    public String getId() {
        return null;
    }
    
    @Override
    public String getName() {
        return NAME;
    }
    
    @Override
    public boolean isAnonymous() {
        return true;
    }
    
    @Override
    public Locale getLocale() {
        // Returning the locale is safe since Locale is immutable.
        return locale;
    }
    
    @Override
    public DateTimeZone getDateTimeZone() {
        // DateTimeZone is immutable, so returning it directly is acceptable.
        return dateTimeZone;
    }
    
    @Override
    public String getEmail() {
        return null;
    }
}

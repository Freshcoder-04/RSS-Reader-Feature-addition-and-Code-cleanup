package com.sismics.rest.exception;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Unauthorized access to the resource exception.
 *
 * @author jtremeaux
 */
public class ForbiddenClientException extends WebApplicationException {
    /**
     * Serial UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(ForbiddenClientException.class);

    /**
     * Constructor of ForbiddenClientException with default message.
     */
    public ForbiddenClientException() throws JSONException {
        this("ForbiddenError", "You don't have access to this resource");
    }

    /**
     * Constructor of ForbiddenClientException with custom type and message.
     * 
     * @param type Error type (e.g., ForbiddenError)
     * @param message Human readable error message
     */
    public ForbiddenClientException(String type, String message) throws JSONException {
        super(Response.status(Status.FORBIDDEN).entity(new JSONObject()
            .put("type", type)
            .put("message", message)).build());
        log.error(type + ": " + message);
    }

    /**
     * Constructor of ForbiddenClientException with custom type, message, and inner exception.
     * 
     * @param type Error type (e.g., ForbiddenError)
     * @param message Human readable error message
     * @param e Inner exception
     */
    public ForbiddenClientException(String type, String message, Exception e) throws JSONException {
        this(type, message);
        log.error(type + ": " + message, e);
    }
}